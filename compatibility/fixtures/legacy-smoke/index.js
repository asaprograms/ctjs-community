const marker = "CTJS_LEGACY_SMOKE_LOADED";

const packedColor = Renderer.color(12, 34, 56, 78);
if (typeof packedColor !== "number" || packedColor !== Renderer.getColor(12, 34, 56, 78)) {
    throw new Error("Legacy Renderer.color did not return a packed color");
}

if (typeof Client.getChatGUI !== "function") {
    throw new Error("Client.getChatGUI legacy alias is not exported");
}
if (typeof ChatLib.test !== "function" || typeof ChatLib.clearChat !== "function") {
    throw new Error("Legacy ChatLib diagnostic or targeted clear API is not exported");
}
ChatLib.clearChat(2147483647);
if (typeof Player.asEntity !== "function" || Player.asEntity() !== null) {
    throw new Error("Legacy Player.asEntity unloaded-player contract failed");
}
for (const method of ["playSound", "playRecord", "stopAllSounds"]) {
    if (typeof World[method] !== "function") {
        throw new Error(`Legacy World.${method} is not exported`);
    }
}
if (typeof World.getMoonPhase !== "function" || World.getMoonPhase() !== -1) {
    throw new Error("Legacy World.getMoonPhase unloaded-world contract failed");
}

for (const method of ["getGraphics", "setGraphics"]) {
    if (typeof Settings.video[method] !== "function") {
        throw new Error(`Legacy Settings.video.${method} is not exported`);
    }
}
const legacyFancyGraphics = Settings.video.getGraphics();
Settings.video.setGraphics(legacyFancyGraphics);
for (const method of ["setClouds", "setParticles"]) {
    if (typeof Settings.video[method] !== "function") {
        throw new Error(`Legacy Settings.video.${method} is not exported`);
    }
}
if (typeof Settings.chat.setVisibility !== "function") {
    throw new Error("Legacy Settings.chat.setVisibility is not exported");
}
World.playSound("minecraft:ui.button.click", 0, 1);
World.playRecord(null, 0, 0, 0);
World.stopAllSounds();

const legacyGui = new Gui();
for (const method of [
    "close", "isControlDown", "isShiftDown", "isAltDown", "getButton",
    "drawString", "drawCreativeTabHoveringString", "drawHoveringString",
]) {
    if (typeof legacyGui[method] !== "function") {
        throw new Error(`Legacy Gui.${method} is not exported`);
    }
}

if (typeof KeyBind.removeKeyBind !== "function" || typeof KeyBind.clearKeyBinds !== "function") {
    throw new Error("Legacy KeyBind cleanup methods are not exported");
}
const legacyKeyBind = new KeyBind("Legacy smoke key", 0);
for (const callbackTrigger of [
    legacyKeyBind.registerKeyPress(() => {}),
    legacyKeyBind.registerKeyRelease(() => {}),
    legacyKeyBind.registerKeyDown(() => {}),
]) {
    if (typeof callbackTrigger.unregister !== "function") {
        throw new Error("Legacy KeyBind callback registration did not return a trigger");
    }
}
KeyBind.removeKeyBind(legacyKeyBind);

const legacyBlockType = new BlockType("minecraft:stone");
if (typeof legacyBlockType.getDefaultMetadata !== "function" ||
    typeof legacyBlockType.getUnlocalizedName !== "function" ||
    legacyBlockType.getDefaultMetadata() < 0 ||
    legacyBlockType.getUnlocalizedName() !== "block.minecraft.stone") {
    throw new Error("Legacy BlockType state API failed");
}
const legacyBlock = legacyBlockType.withBlockPos(new BlockPos(0, 0, 0));
for (const method of ["getMetadata", "isPowered", "getRedstoneStrength"]) {
    if (typeof legacyBlock[method] !== "function") {
        throw new Error(`Legacy Block.${method} is not exported`);
    }
}
for (const method of [
    "setBlockPos", "setFace", "getID", "getRegistryName", "getUnlocalizedName",
    "getName", "getLightValue", "getDefaultState", "getDefaultMetadata",
    "canProvidePower", "isTranslucent",
]) {
    if (typeof legacyBlock[method] !== "function") {
        throw new Error(`Legacy Block.${method} is not exported`);
    }
}
if (legacyBlock.setBlockPos(new BlockPos(1, 2, 3)) !== legacyBlock ||
    legacyBlock.setFace(BlockFace.NORTH) !== legacyBlock ||
    legacyBlock.getX() !== 1 || legacyBlock.getY() !== 2 || legacyBlock.getZ() !== 3 ||
    legacyBlock.getRegistryName() !== "minecraft:stone") {
    throw new Error("Legacy mutable Block wrapper contract failed");
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

const legacyText = new TextComponent("&aFirst");
if (legacyText.getText() !== "&aFirst" || legacyText.getUnformattedText() !== "First") {
    throw new Error("Legacy TextComponent initial text contract failed");
}

if (legacyText.setText("&bSecond") !== legacyText || legacyText.getUnformattedText() !== "Second") {
    throw new Error("Legacy TextComponent.setText did not mutate and return the component");
}

legacyText.setFormatted(false);
if (legacyText.isFormatted() !== false || legacyText.getUnformattedText() !== "&bSecond") {
    throw new Error("Legacy TextComponent formatting toggle failed");
}

legacyText.setFormatted(true)
    .setClick("run_command", "/ctlegacytest")
    .setHover("show_text", "&eDetails");
if (legacyText.getUnformattedText() !== "Second" ||
    legacyText.getClickAction() !== "run_command" ||
    legacyText.getClickValue() !== "/ctlegacytest" ||
    legacyText.getHoverAction() !== "show_text" ||
    legacyText.getHoverValue() !== "&eDetails") {
    throw new Error("Legacy TextComponent click or hover contract failed");
}

const copiedText = legacyText.withChatLineId(12);
copiedText.setText("Changed");
if (legacyText.getUnformattedText() !== "Second" || copiedText.getUnformattedText() !== "Changed") {
    throw new Error("TextComponent copy isolation failed");
}

const ChatTriggerEvent = Java.type("com.chattriggers.ctjs.api.triggers.ChatTrigger$Event");
const chatEvent = new ChatTriggerEvent(new TextComponent("&aLegacy chat"));
const unformattedChatMessage = String(ChatLib.getChatMessage(chatEvent));
const formattedChatMessage = String(ChatLib.getChatMessage(chatEvent, true));
const expectedFormattedChatMessage = String(chatEvent.message.formattedText).replace(/\u00a7/g, "&");
if (unformattedChatMessage !== "Legacy chat" || formattedChatMessage !== expectedFormattedChatMessage) {
    throw new Error(`Legacy ChatLib.getChatMessage contract failed: ${unformattedChatMessage} / ${formattedChatMessage}`);
}

register("command", () => {
    ChatLib.chat("&aLegacy module smoke test passed");
}).setName("ctlegacytest");

let rendererSmokeDrawn = false;
let displayFrameCount = 0;
const legacyDisplay = new Display()
    .setLine(0, "&aLegacy display smoke")
    .setRenderLoc(6, 6)
    .setRegisterType("post gui render");
const legacyDisplayLine = new DisplayLine("initial").setText("legacy line").setTextColor(0xffffffff);
if (legacyDisplayLine.getText().getString() !== "legacy line" || legacyDisplayLine.getTextWidth() <= 0) {
    throw new Error("Legacy DisplayLine text contract failed");
}
for (const callbackTrigger of [
    legacyDisplayLine.registerClicked(() => {}),
    legacyDisplayLine.registerHovered(() => {}),
    legacyDisplayLine.registerMouseLeave(() => {}),
    legacyDisplayLine.registerDragged(() => {}),
]) {
    if (typeof callbackTrigger.unregister !== "function") {
        throw new Error("Legacy DisplayLine callback registration did not return a trigger");
    }
}
legacyDisplayLine.unregisterClicked();
legacyDisplayLine.unregisterHovered();
legacyDisplayLine.unregisterMouseLeave();
legacyDisplayLine.unregisterDragged();
legacyDisplay.addLine(0, legacyDisplayLine);
register("guiRender", () => {
    displayFrameCount++;
    if (displayFrameCount === 1) {
        Player.draw(0, 0, false);
        Renderer.drawPlayer(null, 0, 0, false);
    }
    if (displayFrameCount === 2) {
        console.log("CTJS_DISPLAY_SMOKE_RENDERED");
    }

    if (rendererSmokeDrawn) return;

    Renderer.begin(Renderer.DrawMode.QUADS, Renderer.VertexFormat.POSITION_COLOR);
    Renderer.pos(2, 2).color(255, 255, 255, 255);
    Renderer.pos(2, 4).color(255, 255, 255, 255);
    Renderer.pos(4, 4).color(255, 255, 255, 255);
    Renderer.pos(4, 2).color(255, 255, 255, 255);
    Renderer.draw();

    Renderer.setDrawMode(7);
    if (Renderer.getDrawMode() !== 7) {
        throw new Error("Legacy Renderer draw mode state failed");
    }
    Renderer.retainTransforms(true);
    Renderer.translate(0, 0);
    Renderer.drawShape(
        Renderer.color(255, 255, 255, 255),
        [6, 6], [6, 8], [8, 8], [8, 6]
    );
    Renderer.retainTransforms(false);
    if (Renderer.getDrawMode() !== null) {
        throw new Error("Legacy Renderer.finishDraw did not clear draw mode");
    }

    rendererSmokeDrawn = true;
    console.log("CTJS_RENDERER_SMOKE_DRAWN");
});

console.log(marker);
