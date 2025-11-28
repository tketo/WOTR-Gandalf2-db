# WOTR-GANDALF

```
┌─────────────────────────────────────────┐
│     War of the Ring Digital Edition    │
│                                         │
│  ┌──────┐  ┌──────┐  ┌──────┐         │
│  │  UI  │◄─┤ Game │◄─┤  DB  │         │
│  └──────┘  └──────┘  └──────┘         │
│       ▲         ▲                      │
│       └─────────┴─── FSM Server       │
└─────────────────────────────────────────┘
```

A Java-based implementation of **War of the Ring** board game with complete rules enforcement, database-driven game state, and FSM-based AI orchestration.

**Status:** Phase 7 Complete - 96/96 Event Cards Implemented ✅  
**Progress:** ~95% Feature Complete

## Quick Start

```bash
# Build
mvn clean package

# Run
java -jar target/WOTR-GANDALF.jar

# Or use convenience script (Windows)
run_game.bat

# With FSM Server (optional)
cd fsm/server && npm install && npm start
```

## ✨ What's Implemented

**Core Systems:**
- Complete Middle-earth map (104 regions, 209 connections)
- All 96 event cards with full effects (100% coverage)
- Combat, Hunt, Fellowship, Political Track systems
- Save/Load with SQLite database persistence
- 2-player support with FSM server for AI
- Turn management and action dice system

**Architecture:**
- Database as Board (SQLite as single source of truth)
- 1:1 piece mapping (each piece = one database row)
- State inference (derive state from piece positions)
- Automatic sync (pieces sync to database on movement)

## 🚀 Quick Start

### Prerequisites
- Java 1.8+
- Maven 3.6+

### Build & Run
```bash
# Compile
mvn compile

# Run tests
mvn test

# Run card system test
java -cp "target/classes;path/to/gson.jar" wotr.cards.CardLoaderTest

# Run combat system test
java -cp "target/classes" wotr.rules.CombatSystemTest

# Run Phase 5 demo (Turn & Phase UI)
java -cp "target/classes" wotr.ui.TurnPhaseDemo
```

### Database Setup
Database auto-initializes on first run with **V8 schema**:
- Creates `wotr_game.db` using Flyway migrations
- Direct piece mapping (no JSON parsing)
- Loads all scenarios (Base, LOME, WOME)
- See **[DATABASE.md](DATABASE.md)** for schema details

## 📊 Project Stats

- **241 Java files** (~15,800 lines)
- **96 event cards** (100% implemented)
- **104 map regions** (complete Middle-earth)
- **44 unit tests** (95.5% passing)

## 📖 Documentation

### Core Documents
1. **[IMPLEMENTATION.md](IMPLEMENTATION.md)** - How to build, run, extend, and modify ⭐
2. **[ARCHITECTURE.md](ARCHITECTURE.md)** - System design, modules, and rationale ⭐
3. **[README.md](README.md)** - This file (entry point)
4. **[CHANGELOG.md](CHANGELOG.md)** - Version history and changes
5. **[ROADMAP.md](ROADMAP.md)** - Future vision and priorities

### Game Reference
- **[docs/Official-Rules-Guide.md](docs/Official-Rules-Guide.md)** - War of the Ring rules

## 🛠️ Tech Stack

Java 1.8 • SQLite • Maven • Swing • Node.js (FSM)

---

**Version:** 4.1 (Phase 7 Complete + Bug Fixes)  
**Last Updated:** November 20, 2025
