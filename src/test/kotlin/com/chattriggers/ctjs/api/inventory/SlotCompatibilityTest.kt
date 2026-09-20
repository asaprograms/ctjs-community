package com.chattriggers.ctjs.api.inventory

import net.minecraft.world.SimpleContainer
import net.minecraft.world.inventory.Slot as MCSlot
import kotlin.test.Test
import kotlin.test.assertEquals

class SlotCompatibilityTest {
    @Test
    fun `legacy slot getters expose values rather than property references`() {
        val slot = Slot(MCSlot(SimpleContainer(4), 3, 11, 17))

        assertEquals(3, slot.index)
        assertEquals(11, slot.displayX)
        assertEquals(17, slot.displayY)
        assertEquals(4, slot.inventory.size)
    }
}
