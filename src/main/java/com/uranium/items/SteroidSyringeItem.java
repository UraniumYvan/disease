package com.uranium.items;

import com.uranium.Disease;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SteroidSyringeItem extends Item {
    public SteroidSyringeItem(Properties properties) {
        super(properties);
        System.out.println("物品注册成功");
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level().isClientSide()) {
            System.out.println("方法成功调用");
            target.hurt(player.level().damageSources().generic(), 1.0f);
            target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0));
            target.addEffect(new MobEffectInstance(Disease.STEROID_EFFECT, Integer.MAX_VALUE, 0));
            stack.shrink(1);
            player.addItem(new ItemStack(Disease.SYRINGE.get(), 1));
        }
        return InteractionResult.sidedSuccess(player.level().isClientSide());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();

        Level level = context.getLevel();

        if (!level.isClientSide() && player != null) {
            System.out.println("方法成功调用");
            player.hurt(level.damageSources().generic(), 1.0f);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0));
            player.addEffect(new MobEffectInstance(Disease.STEROID_EFFECT, Integer.MAX_VALUE, 0));
            context.getItemInHand().shrink(1);
            player.addItem(new ItemStack(Disease.SYRINGE.get(), 1));
        }
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            System.out.println("方法成功调用");
            player.hurt(player.damageSources().generic(), 1.0F);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0));
            player.addEffect(new MobEffectInstance(Disease.STEROID_EFFECT, Integer.MAX_VALUE, 0));
            player.setItemInHand(hand, new ItemStack(Disease.SYRINGE.get()));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}

// 该代码未完成对生物右键能够对生物造成1点伤害，并给予其力量1与自定义效果SteroidEffect，若对空或对方块右键则对自己造成1点伤害并给予自身1点伤害并将手中物品替换为SyringeItem的指定功能，分析原因并解决
