#!/usr/bin/env python3
"""
Convert bits array JSON exports to SQL INSERT statements for scenario_setup table.
Uses NEW V8 schema with piece_class, card fields, etc.

Usage:
    python import_json_to_sql.py

Output:
    scenario_imports.sql - SQL file with all INSERT statements
"""

import json
import os
from pathlib import Path

# Variant to scenario name mapping
VARIANT_TO_SCENARIO = {
    "base2": "base",
    "base2[T]": "base_t",
    "expansion2[L]": "lome",
    "expansion2[LT]": "lome_t",
    "base[WT]": "wome_t",
    "expansion2[WLT]": "wome_lome_t"
}

def get_faction(class_name):
    """Determine faction from Java class name."""
    simple_name = class_name.split('.')[-1]
    
    if any(x in simple_name for x in ['Free', 'FP', 'Gondor', 'Rohan', 'Elven', 'Dwarven', 'North']):
        return 'free_peoples'
    elif any(x in simple_name for x in ['Shadow', 'SA', 'Sauron', 'Isengard', 'Southron', 'WitchKing']):
        return 'shadow'
    
    return 'neutral'

def get_nation_from_class(class_name):
    """Extract nation from unit class name."""
    simple_name = class_name.split('.')[-1]
    
    if 'Gondor' in simple_name:
        return 'gondor'
    elif 'Rohan' in simple_name:
        return 'rohan'
    elif 'Elven' in simple_name or 'Elrond' in simple_name:
        return 'elves'
    elif 'Dwarven' in simple_name:
        return 'dwarves'
    elif 'North' in simple_name:
        return 'north'
    elif 'Sauron' in simple_name:
        return 'sauron'
    elif 'Isengard' in simple_name:
        return 'isengard'
    elif 'Southron' in simple_name:
        return 'southrons_easterlings'
    
    return None

def get_unit_type_from_class(class_name):
    """Extract unit type from class name."""
    simple_name = class_name.split('.')[-1]
    
    if 'Regular' in simple_name:
        return 'regular'
    elif 'Elite' in simple_name:
        return 'elite'
    elif 'Leader' in simple_name or any(x in simple_name for x in ['Gandalf', 'Aragorn', 'Strider', 'Legolas', 'Gimli', 'Boromir', 'Merry', 'Pippin', 'Galadriel', 'WitchKing', 'Smeagol', 'Saruman', 'Mouth', 'Elrond']):
        return 'leader'
    elif 'Nazgul' in simple_name:
        return 'nazgul'
    
    return None

def escape_sql_string(s):
    """Escape single quotes for SQL."""
    if s is None or s == '':
        return 'NULL'
    return "'" + str(s).replace("'", "''") + "'"

def process_json_file(json_path):
    """Process a single JSON file and return SQL INSERT statements using V8 schema."""
    with open(json_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    variant = data['variant']
    scenario = VARIANT_TO_SCENARIO.get(variant, variant)
    pieces = data['pieces']
    
    inserts = []
    inserts.append(f"\n-- Scenario: {scenario} (variant: {variant})")
    inserts.append(f"-- Total pieces: {data['piece_count']}\n")
    
    for piece in pieces:
        if piece['type'] is None:
            continue  # Skip null pieces
        
        # Core fields
        piece_id = piece['index']
        piece_class = piece['type']  # Simple class name (e.g., "FreeStrategyCard")
        area_id = piece['area_index']
        area_name = piece.get('area_name', '')
        faction = get_faction(piece['class'])
        
        # Card-specific fields
        small_image = piece.get('small_image')
        big_image = piece.get('big_image')
        card_name = piece.get('card_name')
        
        # BattleCard-specific fields
        small_back_image = piece.get('small_back_image')
        big_back_image = piece.get('big_back_image')
        card_type = piece.get('card_type')
        
        # Unit-specific fields
        nation = get_nation_from_class(piece['class'])
        unit_type = get_unit_type_from_class(piece['class'])
        
        # Build INSERT statement with V8 schema
        cols = ['piece_id', 'scenario_id', 'piece_class', 'initial_area_id', 'initial_area_name', 'faction']
        vals = [str(piece_id), escape_sql_string(scenario), escape_sql_string(piece_class), 
                str(area_id), escape_sql_string(area_name), escape_sql_string(faction)]
        
        # Add card fields
        cols.extend(['small_image', 'big_image', 'card_name'])
        vals.extend([escape_sql_string(small_image), escape_sql_string(big_image), escape_sql_string(card_name)])
        
        # Add BattleCard fields
        cols.extend(['small_back_image', 'big_back_image', 'card_type'])
        vals.extend([escape_sql_string(small_back_image), escape_sql_string(big_back_image), escape_sql_string(card_type)])
        
        # Add unit fields
        cols.extend(['nation', 'unit_type'])
        vals.extend([escape_sql_string(nation), escape_sql_string(unit_type)])
        
        insert = f"INSERT INTO scenario_setup ({', '.join(cols)}) VALUES ({', '.join(vals)});"
        inserts.append(insert)
    
    return inserts

def main():
    # Define input/output paths
    data_dir = Path(__file__).parent.parent / 'data'
    output_file = Path(__file__).parent.parent / 'src' / 'main' / 'resources' / 'database' / 'imports' / 'scenario_imports.sql'
    
    # JSON files to process
    json_files = [
        'bits_array_base2_bitinit.json',
        'bits_array_base2[T]_bitinit.json',
        'bits_array_expansion2[L]_bitinit.json',
        'bits_array_expansion2[LT]_bitinit.json',
        'bits_array_base[WT]_bitinit.json',
        'bits_array_expansion2[WLT]_bitinit.json'
    ]
    
    # Create output directory if needed
    output_file.parent.mkdir(parents=True, exist_ok=True)
    
    all_inserts = []
    all_inserts.append("-- Generated SQL imports from bits array JSON exports")
    all_inserts.append("-- Generated by: import_json_to_sql.py")
    all_inserts.append("-- DO NOT EDIT MANUALLY - regenerate from JSON if changes needed\n")
    all_inserts.append("-- Clear existing data first")
    all_inserts.append("DELETE FROM scenario_setup;\n")
    
    # Process each JSON file
    for json_file in json_files:
        json_path = data_dir / json_file
        if not json_path.exists():
            print(f"Warning: {json_path} not found, skipping...")
            continue
        
        print(f"Processing {json_file}...")
        inserts = process_json_file(json_path)
        all_inserts.extend(inserts)
        all_inserts.append("")  # Blank line between scenarios
    
    # Write to output file
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write('\n'.join(all_inserts))
    
    print(f"\n✓ SQL file generated: {output_file}")
    print(f"✓ Total lines: {len(all_inserts)}")
    print("\nNext steps:")
    print("1. Review the generated SQL file")
    print("2. Run: sqlite3 wotr_game.db < src/main/resources/database/imports/scenario_imports.sql")

if __name__ == '__main__':
    main()
