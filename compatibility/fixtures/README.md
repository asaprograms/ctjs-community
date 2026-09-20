# Compatibility fixtures

These small modules provide reproducible runtime checks for legacy ChatTriggers behavior.

`runClient` stages `legacy-smoke` into the development modules directory automatically. Confirm
that `CTJS_LEGACY_SMOKE_LOADED`, `CTJS_RENDERER_SMOKE_DRAWN`, and `CTJS_DISPLAY_SMOKE_RENDERED` appear on standard output. The renderer marker is emitted only after a legacy quad is submitted from a render trigger. The display marker is emitted on the frame after an automatically registered legacy display has rendered. The `/ctlegacytest` command should
then print a green confirmation message in chat. CI performs the same startup check in a virtual
display and preserves the complete client log as an artifact.

The fixture uses the metadata shape, global APIs, command trigger, and method chaining expected
by older modules. Passing it is a baseline smoke test, not a claim that every legacy module works.
