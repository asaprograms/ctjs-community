package com.chattriggers.ctjs.internal.mixins;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(KeyMapping.Category.class)
public interface KeyMappingCategoryAccessor {
    @Accessor("SORT_ORDER")
    static List<KeyMapping.Category> getCategoryList() {
        throw new IllegalStateException();
    }
}
