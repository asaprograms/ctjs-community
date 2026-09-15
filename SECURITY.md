# Security policy

## Reporting a vulnerability

Do not publish a working exploit in a public issue. Send a private report through the repository security advisory form. Include the affected version, impact, reproduction steps, and any proposed mitigation.

Reports about malicious registry modules should include the module name, release identifier, and the behavior observed. Registry moderators can quarantine a release independently of the mod release cycle.

## Module threat model

ChatTriggers modules execute JavaScript inside the Minecraft client and may call permitted Java APIs. A module can read local game data, make network requests, and affect the running client. Automated scanning is intended to identify suspicious capabilities and known malicious patterns. It is not a sandbox and it is not a guarantee of safety.

Registry publication requires both an automated scan and human approval. Reviewers inspect source files, declared dependencies, network destinations, binary payloads, obfuscation, dynamic code execution, and changes from the previous release.

## Supported versions

Security fixes are provided for the latest release on the `main` and `26.1.2` branches. Older builds may receive fixes when the same patch applies cleanly, but are not guaranteed support.
