const marker = "CTJS_LEGACY_SMOKE_LOADED";

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

const preItemRenderTrigger = register("preItemRender", () => {}).unregister();
if (typeof preItemRenderTrigger.register !== "function") {
    throw new Error("Legacy preItemRender trigger is not exported");
}

const renderItemIntoGuiTrigger = register("renderItemIntoGui", () => {}).unregister();
if (typeof renderItemIntoGuiTrigger.register !== "function") {
    throw new Error("Legacy renderItemIntoGui trigger is not exported");
}

const renderItemOverlayIntoGuiTrigger = register("renderItemOverlayIntoGui", () => {}).unregister();
if (typeof renderItemOverlayIntoGuiTrigger.register !== "function") {
    throw new Error("Legacy renderItemOverlayIntoGui trigger is not exported");
}

for (const triggerName of ["chatComponentClicked", "chatComponentHovered"]) {
    const trigger = register(triggerName, () => {}).unregister();
    if (typeof trigger.register !== "function") {
        throw new Error(`Legacy ${triggerName} trigger is not exported`);
    }
}

const pickupItemTrigger = register("pickupItem", () => {}).unregister();
if (typeof pickupItemTrigger.register !== "function") {
    throw new Error("Legacy pickupItem trigger is not exported");
}

for (const triggerName of ["noteBlockPlay", "noteBlockChange"]) {
    const trigger = register(triggerName, () => {}).unregister();
    if (typeof trigger.register !== "function") {
        throw new Error(`Legacy ${triggerName} trigger is not exported`);
    }
}

for (const triggerName of [
    "renderCrosshair", "renderDebug", "renderBossHealth", "renderHealth",
    "renderArmor", "renderFood", "renderMountHealth", "renderHotbar",
    "renderAir", "renderPortal", "renderChat", "renderScoreboard", "renderTitle"
]) {
    const trigger = register(triggerName, () => {}).unregister();
    if (typeof trigger.register !== "function") {
        throw new Error(`Legacy ${triggerName} trigger is not exported`);
    }
}

const guiBackgroundTrigger = register("guiDrawBackground", () => {}).unregister();
if (typeof guiBackgroundTrigger.register !== "function") {
    throw new Error("Legacy guiDrawBackground trigger is not exported");
}

const renderHandTrigger = register("renderHand", () => {}).unregister();
if (typeof renderHandTrigger.register !== "function") {
    throw new Error("Legacy renderHand trigger is not exported");
}

const renderHelmetTrigger = register("renderHelmet", () => {}).unregister();
if (typeof renderHelmetTrigger.register !== "function") {
    throw new Error("Legacy renderHelmet trigger is not exported");
}

for (const triggerName of ["renderExperience", "renderJumpBar"]) {
    const trigger = register(triggerName, () => {}).unregister();
    if (typeof trigger.register !== "function") {
        throw new Error(`Legacy ${triggerName} trigger is not exported`);
    }
}

for (const triggerName of ["playerJoined", "playerLeft"]) {
    const trigger = register(triggerName, () => {}).unregister();
    if (typeof trigger.register !== "function") {
        throw new Error(`Legacy ${triggerName} trigger is not exported`);
    }
}

register("command", () => {
    ChatLib.chat("&aLegacy module smoke test passed");
}).setName("ctlegacytest");

console.log(marker);
