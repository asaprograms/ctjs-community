package com.chattriggers.ctjs.api.world.block

import com.chattriggers.ctjs.api.message.Message
import com.chattriggers.ctjs.api.world.World
import net.minecraft.world.level.block.entity.SignBlockEntity

/** Compatibility wrapper for the front text of a legacy sign block. */
class Sign(block: Block) : Block(block) {
    private fun sign(): SignBlockEntity? = World.toMC()?.getBlockEntity(pos.toMC()) as? SignBlockEntity

    fun getLines(): Array<Message> = Array(4) { index ->
        sign()?.frontText?.getMessage(index, false)?.let { line -> Message(line) } ?: Message("")
    }

    fun getFormattedLines(): Array<String> = Array(4) { index ->
        sign()?.frontText?.getMessage(index, false)?.let { line -> Message(line).getFormattedText() } ?: ""
    }

    fun getUnformattedLines(): Array<String> = Array(4) { index ->
        sign()?.frontText?.getMessage(index, false)?.string ?: ""
    }

    override fun toString() = "Sign{lines=${getUnformattedLines().contentToString()}, name=${getRegistryName()}, x=$x, y=$y, z=$z}"
}
