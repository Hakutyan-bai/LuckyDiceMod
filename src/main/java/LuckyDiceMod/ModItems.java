package LuckyDiceMod;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.IEventBus; // 添加此行
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "luckydice");

    // 注册幸运骰子
    public static final RegistryObject<Item> LUCKY_DICE = ITEMS.register("lucky_dice",
            () -> new LuckyDiceItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(32)));


    // 注册强化骰子
    public static final RegistryObject<Item> ENHANCED_DICE = ITEMS.register("enhanced_dice",
            () -> new Item(new Item.Properties()
                    .stacksTo(1)
                    .durability(64)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // 添加到创造模式标签页
    @SubscribeEvent
    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(LUCKY_DICE.get());
        }
    }
}