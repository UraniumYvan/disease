package com.uranium.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class ArsenicPoisoningEffect extends MobEffect {
    public ArsenicPoisoningEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, Integer.MAX_VALUE, amplifier));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, Integer.MAX_VALUE, amplifier));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, Integer.MAX_VALUE, amplifier));
            entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, Integer.MAX_VALUE, amplifier));
            entity.hurt(entity.damageSources().magic(), amplifier * 0.5F);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int frequency = 20;
        return duration % frequency == 0;
    }
}
