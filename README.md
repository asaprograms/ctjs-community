# ChatTriggers Community Edition

ChatTriggers Community Edition is a maintained Fabric implementation of the ChatTriggers scripting platform for Minecraft 26.1.2 and 26.2. It keeps the familiar JavaScript API, live module reloading, triggers, wrappers, and dynamic Mixins while tracking current Minecraft and Fabric releases.

This is an independent community continuation. It is not operated or endorsed by the original ChatTriggers team.

## Supported versions

| Branch | Minecraft | Java | Status |
| --- | --- | --- | --- |
| `main` | 26.2 | 26 or newer | Active |
| `26.1.2` | 26.1.2 | 25 or newer | Maintained |

Fabric Loader, Fabric API, and Fabric Language Kotlin are required. Release artifacts are built by GitHub Actions from the tagged source.

## Module compatibility

Modules written against the public ChatTriggers 2.x API are a primary compatibility target. Most modules that use triggers, commands, chat helpers, rendering helpers, settings, and module imports can be brought forward without structural changes.

Code that imports Forge classes, accesses Minecraft 1.8.9 internals, uses legacy LWJGL APIs, references obfuscated names, or performs raw ASM transformation is not automatically portable. Compatibility claims are tracked against executable fixtures and published in [the compatibility documentation](docs/COMPATIBILITY.md).

## Building

The project uses the included Gradle wrapper.

```text
./gradlew build
```

The distributable jar is written to `build/libs`. Development on the 26.2 branch requires JDK 26 or newer.

## Security

Modules run with substantial access to the game and the local Java process. Only install modules from authors you trust. The ctjs.net registry combines automated static analysis with mandatory human review, but review cannot prove that software is harmless. See [SECURITY.md](SECURITY.md) for reporting instructions and the module threat model.

## Contributing

Bug reports, compatibility fixtures, documentation corrections, and focused pull requests are welcome. Read [CONTRIBUTING.md](CONTRIBUTING.md) before submitting a change.

## Credits and lineage

ChatTriggers was created by Ecolsson, FalseHonesty, and kerbybit, with substantial work from Squagward, Debuggings, DJtheRedstoner, srockw, mattco98, and many other contributors. The modern Fabric implementation was developed in the official [ChatTriggers/ctjs](https://github.com/ChatTriggers/ctjs) project. Later version ports by Synnerz, DocilElm, and contributors provided the starting point for the 26.1.2 and 26.2 platform work.

The complete source lineage and licensing information are recorded in [NOTICE.md](NOTICE.md). Please support and credit the original maintainers when sharing this project.

## License

This project is distributed under the MIT License. See [LICENSE](LICENSE).
