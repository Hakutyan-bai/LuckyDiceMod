package LuckyDiceMod;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("luckydice")
public class LuckyDiceMod {
    public LuckyDiceMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册物品
        ModItems.register(eventBus);

        // 注册粒子效果
        ModParticles.register(eventBus);


        // 注册事件监听器
        MinecraftForge.EVENT_BUS.register(this);
    }
}