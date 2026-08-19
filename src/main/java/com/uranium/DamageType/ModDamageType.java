package com.uranium.DamageType;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ModDamageType {
    // 定义病死的ResourceKey
    public static final ResourceKey<DamageType> DISEASE_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath("disease", "disease"));

    // 创建 DamageSource 的辅助方法
    public static DamageSource of(Level level, ResourceKey<DamageType> key) {
        // 通过 Level 获取伤害类型注册表，然后获取对应的 Holder
        return new DamageSource(level.registryAccess()
                .lookupOrThrow(Registries.DAMAGE_TYPE)
                .getOrThrow(key));
    }
}
