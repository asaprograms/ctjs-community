package com.chattriggers.ctjs.internal.mixins;

import net.minecraft.client.particle.SingleQuadParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SingleQuadParticle.class)
public interface SingleQuadParticleAccessor {
    @Accessor
    float getRCol();

    @Accessor
    float getGCol();

    @Accessor
    float getBCol();

    @Accessor
    float getAlpha();

    @Accessor
    void setAlpha(float value);
}
