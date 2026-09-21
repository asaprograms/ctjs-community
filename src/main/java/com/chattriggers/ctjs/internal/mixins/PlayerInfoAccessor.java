package com.chattriggers.ctjs.internal.mixins;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerInfo.class)
public interface PlayerInfoAccessor {
    @Accessor("tabListDisplayName")
    void ctjs_setTabListDisplayName(Component displayName);

    @Invoker
    void invokeSetLatency(int latency);
}
