const marker = "CTJS_LEGACY_SMOKE_LOADED";

console.log(marker);

register("command", () => {
    ChatLib.chat("&aLegacy module smoke test passed");
}).setName("ctlegacytest");
