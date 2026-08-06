package com.uranium.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;


public class MinecraftTools {
    // 判断实体是否在水、装水的炼药锅或雨中
    public static boolean isInWaterOrInRain(Entity entity) {
        Level level = entity.level();

        if (entity.isInWaterRainOrBubble()) {
            return true;
        }

        BlockPos pos = BlockPos.containing(entity.getX(), entity.getY(), entity.getZ());
        BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.WATER_CAULDRON)) {
            return true;
        }

        return false;
    }

    // 判断实体是否为玩家，村民，掠夺者，卫道士，幻魔者，幻术师
    public static boolean isHuman(Entity entity) {
        return entity instanceof net.minecraft.world.entity.player.Player ||
                entity instanceof net.minecraft.world.entity.npc.Villager ||
                entity instanceof net.minecraft.world.entity.npc.WanderingTrader ||
                entity instanceof net.minecraft.world.entity.monster.Vindicator ||
                entity instanceof net.minecraft.world.entity.monster.Evoker ||
                entity instanceof net.minecraft.world.entity.monster.Illusioner ||
                entity instanceof net.minecraft.world.entity.monster.Pillager ||
                entity instanceof net.minecraft.world.entity.monster.Witch;
    }
}
