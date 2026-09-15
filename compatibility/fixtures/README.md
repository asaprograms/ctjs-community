# Compatibility fixtures

These small modules provide reproducible runtime checks for legacy ChatTriggers behavior.

To run `legacy-smoke`, copy its directory to
`run/config/ChatTriggers/modules/LegacySmokeTest`, start the development client, and confirm
that `CTJS_LEGACY_SMOKE_LOADED` appears on standard output. The `/ctlegacytest` command should
then print a green confirmation message in chat.

The fixture uses the metadata shape, global APIs, command trigger, and method chaining expected
by older modules. Passing it is a baseline smoke test, not a claim that every legacy module works.
