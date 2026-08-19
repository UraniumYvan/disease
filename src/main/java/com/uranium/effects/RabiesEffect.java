package com.uranium.effects;

import com.uranium.DamageType.ModDamageType;
import com.uranium.tools.MinecraftTools;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class RabiesEffect extends MobEffect {
    private static final int DEATH_TIME = 20 * 60 * 20;
    private static final String TAG_RABIES_DEATH_TIMER = "RabiesDeathTimer";

    public RabiesEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        // 检测是否在服务端
        if (!entity.level().isClientSide() &&
                !(entity instanceof net.minecraft.world.entity.monster.Skeleton) &&
                !(entity instanceof net.minecraft.world.entity.monster.Zombie) &&
                !(entity instanceof net.minecraft.world.entity.monster.WitherSkeleton) &&
                !(entity instanceof net.minecraft.world.entity.monster.Husk) &&
                !(entity instanceof net.minecraft.world.entity.monster.Drowned) &&
                !(entity instanceof net.minecraft.world.entity.monster.ZombieVillager) &&
                !(entity instanceof net.minecraft.world.entity.monster.ZombifiedPiglin) &&
                !(entity instanceof net.minecraft.world.entity.monster.Zoglin) &&
                !(entity instanceof net.minecraft.world.entity.monster.Stray)) {
            ////////////////////////////////////////////////////////////////
            // 症状：恐惧水
            // 检测是否在水、装水炼药锅或雨中
            if (MinecraftTools.isInWaterOrInRain(entity)) {
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, amplifier, false, true));
                float damage = 0.5f + (float) amplifier;
                entity.hurt(entity.damageSources().magic(), damage);
            }
            ////////////////////////////////////////////////////////////////
            // 症状：恐惧光亮
            BlockPos pos = entity.blockPosition();
            int lightLevel = entity.level().getLightEngine().getRawBrightness(pos, 0);

            if (lightLevel >= 10) {
                // 给予10秒的虚弱效果
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, amplifier, false, true));
            }
            ////////////////////////////////////////////////////////////////
            // 致死过程
            if (MinecraftTools.isHuman(entity)) {
                CompoundTag data = entity.getPersistentData();
                if (!data.contains(TAG_RABIES_DEATH_TIMER)) {
                    data.putInt(TAG_RABIES_DEATH_TIMER, DEATH_TIME);
                }
                int timer = data.getInt(TAG_RABIES_DEATH_TIMER);
                timer -= 20;

                if (timer <= 0) {
                    entity.hurt(ModDamageType.of(entity.level(), ModDamageType.DISEASE_DAMAGE_TYPE), Float.MAX_VALUE);
                    data.remove(TAG_RABIES_DEATH_TIMER);
                } else {
                    data.putInt(TAG_RABIES_DEATH_TIMER, timer);
                }
            }
            ////////////////////////////////////////////////////////////////
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int frequency = 20;
        return duration % frequency == 0;
    }
}
