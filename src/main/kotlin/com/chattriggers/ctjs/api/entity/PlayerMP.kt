package com.chattriggers.ctjs.api.entity

import com.chattriggers.ctjs.api.client.Client
import com.chattriggers.ctjs.api.message.TextComponent
import com.chattriggers.ctjs.api.render.Renderer
import com.chattriggers.ctjs.internal.NameTagOverridable
import com.chattriggers.ctjs.internal.mixins.PlayerInfoAccessor
import com.chattriggers.ctjs.MCTeam
import com.chattriggers.ctjs.internal.utils.asMixin
import net.minecraft.client.multiplayer.PlayerInfo
import net.minecraft.world.entity.player.Player
import net.minecraft.network.chat.Component
import org.mozilla.javascript.NativeObject

class PlayerMP(override val mcValue: Player) : LivingEntity(mcValue) {
    fun isSpectator() = mcValue.isSpectator

    fun getPing(): Int {
        return getPlayerInfo()?.latency ?: -1
    }

    fun getTeam(): Team? {
        return getPlayerInfo()?.team?.let(::Team)
    }

    /**
     * Gets the display name for this player,
     * i.e. the name shown in tab list and in the player's nametag.
     * @return the display name
     */
    fun getDisplayName() = getPlayerName(getPlayerInfo())

    fun setTabDisplayName(textComponent: TextComponent) {
        getPlayerInfo()?.asMixin<PlayerInfoAccessor>()?.ctjs_setTabListDisplayName(textComponent)
    }

    /**
     * Sets the name for this player shown above their head,
     * in their name tag
     *
     * @param textComponent the new name to display
     */
    fun setNametagName(textComponent: TextComponent) {
        mcValue.asMixin<NameTagOverridable>().ctjs_setOverriddenNametagName(textComponent)
    }

    /**
     * Draws the player in the GUI. Takes the same parameters as [Renderer.drawPlayer]
     * minus `player`.
     *
     * @see Renderer.drawPlayer
     */
    fun draw(obj: NativeObject) = apply {
        obj.put("player", obj, this)
        Renderer.drawPlayer(obj)
    }

    /** Legacy positional player renderer. */
    @JvmOverloads
    fun draw(x: Int, y: Int, rotate: Boolean = false) = draw(NativeObject().also {
        it.put("x", it, x)
        it.put("y", it, y)
        it.put("rotate", it, rotate)
    })

    private fun getPlayerName(playerListEntry: PlayerInfo?): TextComponent {
        return playerListEntry?.tabListDisplayName?.let { TextComponent(it) }
            ?: TextComponent(
                MCTeam.formatNameForTeam(
                    playerListEntry?.team,
                    Component.nullToEmpty(playerListEntry?.profile?.name)
                )
            )
    }

    private fun getPlayerInfo() = Client.getConnection()?.getPlayerInfo(mcValue.uuid)
}
