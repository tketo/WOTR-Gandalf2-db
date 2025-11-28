#!/usr/bin/env node
/**
 * build_fsm.js
 *
 * Reads `/fsm/wotr_fsm.yml` (Source of Truth) and generates:
 *   /fsm/wotr_fsm.json              ← runtime FSM
 *   /fsm/wotr_fsm_prompts.md        ← prompts documentation
 *   /fsm/wotr_fsm_table.md          ← state table
 *   /fsm/wotr_fsm_diagrams.md       ← Mermaid diagrams (module + per-phase)
 *
 *   /fsm/site/index.html            ← diagram-only mini-site (entry)
 *   /fsm/site/module_<id>.html      ← per-module HTML docs + diagrams
 *
 *   /fsm/wotr_fsm_state_ids.json    ← all state IDs (for VSCode highlighting, etc.)
 *
 * Includes validation for:
 *   - Missing state targets in transitions
 *   - Invalid guards
 *   - Missing prompts
 *
 * IMPORTANT: Do NOT edit generated files by hand.
 */

const fs = require('fs');
const path = require('path');
const YAML = require('yaml');

//
// Path Setup
//
const ROOT = path.resolve(__dirname, '..');
const FSM_DIR = path.join(ROOT, '.');
const INPUT_FILE = path.join(FSM_DIR, 'wotr_fsm.yml');
const SITE_DIR = path.join(FSM_DIR, 'site');

// Ensure /fsm exists
if (!fs.existsSync(FSM_DIR)) {
  console.error('ERROR: /fsm/ directory not found. Expected structure:');
  console.error('/fsm/wotr_fsm.yml');
  process.exit(1);
}

function ensureDir(dir) {
  if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });
}

//
// Helpers
//
function readYaml(filePath) {
  const raw = fs.readFileSync(filePath, 'utf8');
  return YAML.parse(raw);
}

function write(filePath, data) {
  fs.writeFileSync(filePath, data, 'utf8');
  console.log('✓', path.relative(ROOT, filePath));
}

/**
 * Flatten modules/phases into a convenient list
 *   [{ moduleId, moduleLabel, phaseId, phaseLabel, state }]
 */
function flattenStates(fsm) {
  const result = [];

  for (const mod of fsm.modules || []) {
    if (mod.type === 'phase_group') {
      for (const phase of mod.phases || []) {
        for (const state of phase.states || []) {
          result.push({
            moduleId: mod.id,
            moduleLabel: mod.label,
            phaseId: phase.id,
            phaseLabel: phase.label,
            state
          });
        }
      }
    }

    if (mod.type === 'subsystem') {
      for (const state of mod.states || []) {
        result.push({
          moduleId: mod.id,
          moduleLabel: mod.label,
          phaseId: null,
          phaseLabel: null,
          state
        });
      }
    }
  }

  return result;
}

function getAllStateIds(fsm) {
  const ids = new Set();
  const flat = flattenStates(fsm);
  for (const { state } of flat) {
    if (state.id) ids.add(state.id);
  }
  return ids;
}

/**
 * VALIDATION
 */
function validateFsm(fsm) {
  const errors = [];
  const warnings = [];

  const flat = flattenStates(fsm);
  const allStateIds = getAllStateIds(fsm);
  const allPrompts = fsm.prompts || {};

  function loc(item) {
    if (item.phaseLabel) {
      return `${item.moduleLabel} / ${item.phaseLabel} / ${item.state.id}`;
    }
    return `${item.moduleLabel} / ${item.state.id}`;
  }

  // Transitions + guards
  for (const item of flat) {
    const s = item.state;
    const trans = s.transitions || [];

    for (const t of trans) {
      const where = `transition ${t.event || '(no event)'} from state ${s.id}`;

      if (!t.target) {
        errors.push(
          `[MISSING_TARGET] ${where} in ${loc(item)} has no 'target' field.`
        );
      } else if (!allStateIds.has(t.target)) {
        errors.push(
          `[UNKNOWN_TARGET] ${where} in ${loc(
            item
          )} points to unknown state '${t.target}'.`
        );
      }

      if (t.guard !== null && t.guard !== undefined) {
        if (typeof t.guard !== 'string') {
          errors.push(
            `[BAD_GUARD] ${where} in ${loc(
              item
            )} has non-string guard (type ${typeof t.guard}).`
          );
        } else if (t.guard.trim() === '') {
          warnings.push(
            `[EMPTY_GUARD] ${where} in ${loc(
              item
            )} has an empty guard string (use null instead if no condition).`
          );
        }
      }
    }
  }

  // Prompt IDs
  for (const item of flat) {
    const s = item.state;
    const ui = s.ui || {};

    const checkPromptId = (promptId, fieldName) => {
      if (!promptId) return;
      if (!allPrompts[promptId]) {
        errors.push(
          `[MISSING_PROMPT] State ${s.id} (${loc(
            item
          )}) references ${fieldName}='${promptId}' which is not defined in fsm.prompts.`
        );
      }
    };

    checkPromptId(ui.on_enter_prompt_id, 'ui.on_enter_prompt_id');
    checkPromptId(ui.on_exit_prompt_id, 'ui.on_exit_prompt_id');
  }

  if (errors.length || warnings.length) {
    console.log('---');
    console.log('FSM VALIDATION REPORT');
    console.log('---');

    if (errors.length) {
      console.log('❌ Errors:');
      for (const e of errors) console.log('  -', e);
      console.log('');
    }

    if (warnings.length) {
      console.log('⚠️ Warnings:');
      for (const w of warnings) console.log('  -', w);
      console.log('');
    }
  }

  const ok = errors.length === 0;
  if (!ok) {
    console.error(
      `Validation failed with ${errors.length} error(s) and ${warnings.length} warning(s).`
    );
  } else if (warnings.length) {
    console.log(
      `Validation passed with ${warnings.length} warning(s). (Proceeding with build.)`
    );
  } else {
    console.log('Validation passed with no issues.');
  }

  return ok;
}

//
// 1) Runtime JSON Output
//
function generateRuntimeJson(fsm) {
  return JSON.stringify(fsm, null, 2);
}

//
// 2) Prompts Markdown
//
function generatePromptsMarkdown(fsm) {
  const lines = [];

  lines.push('# War of the Ring — FSM Prompts');
  lines.push('');
  lines.push('Generated from `wotr_fsm.yml`. Do not edit by hand.');
  lines.push('');

  if (fsm.prompts) {
    lines.push('## Global Prompt Catalog');
    lines.push('');
    for (const [id, text] of Object.entries(fsm.prompts)) {
      lines.push(`- \`${id}\`: ${text}`);
    }
    lines.push('');
  }

  const flat = flattenStates(fsm);
  const groups = {};

  for (const item of flat) {
    const key =
      item.phaseId != null ? `${item.moduleId}::${item.phaseId}` : item.moduleId;

    if (!groups[key]) {
      groups[key] = {
        moduleLabel: item.moduleLabel,
        phaseLabel: item.phaseLabel,
        items: []
      };
    }
    groups[key].items.push(item);
  }

  for (const [, group] of Object.entries(groups)) {
    lines.push('---');
    if (group.phaseLabel) {
      lines.push(`## ${group.phaseLabel}`);
      lines.push(`_Module: ${group.moduleLabel}_`);
    } else {
      lines.push(`## ${group.moduleLabel} Subsystem`);
    }
    lines.push('');

    for (const { state } of group.items) {
      lines.push(`### \`${state.id}\` — ${state.label || ''}`);
      lines.push('');
      if (state.description) {
        lines.push(state.description);
        lines.push('');
      }

      const ui = state.ui || {};
      if (ui.on_enter_prompt || ui.on_enter_prompt_id) {
        lines.push('**On Enter Prompt:**');
        lines.push('');
        if (ui.on_enter_prompt) {
          lines.push(`> ${ui.on_enter_prompt}`);
        } else if (ui.on_enter_prompt_id) {
          lines.push(`> \`${ui.on_enter_prompt_id}\``);
        }
        lines.push('');
      }
    }
  }

  return lines.join('\n');
}

//
// 3) State Table Markdown
//
function generateStateTableMarkdown(fsm) {
  const flat = flattenStates(fsm);
  const lines = [];

  lines.push('# War of the Ring — FSM State Table');
  lines.push('');
  lines.push('Generated from `wotr_fsm.yml`. Do not edit by hand.');
  lines.push('');

  lines.push('| State | Module / Phase | Description | Entry Actions | Transitions |');
  lines.push('| --- | --- | --- | --- | --- |');

  for (const item of flat) {
    const { state, moduleLabel, phaseLabel } = item;
    const location = phaseLabel
      ? `${moduleLabel} / ${phaseLabel}`
      : moduleLabel;

    const entryActions = (state.entry_actions || []).join('<br>');

    const transitions = (state.transitions || [])
      .map((t) => {
        const cond = t.guard ? ` [if ${t.guard}]` : '';
        return `\`${t.event}\` → \`${t.target}\`${cond}`;
      })
      .join('<br>');

    lines.push(
      `| \`${state.id}\` | ${location} | ${state.description || ''} | ${entryActions} | ${transitions} |`
    );
  }

  return lines.join('\n');
}

//
// 4) Mermaid Diagrams Markdown (modules + per-phase)
//
function generateMermaidDiagramsMarkdown(fsm) {
  const lines = [];

  lines.push('# War of the Ring — FSM Diagrams');
  lines.push('');
  lines.push('Generated from `wotr_fsm.yml`. Do not edit by hand.');
  lines.push('');

  function statesToMermaid(states) {
    const diag = [];
    diag.push('```mermaid');
    diag.push('stateDiagram-v2');

    for (const s of states) {
      const label = s.label ? `${s.id} : ${s.label}` : s.id;
      diag.push(`    state "${label}" as ${s.id}`);
    }

    for (const s of states) {
      for (const t of s.transitions || []) {
        const cond = t.guard ? ` : ${t.event} [${t.guard}]` : ` : ${t.event}`;
        diag.push(`    ${s.id} --> ${t.target}${cond}`);
      }
    }

    diag.push('```');
    return diag.join('\n');
  }

  function moduleDiagram(mod) {
    const states = [];

    if (mod.type === 'phase_group') {
      for (const phase of mod.phases || []) {
        for (const s of phase.states || []) states.push(s);
      }
    } else if (mod.type === 'subsystem') {
      for (const s of mod.states || []) states.push(s);
    }

    return statesToMermaid(states);
  }

  function phaseDiagram(phase) {
    const states = phase.states || [];
    return statesToMermaid(states);
  }

  for (const mod of fsm.modules || []) {
    lines.push('---');
    lines.push(`## Module: ${mod.label} (\`${mod.id}\`)`);
    lines.push('');
    lines.push('### Full Module Diagram');
    lines.push('');
    lines.push(moduleDiagram(mod));
    lines.push('');

    if (mod.type === 'phase_group') {
      for (const phase of mod.phases || []) {
        lines.push('#### Phase Diagram: ' + phase.label + ` (\`${phase.id}\`)`);
        lines.push('');
        lines.push(phaseDiagram(phase));
        lines.push('');
      }
    }
  }

  return lines.join('\n');
}

//
// 5) HTML mini-site (per-module + index)
//
function statesToMermaidString(states) {
  const lines = [];
  lines.push('stateDiagram-v2');

  for (const s of states) {
    const label = s.label ? `${s.id} : ${s.label}` : s.id;
    lines.push(`    state "${label}" as ${s.id}`);
  }

  for (const s of states) {
    for (const t of s.transitions || []) {
      const cond = t.guard ? ` : ${t.event} [${t.guard}]` : ` : ${t.event}`;
      lines.push(`    ${s.id} --> ${t.target}${cond}`);
    }
  }

  return lines.join('\n');
}

function generateModuleHtmlPages(fsm) {
  ensureDir(SITE_DIR);

  const modules = fsm.modules || [];

  // Index page
  const indexLines = [];
  indexLines.push('<!DOCTYPE html>');
  indexLines.push('<html lang="en">');
  indexLines.push('<head>');
  indexLines.push('  <meta charset="UTF-8" />');
  indexLines.push('  <title>War of the Ring FSM — Modules</title>');
  indexLines.push(
    '  <script src="https://unpkg.com/mermaid@10/dist/mermaid.min.js"></script>'
  );
  indexLines.push('  <script>mermaid.initialize({ startOnLoad: true });</script>');
  indexLines.push(
    '  <style>body{font-family:system-ui, sans-serif; padding:16px;} a{color:#0b7285;}</style>'
  );
  indexLines.push('</head>');
  indexLines.push('<body>');
  indexLines.push('<h1>War of the Ring — FSM Modules</h1>');
  indexLines.push('<p>Click a module to view its states and diagram.</p>');
  indexLines.push('<ul>');

  for (const mod of modules) {
    indexLines.push(
      `  <li><a href="module_${mod.id}.html">${mod.label} (<code>${mod.id}</code>)</a></li>`
    );
  }

  indexLines.push('</ul>');
  indexLines.push('</body>');
  indexLines.push('</html>');

  write(path.join(SITE_DIR, 'index.html'), indexLines.join('\n'));

  // Per-module pages
  for (const mod of modules) {
    const modStates = [];
    const phases = [];

    if (mod.type === 'phase_group') {
      for (const phase of mod.phases || []) {
        phases.push(phase);
        for (const s of phase.states || []) {
          modStates.push(s);
        }
      }
    } else if (mod.type === 'subsystem') {
      for (const s of mod.states || []) {
        modStates.push(s);
      }
    }

    const html = [];
    html.push('<!DOCTYPE html>');
    html.push('<html lang="en">');
    html.push('<head>');
    html.push('  <meta charset="UTF-8" />');
    html.push(`  <title>FSM Module — ${mod.label}</title>`);
    html.push(
      '  <script src="https://unpkg.com/mermaid@10/dist/mermaid.min.js"></script>'
    );
    html.push('  <script>mermaid.initialize({ startOnLoad: true });</script>');
    html.push(
      '  <style>body{font-family:system-ui, sans-serif; padding:16px;} table{border-collapse:collapse; width:100%; margin-top:16px;} th,td{border:1px solid #ccc; padding:4px 8px; font-size:13px;} code{font-size:12px;}</style>'
    );
    html.push('</head>');
    html.push('<body>');
    html.push(
      `<p><a href="index.html">&larr; Back to modules index</a></p>`
    );
    html.push(`<h1>${mod.label} <small><code>${mod.id}</code></small></h1>`);

    // Diagram
    html.push('<h2>Diagram</h2>');
    html.push('<div class="mermaid">');
    html.push(statesToMermaidString(modStates));
    html.push('</div>');

    // State table
    html.push('<h2>States</h2>');
    html.push(
      '<table><thead><tr><th>State</th><th>Description</th><th>Entry Actions</th><th>Transitions</th></tr></thead><tbody>'
    );

    for (const s of modStates) {
      const entryActions = (s.entry_actions || []).join('<br>');
      const transitions = (s.transitions || [])
        .map((t) => {
          const guardPart = t.guard ? ` [if ${t.guard}]` : '';
          const targetLink = `<a href="#${t.target}"><code>${t.target}</code></a>`;
          return `<code>${t.event}</code> → ${targetLink}${guardPart}`;
        })
        .join('<br>');

      html.push(
        `<tr id="${s.id}"><td><code>${s.id}</code><br>${s.label || ''}</td><td>${s.description || ''}</td><td>${entryActions}</td><td>${transitions}</td></tr>`
      );
    }

    html.push('</tbody></table>');

    // Optional per-phase mini-sections for phase_group
    if (mod.type === 'phase_group') {
      html.push('<h2>Per-Phase Diagrams</h2>');
      for (const phase of phases) {
        html.push(
          `<h3>${phase.label} <small><code>${phase.id}</code></small></h3>`
        );
        html.push('<div class="mermaid">');
        html.push(statesToMermaidString(phase.states || []));
        html.push('</div>');
      }
    }

    html.push('</body>');
    html.push('</html>');

    write(
      path.join(SITE_DIR, `module_${mod.id}.html`),
      html.join('\n')
    );
  }
}

//
// 6) VSCode helper: state IDs JSON
//
function generateVscodeStateIds(fsm) {
  const ids = Array.from(getAllStateIds(fsm)).sort();
  return JSON.stringify({ states: ids }, null, 2);
}

//
// MAIN
//
console.log('Building FSM from /fsm/wotr_fsm.yml…');

const fsm = readYaml(INPUT_FILE);

// Validate before generating anything
const ok = validateFsm(fsm);
if (!ok) {
  console.error('Aborting build due to FSM validation errors.');
  process.exit(1);
}

// Ensure subdirs
ensureDir(SITE_DIR);

// 1) Runtime FSM
write(path.join(FSM_DIR, 'wotr_fsm.json'), generateRuntimeJson(fsm));

// 2) Prompts doc
write(
  path.join(FSM_DIR, 'wotr_fsm_prompts.md'),
  generatePromptsMarkdown(fsm)
);

// 3) State table
write(
  path.join(FSM_DIR, 'wotr_fsm_table.md'),
  generateStateTableMarkdown(fsm)
);

// 4) Diagrams (module + per-phase)
write(
  path.join(FSM_DIR, 'wotr_fsm_diagrams.md'),
  generateMermaidDiagramsMarkdown(fsm)
);

// 5) HTML mini-site (per-module + index)
generateModuleHtmlPages(fsm);

// 6) VSCode state IDs helper
write(
  path.join(FSM_DIR, 'wotr_fsm_state_ids.json'),
  generateVscodeStateIds(fsm)
);

console.log('FSM build complete.');
