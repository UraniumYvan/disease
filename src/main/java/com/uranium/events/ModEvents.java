package com.uranium.events;

import com.uranium.Disease;
import com.uranium.effects.RabiesEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import static com.uranium.Disease.MODID;

// 确保这个类被模组加载，通常使用 @EventBusSubscriber 自动注册
@EventBusSubscriber(modid = MODID)
public class ModEvents {
    /**
     * 创建创造模式物品栏的条目
     */
    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(Disease.LILY_FRUIT.get());
        }
    }

    /**
     * 移除效果时，清除死亡倒计时
     */
    @SubscribeEvent
    public static void onEffectRemove(MobEffectEvent.Remove event) {
        // 获取效果实例
        MobEffectInstance effectInstance = event.getEffectInstance();

        // 检查移除的是否是我们的狂犬病效果
        if (effectInstance != null && effectInstance.getEffect().value() instanceof RabiesEffect) {
            LivingEntity entity = event.getEntity();

            // 核心逻辑：清除死亡倒计时
            // 这样如果玩家喝牛奶，倒计时会被清空，不会死
            entity.getPersistentData().remove("RabiesDeathTimer");
        }
    }
}
