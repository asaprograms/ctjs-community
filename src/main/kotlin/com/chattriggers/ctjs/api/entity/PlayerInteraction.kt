package com.chattriggers.ctjs.api.entity

import net.minecraft.world.InteractionHand

sealed class PlayerInteraction(val name: String, val mainHand: Boolean) {
    object AttackBlock : PlayerInteraction("AttackBlock", true)
    object AttackEntity : PlayerInteraction("AttackEntity", true)
    object BreakBlock : PlayerInteraction("BreakBlock", true)
    data class UseBlock(val hand: InteractionHand) : PlayerInteraction("UseBlock", hand == InteractionHand.MAIN_HAND)
    data class UseEntity(val hand: InteractionHand) : PlayerInteraction("UseEntity", hand == InteractionHand.MAIN_HAND)
    data class UseItem(val hand: InteractionHand) : PlayerInteraction("UseItem", hand == InteractionHand.MAIN_HAND)

    override fun toString(): String = name
}

/** Historical 1.8.9 player-interaction constants. */
object InteractAction {
    @JvmField val LEFT_CLICK_BLOCK: PlayerInteraction = PlayerInteraction.AttackBlock
    @JvmField val RIGHT_CLICK_BLOCK: PlayerInteraction = PlayerInteraction.UseBlock(InteractionHand.MAIN_HAND)
    @JvmField val RIGHT_CLICK_AIR: PlayerInteraction = PlayerInteraction.UseItem(InteractionHand.MAIN_HAND)
}
