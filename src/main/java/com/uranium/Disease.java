package com.uranium;

import com.mojang.logging.LogUtils;
import com.uranium.effects.ArsenicPoisoningEffect;
import com.uranium.effects.RabiesEffect;
import com.uranium.effects.SteroidEffect;
import com.uranium.items.SteroidSyringeItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Disease.MODID)
public class Disease {
    // 定义模组ID
    public static final String MODID = "disease";
    // 直接引用 slf4j 日志记录器
    private static final Logger LOGGER = LogUtils.getLogger();
    // 注册方块用
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // 注册物品用
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // 注册物品栏用
    public static final DeferredRegister<CreativeModeTab> DISEASE_ITEM_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    // 注册效果用
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MODID);
    public static final DeferredHolder<MobEffect, RabiesEffect> RABIES_EFFECT = EFFECTS.register("rabies", () -> new RabiesEffect(MobEffectCategory.HARMFUL, 0xFF4500));
    public static final DeferredHolder<MobEffect, ArsenicPoisoningEffect> ARSENIC_POISONING = EFFECTS.register("arsenic_poisoning", () -> new ArsenicPoisoningEffect(MobEffectCategory.HARMFUL, 0xB7099E));
    public static final DeferredHolder<MobEffect, SteroidEffect> STEROID_EFFECT = EFFECTS.register("steroid", () -> new SteroidEffect(MobEffectCategory.HARMFUL, 0xFF94DB));;

    public static final FoodProperties LILY_FRUIT_FOOD = new FoodProperties.Builder()
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 120*20, 5), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 3600*20, 2), 1.0f)
            .nutrition(1)           // 营养值
            .saturationModifier(1.2f) // 饱和度修饰符
            .build();               // 构建对象

    // 创建一个物品针尖
    public static final DeferredItem<Item> PINPOINT = ITEMS.registerSimpleItem("pinpoint");
    // 创建物品注射器
    public static final DeferredItem<Item> SYRINGE = ITEMS.registerSimpleItem("syringe");
    // 创建物品铃兰果实
    public static final DeferredHolder<Item, Item> LILY_FRUIT = ITEMS.register("lily_fruit",
            () -> new Item(new Item.Properties().food(LILY_FRUIT_FOOD)));
    public static final DeferredItem<Item> ARSENIC_TRIOXIDE = ITEMS.registerSimpleItem("arsenic_trioxide");
    public static final DeferredItem<Item> STEROID_SYRINGE = ITEMS.register("steroid_syringe", () -> new SteroidSyringeItem(new Item.Properties()));

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DISEASE_TAB = DISEASE_ITEM_TAB.register("disease_item_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.disease"))
            .icon(() -> new ItemStack(SYRINGE.get()))
            .displayItems((parameters, output) -> {
                output.accept(PINPOINT.get());
                output.accept(SYRINGE.get());
                output.accept(STEROID_SYRINGE.get());
            })
            .build());


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Disease(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        DISEASE_ITEM_TAB.register(modEventBus);
        EFFECTS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock) LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // 服务器启动时所做之事
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void infect(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();

        if (source.getEntity() instanceof Wolf wolf) {
            if (wolf.hasEffect(RABIES_EFFECT)) {
                event.getEntity().addEffect(new MobEffectInstance(RABIES_EFFECT, Integer.MAX_VALUE, 5));
            }
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // 一些注册客户端的代码
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
