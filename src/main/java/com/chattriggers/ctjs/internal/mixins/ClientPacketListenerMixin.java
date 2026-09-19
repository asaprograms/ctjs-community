package com.chattriggers.ctjs.internal.mixins;

import com.chattriggers.ctjs.api.entity.PlayerMP;
import com.chattriggers.ctjs.api.inventory.Item;
import com.chattriggers.ctjs.api.triggers.CancellableEvent;
import com.chattriggers.ctjs.api.triggers.TriggerType;
import com.chattriggers.ctjs.api.world.NoteBlockOctave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    private static final String[] CTJS$NOTE_NAMES = {
        "F_SHARP", "G", "G_SHARP", "A", "A_SHARP", "B",
        "C", "C_SHARP", "D", "D_SHARP", "E", "F"
    };

    @Inject(method = "handleBlockEvent", at = @At("HEAD"), cancellable = true)
    private void ctjs$noteBlockPlay(ClientboundBlockEventPacket packet, CallbackInfo ci) {
        if (!(packet.getBlock() instanceof NoteBlock)) return;

        int noteId = packet.getB1();
        CancellableEvent event = new CancellableEvent();
        TriggerType.NOTE_BLOCK_PLAY.triggerAll(
            new Vector3f(packet.getPos().getX(), packet.getPos().getY(), packet.getPos().getZ()),
            ctjs$noteName(noteId),
            NoteBlockOctave.fromNoteId(noteId),
            event
        );
        if (event.isCancelled()) ci.cancel();
    }

    @Inject(method = "handleBlockUpdate", at = @At("HEAD"), cancellable = true)
    private void ctjs$noteBlockChange(ClientboundBlockUpdatePacket packet, CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        BlockState next = packet.getBlockState();
        if (level == null || !(next.getBlock() instanceof NoteBlock)) return;

        BlockState current = level.getBlockState(packet.getPos());
        if (!(current.getBlock() instanceof NoteBlock)) return;

        int oldNote = current.getValue(NoteBlock.NOTE);
        int newNote = next.getValue(NoteBlock.NOTE);
        if (oldNote == newNote) return;

        CancellableEvent event = new CancellableEvent();
        TriggerType.NOTE_BLOCK_CHANGE.triggerAll(
            new Vector3f(packet.getPos().getX(), packet.getPos().getY(), packet.getPos().getZ()),
            ctjs$noteName(newNote),
            NoteBlockOctave.fromNoteId(newNote),
            event
        );
        if (event.isCancelled()) ci.cancel();
    }

    private static String ctjs$noteName(int noteId) {
        return CTJS$NOTE_NAMES[Math.floorMod(noteId, CTJS$NOTE_NAMES.length)];
    }

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
