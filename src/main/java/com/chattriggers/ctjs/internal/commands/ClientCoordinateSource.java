package com.chattriggers.ctjs.internal.commands;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

/** Coordinate parsing needs client position and rotation, not a server world. */
public final class ClientCoordinateSource {
    private ClientCoordinateSource() {}

    public static CommandSourceStack create(CommandSource output, Vec3 position, Vec2 rotation,
                                            String name, Component displayName, Entity entity) {
        return new CommandSourceStack(output, position, rotation, null, permission -> true,
                name, displayName, null, entity);
    }
}
