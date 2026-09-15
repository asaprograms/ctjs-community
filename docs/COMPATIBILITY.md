# Module compatibility

## Compatibility levels

| Level | Meaning |
| --- | --- |
| Verified | Covered by an automated fixture on every maintained branch |
| Expected | Uses supported public APIs, but does not yet have a dedicated fixture |
| Migration required | Uses a known platform-specific API that cannot be preserved exactly |
| Unsupported | Depends on unsafe or removed runtime behavior |

Compatibility is measured by behavior, not only by whether a module loads.

## Primary compatibility targets

- Trigger registration and cancellation
- Chat criteria and chat component handling
- Client commands and command arguments
- World, player, entity, inventory, item, scoreboard, and tab-list wrappers
- 2D and 3D rendering helpers
- GUI, display, image, sound, keybind, and settings APIs
- Module metadata, imports, dependency ordering, and live reload
- File and web request helpers
- Dynamic Mixins supported by the modern runtime

## Known migration boundaries

Modules require changes when they directly depend on any of the following:

- `net.minecraftforge` classes or Forge events
- Minecraft 1.8.9 class, field, or method names
- MCP or obfuscated names
- LWJGL 2 input or rendering APIs
- Forge coremods or raw ASM transformers
- Rendering behavior that bypasses supported Blaze3D abstractions

Where practical, the project provides a compatibility wrapper with the old ChatTriggers shape. Where a faithful wrapper would be misleading or unsafe, the migration is documented instead.

## Evidence

The compatibility manifest records each fixture, upstream module version, APIs exercised, expected result, and the last branches on which it passed. A compatibility claim should not be marked Verified without an executable fixture.
