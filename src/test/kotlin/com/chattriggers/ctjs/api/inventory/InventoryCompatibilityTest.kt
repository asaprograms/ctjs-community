package com.chattriggers.ctjs.api.inventory

import net.minecraft.world.SimpleContainer
import net.minecraft.SharedConstants
import net.minecraft.server.Bootstrap
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class InventoryCompatibilityTest {
    @BeforeTest
    fun bootstrapRegistries() {
        SharedConstants.tryDetectVersion()
        Bootstrap.bootStrap()
    }

    @Test
    fun `legacy inventory name remains a string`() {
        val inventory = Inventory(SimpleContainer(1))

        assertEquals("inventory", inventory.getName())
        assertEquals("inventory", inventory.getNameComponent().unformattedText)
    }
}
