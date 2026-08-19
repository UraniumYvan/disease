package com.uranium.items;

import com.uranium.Disease;
import net.minecraft.server.level.ServerLevel;
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
import org.jetbrains.annotations.NotNull;

public class SteroidSyringeItem extends Item {
    public SteroidSyringeItem(Properties properties) {
        super(properties);
    }

//    @Override@NotNull
//    public InteractionResult interactLivingEntity(ItemStack stack, Player player,LivingEntity target, InteractionHand hand) {
//        applyEffectsAndTransform(player, hand, target);
//
//        return InteractionResult.SUCCESS;
//    }
//
//    @Override@NotNull
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        ItemStack stack = player.getItemInHand(hand);
//
//        // 必须在服务端执行逻辑
//        if (level.isClientSide()) {
//            // 目标设为玩家自己
//            applyEffectsAndTransform(player, hand, player);
//        }
//
//        // 返回成功，并返回当前手中的物品栈（此时可能已经被替换）
//        return InteractionResultHolder.success(player.getItemInHand(hand));
//    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        System.out.println("[日志] 触发了 useOn！看向了方块。");
        Player player = context.getPlayer();
        Level level = context.getLevel();

//        // 如果看着方块右键，我们强制让它执行 use 的逻辑（作用于自己）
//        if (player != null && level instanceof ServerLevel serverLevel) {
//            System.out.println("[日志] 拦截方块交互，转而执行自身逻辑...");
//            applyLogic(player.getItemInHand(context.getHand()), player, context.getHand(), player, serverLevel);
//            return InteractionResult.SUCCESS; // 返回 SUCCESS 阻止方块被交互
//        }

        applyEffectsAndTransform(player, context.getHand(), player);

        // 如果不拦截，返回 PASS，让原版处理方块交互（那就不会触发你的逻辑）
        return InteractionResult.PASS;
    }

    private static void applyEffectsAndTransform(Player player, InteractionHand hand, LivingEntity target) {
        Level level = player.level();

        if (!level.isClientSide) {
            target.hurt(level.damageSources().playerAttack(player), 1.0f);
            target.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0));
//            ItemStack newStack = new ItemStack(Disease.SYRINGE.get());
//            player.setItemInHand(hand, newStack);
            // 获取手中物品
            ItemStack stack = player.getItemInHand(hand);
            stack.shrink(1);
            player.getInventory().add(new ItemStack(Disease.SYRINGE.get(), 1));
        }
    }
}
