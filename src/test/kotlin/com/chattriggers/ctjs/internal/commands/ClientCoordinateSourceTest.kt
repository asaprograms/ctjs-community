package com.chattriggers.ctjs.internal.commands

import net.minecraft.commands.CommandSource
import com.mojang.brigadier.StringReader
import net.minecraft.commands.arguments.coordinates.WorldCoordinates
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import kotlin.test.Test
import kotlin.test.assertEquals

class ClientCoordinateSourceTest {
    @Test
    fun `client coordinate source can be created without server state`() {
        val position = Vec3(10.0, 64.0, -20.0)
        val rotation = Vec2(15f, 90f)
        val source = ClientCoordinateSource.create(
            CommandSource.NULL, position, rotation, "Fixture", Component.literal("Fixture"), null,
        )
        assertEquals(position, source.position)
        assertEquals(rotation, source.rotation)
        val relative = WorldCoordinates.parseDouble(StringReader("~2 ~-4 ~5"), false)
        assertEquals(Vec3(12.0, 60.0, -15.0), relative.getPosition(source))
    }
}
