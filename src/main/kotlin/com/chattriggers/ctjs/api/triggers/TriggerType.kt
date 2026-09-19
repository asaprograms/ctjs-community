package com.chattriggers.ctjs.api.triggers

import com.chattriggers.ctjs.internal.engine.JSLoader

sealed interface ITriggerType {
    val name: String

    fun triggerAll(vararg args: Any?) {
        JSLoader.exec(this, args)
    }
}

enum class TriggerType : ITriggerType {
    // client
    CHAT,
    ACTION_BAR,
    TICK,
    STEP,
    GAME_UNLOAD,
    GAME_LOAD,
    CLICKED,
    SCROLLED,
    DRAGGED,
    GUI_OPENED,
    MESSAGE_SENT,
    ITEM_TOOLTIP,
    PLAYER_INTERACT,
    ATTACK_ENTITY,
    HIT_BLOCK,
    BLOCK_BREAK,
    GUI_KEY,
    GUI_MOUSE_CLICK,
    GUI_MOUSE_RELEASE,
    GUI_MOUSE_DRAG,
    PACKET_SENT,
    PACKET_RECEIVED,
    SERVER_CONNECT,
    SERVER_DISCONNECT,
    GUI_CLOSED,
    DROP_ITEM,
    PICKUP_ITEM,
    SCREENSHOT_TAKEN,
    CHAT_COMPONENT_CLICKED,
    CHAT_COMPONENT_HOVERED,

    // rendering
    PRE_RENDER_WORLD,
    POST_RENDER_WORLD,
    BLOCK_HIGHLIGHT,
    RENDER_OVERLAY,
    RENDER_PLAYER_LIST,
    RENDER_ENTITY,
    POST_RENDER_ENTITY,
    RENDER_BLOCK_ENTITY,
    POST_RENDER_BLOCK_ENTITY,
    GUI_RENDER,
    POST_GUI_RENDER,
    RENDER_SLOT,
    PRE_ITEM_RENDER,
    RENDER_SLOT_HIGHLIGHT,
    RENDER_ITEM_INTO_GUI,
    RENDER_ITEM_OVERLAY_INTO_GUI,
    RENDER_CROSSHAIR,
    RENDER_DEBUG,
    RENDER_BOSS_HEALTH,
    RENDER_HEALTH,
    RENDER_ARMOR,
    RENDER_FOOD,
    RENDER_MOUNT_HEALTH,
    RENDER_HOTBAR,
    RENDER_AIR,
    RENDER_PORTAL,
    RENDER_CHAT,
    RENDER_SCOREBOARD,
    RENDER_TITLE,
    GUI_DRAW_BACKGROUND,
    RENDER_HAND,

    // world
    SOUND_PLAY,
    WORLD_LOAD,
    WORLD_UNLOAD,
    SPAWN_PARTICLE,
    ENTITY_DEATH,
    ENTITY_DAMAGE,
    NOTE_BLOCK_PLAY,
    NOTE_BLOCK_CHANGE,

    // misc
    COMMAND,
    OTHER
}

data class CustomTriggerType(override val name: String) : ITriggerType
