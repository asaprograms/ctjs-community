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

### APIs that cannot be represented faithfully

The following legacy calls remain unavailable rather than returning invented data or silently doing nothing:

- `World.getSeed()` and `World.getType()`: multiplayer clients are not sent the world seed or server generator type.
- `Entity.dropItem()` and `Entity.setIsOutsideBorder()`: these mutate server-owned entity state.
- `Entity.isAirborne()`: the old state flag no longer has a stable client-side equivalent.
- `Settings.video.get3dAnaglyph()`: Minecraft removed the setting.
- Forge, MCP, LWJGL 2, coremod, and raw ASM entry points listed above.

`BlockType.getHarvestLevel()` is still under review. Modern Minecraft represents tool suitability through tags and block state, so a single context-free legacy value would not be reliable. `Item.canDestroy(Block)` is supported because the placed block supplies the world context required by the modern adventure-mode predicate.

The project does restore compatible public shapes when modern Minecraft exposes equivalent behavior. This includes legacy block metadata indexing, redstone queries, inventory actions, item block predicates, moon phase, boolean graphics settings, display lines, draw-mode helpers, sound controls, keybind callback lifecycles, and common wrapper aliases. The manifest is the authoritative list of verified surfaces.

## Evidence

The compatibility manifest records each fixture, upstream module version, APIs exercised, expected result, and the test that enforces it. Both maintained branches run unit tests and a real Minecraft client smoke fixture in CI. A compatibility claim should not be marked Verified without executable evidence.
