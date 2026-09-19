package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.api.entity.PlayerMP;
import com.chattriggers.ctjs.api.inventory.Item;
import com.chattriggers.ctjs.api.triggers.CancellableEvent;
import com.chattriggers.ctjs.api.triggers.TriggerType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Inject(method = "handleTakeItemEntity", at = @At("HEAD"), cancellable = true)
    private void ctjs$pickupItem(ClientboundTakeItemEntityPacket packet, CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        Entity itemEntity = level.getEntity(packet.getItemId());
        Entity playerEntity = level.getEntity(packet.getPlayerId());
        if (!(itemEntity instanceof ItemEntity item) || !(playerEntity instanceof Player player)) {
            return;
        }

        CancellableEvent event = new CancellableEvent();
        TriggerType.PICKUP_ITEM.triggerAll(
            Item.fromMC(item.getItem()),
            new PlayerMP(player),
            new Vector3f((float) item.getX(), (float) item.getY(), (float) item.getZ()),
            new Vector3f(
                (float) item.getDeltaMovement().x,
                (float) item.getDeltaMovement().y,
                (float) item.getDeltaMovement().z
            ),
            event
        );
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}
