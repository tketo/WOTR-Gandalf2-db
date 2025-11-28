# FSM Server

This folder contains the Finite State Machine (FSM) server for War of the Ring AI orchestration.

## Quick Start

```bash
# Install dependencies
npm install

# Start server
npm start

# Server listens on TCP port 8080
```

## What's Here

```
fsm/
├── server/
│   ├── index.js           # Main FSM server
│   ├── fsm-schema.json    # State machine definition
│   ├── package.json       # Node.js dependencies
│   └── README.md          # This file
```

## How It Works

1. FSM server starts and loads `fsm-schema.json`
2. Listens for Java game client connection on TCP port 8080
3. Orchestrates game flow by sending commands to client
4. Receives events from client and transitions FSM state
5. Queries `wotr_game.db` to make AI decisions

## Documentation

**For full details, see:**
- **[/ARCHITECTURE.md](../ARCHITECTURE.md)** - FSM design and rationale
- **[/IMPLEMENTATION.md](../IMPLEMENTATION.md)** - How to run and modify FSM
- **[/docs/FSM_PROTOCOL.md](../docs/FSM_PROTOCOL.md)** - TCP protocol specification

## Development

```bash
# Run tests
npm test

# Debug mode (verbose logging)
DEBUG=true npm start

# Install specific Node version (if needed)
nvm use 16
```

---

**Maintained By:** Development Team
