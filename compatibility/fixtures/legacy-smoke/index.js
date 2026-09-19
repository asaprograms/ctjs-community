const marker = "CTJS_LEGACY_SMOKE_LOADED";

console.log(marker);

const packedColor = Renderer.color(12, 34, 56, 78);
if (typeof packedColor !== "number" || packedColor !== Renderer.getColor(12, 34, 56, 78)) {
    throw new Error("Legacy Renderer.color did not return a packed color");
}

if (typeof Client.getChatGUI !== "function") {
    throw new Error("Client.getChatGUI legacy alias is not exported");
}

const packetTrigger = register("packetReceived", () => {}).unregister();
if (typeof packetTrigger.setPacketClass !== "function" || typeof packetTrigger.setPacketClasses !== "function") {
    throw new Error("Legacy packet filter aliases are not exported");
}

const renderSlotTrigger = register("renderSlot", () => {}).unregister();
if (typeof renderSlotTrigger.register !== "function") {
    throw new Error("Legacy renderSlot trigger is not exported");
}

const mouseReleaseTrigger = register("guiMouseRelease", () => {}).unregister();
if (typeof mouseReleaseTrigger.register !== "function") {
    throw new Error("Legacy guiMouseRelease trigger is not exported");
}

for (const triggerName of ["attackEntity", "hitBlock", "blockBreak"]) {
    const trigger = register(triggerName, () => {}).unregister();
    if (typeof trigger.register !== "function") {
        throw new Error(`Legacy ${triggerName} trigger is not exported`);
    }
}

const screenshotTrigger = register("screenshotTaken", () => {}).unregister();
if (typeof screenshotTrigger.register !== "function") {
    throw new Error("Legacy screenshotTaken trigger is not exported");
}

for (const triggerName of ["renderTileEntity", "postRenderEntity", "postRenderTileEntity"]) {
    const trigger = register(triggerName, () => {}).unregister();
    if (typeof trigger.setFilteredClasses !== "function") {
        throw new Error(`Legacy ${triggerName} class filter is not exported`);
    }
}

const slotHighlightTrigger = register("renderSlotHighlight", () => {}).unregister();
if (typeof slotHighlightTrigger.register !== "function") {
    throw new Error("Legacy renderSlotHighlight trigger is not exported");
}

register("command", () => {
    ChatLib.chat("&aLegacy module smoke test passed");
}).setName("ctlegacytest");
