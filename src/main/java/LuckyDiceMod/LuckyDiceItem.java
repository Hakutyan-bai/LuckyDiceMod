package LuckyDiceMod;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.data.DataGenerator;

import java.util.List;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

public class LuckyDiceItem extends Item {
    private static final Random RANDOM = new Random();
    private static final Map<MobEffect, String> POSITIVE_EFFECT_NAMES = new HashMap<>();
    private static final Map<MobEffect, String> NEGATIVE_EFFECT_NAMES = new HashMap<>();
    static {
        POSITIVE_EFFECT_NAMES.put(MobEffects.REGENERATION, "生命恢复");
        POSITIVE_EFFECT_NAMES.put(MobEffects.DAMAGE_BOOST, "力量");
        POSITIVE_EFFECT_NAMES.put(MobEffects.MOVEMENT_SPEED, "速度");
        POSITIVE_EFFECT_NAMES.put(MobEffects.DAMAGE_RESISTANCE, "抗性提升");
        POSITIVE_EFFECT_NAMES.put(MobEffects.FIRE_RESISTANCE, "防火");
        POSITIVE_EFFECT_NAMES.put(MobEffects.HEALTH_BOOST, "生命提升");
        POSITIVE_EFFECT_NAMES.put(MobEffects.ABSORPTION, "伤害吸收");
        POSITIVE_EFFECT_NAMES.put(MobEffects.LUCK, "幸运");
        POSITIVE_EFFECT_NAMES.put(MobEffects.HERO_OF_THE_VILLAGE, "村庄英雄");
        POSITIVE_EFFECT_NAMES.put(MobEffects.NIGHT_VISION, "夜视");
        POSITIVE_EFFECT_NAMES.put(MobEffects.WATER_BREATHING, "水下呼吸");
        POSITIVE_EFFECT_NAMES.put(MobEffects.SATURATION, "饱和");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.POISON, "中毒");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.BLINDNESS, "失明");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.CONFUSION, "反胃");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.HUNGER, "饥饿");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.WEAKNESS, "虚弱");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.UNLUCK, "霉运");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.WITHER, "凋零");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.MOVEMENT_SLOWDOWN, "缓慢");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.DIG_SLOWDOWN, "挖掘疲劳");
        NEGATIVE_EFFECT_NAMES.put(MobEffects.BAD_OMEN, "不祥之兆");
    }

    public LuckyDiceItem(Properties properties) {
        super(properties);
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
//        ItemStack dice = player.getItemInHand(hand);
//
//        if (!world.isClientSide) {
//            player.sendSystemMessage(Component.literal("§6幸运骰子正在使用中..."));
//            int roll = RANDOM.nextInt(6) + 1; // 1-6
//            player.sendSystemMessage(Component.literal("§6你掷出了: §e" + roll + "§6!"));
//
//            switch (roll) {
//                case 6: giveEnchantedGoldenApple(player); break;
//                case 5: giveGoldenApple(player); break;
//                case 4: giveRandomNegativeEffect(player); break;
//                case 3: giveRandomPositiveEffect(player); break;
//                case 2: giveSaturation(player); break;
//                case 1: playerExplode(player); break;
//            }
//
//            player.getCooldowns().addCooldown(this, 100); // 5秒冷却
//            dice.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
//        }
//
//        return InteractionResultHolder.sidedSuccess(dice, world.isClientSide());
//    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack dice = player.getItemInHand(hand);

        if (!world.isClientSide) {
            // 获取服务器实例
            MinecraftServer server = player.getServer();

            // 全服广播玩家使用动作
            server.getPlayerList().broadcastSystemMessage(
                    Component.literal("§e" + player.getName().getString() + " §6掏出了幸运骰子..."),
                    false
            );

            int roll = RANDOM.nextInt(6) + 1;

            // 全服广播骰子点数
            server.getPlayerList().broadcastSystemMessage(
                    Component.literal("§e" + player.getName().getString() +
                            " §6掷出了: §l§e" + roll + "§6!"),
                    false
            );

            switch (roll) {
                case 6:
                    giveEnchantedGoldenApple(player);
                    server.getPlayerList().broadcastSystemMessage(
                            Component.literal("§e" + player.getName().getString() +
                                    " §b§l[奇迹降临] 获得附魔金苹果!"),
                            false);
                    break;
                case 5:
                    giveGoldenApple(player);
                    server.getPlayerList().broadcastSystemMessage(
                            Component.literal("§e" + player.getName().getString() +
                                    " §a[好运爆棚] 获得金苹果!"),
                            false);
                    break;
                case 4: giveRandomNegativeEffect(player);

                    break;
                case 3: giveRandomPositiveEffect(player);

                    break;
                case 2: giveSaturation(player);
                    server.getPlayerList().broadcastSystemMessage(
                            Component.literal("§e" + player.getName().getString() +
                                    " §b[饱和效果] 获得饱和效果！"),
                            false);
                    break;
                case 1:
                    playerExplode(player);
                    server.getPlayerList().broadcastSystemMessage(
                            Component.literal("§e" + player.getName().getString() +
                                    " §4§l[自爆惨剧] 把自己炸成了烟花!"),
                            false);
                    break;
            }
            player.getCooldowns().addCooldown(this, 100); // 5秒冷却
            dice.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
        }
        return InteractionResultHolder.sidedSuccess(dice, world.isClientSide());
    }


    private void giveEnchantedGoldenApple(Player player) {
        player.addItem(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE));
//        player.sendSystemMessage(Component.literal("§b头奖！你获得了附魔金苹果！"));
    }

    private void giveGoldenApple(Player player) {
        player.addItem(new ItemStack(Items.GOLDEN_APPLE));
//        player.sendSystemMessage(Component.literal("§b你获得了金苹果！"));
    }

    private void giveSaturation(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20 * 60, 0)); // 1分钟
//        player.sendSystemMessage(Component.literal("§b获得饱和效果1分钟！"));
    }

    private void giveRandomPositiveEffect(Player player) {
        MinecraftServer server = player.getServer();
        MobEffect[] positiveEffects = {
            MobEffects.REGENERATION,
            MobEffects.DAMAGE_BOOST,
            MobEffects.MOVEMENT_SPEED,
            MobEffects.DAMAGE_RESISTANCE,
            MobEffects.FIRE_RESISTANCE,
            MobEffects.HEALTH_BOOST,
            MobEffects.ABSORPTION,
            MobEffects.LUCK,
            MobEffects.HERO_OF_THE_VILLAGE,
            MobEffects.NIGHT_VISION,
            MobEffects.WATER_BREATHING,
            MobEffects.SATURATION
        };
        MobEffect effect = positiveEffects[RANDOM.nextInt(positiveEffects.length)];
        player.addEffect(new MobEffectInstance(effect, 20 * 10, 0));
        String name = POSITIVE_EFFECT_NAMES.getOrDefault(effect, effect.getDescriptionId());
        server.getPlayerList().broadcastSystemMessage(
                Component.literal("§e" + player.getName().getString() +
                        " §a[好运加持] 获得正面效果：" + name + " 10秒！"),
                false);
//        player.sendSystemMessage(Component.literal("§a获得正面效果：" + name + " 10秒！"));
    }

    private void giveRandomNegativeEffect(Player player) {
        MinecraftServer server = player.getServer();
        MobEffect[] negativeEffects = {
            MobEffects.POISON,
            MobEffects.BLINDNESS,
            MobEffects.CONFUSION,
            MobEffects.HUNGER,
            MobEffects.WEAKNESS,
            MobEffects.UNLUCK,
            MobEffects.WITHER,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.BAD_OMEN
        };
        MobEffect effect = negativeEffects[RANDOM.nextInt(negativeEffects.length)];
        player.addEffect(new MobEffectInstance(effect, 20 * 10, 0));
        String name = NEGATIVE_EFFECT_NAMES.getOrDefault(effect, effect.getDescriptionId());
        server.getPlayerList().broadcastSystemMessage(
                Component.literal("§e" + player.getName().getString() +
                        "§c获得负面效果：" + name + " 10秒！"),
                false);
//        player.sendSystemMessage(Component.literal("§c获得负面效果：" + name + " 10秒！"));
    }

    private void playerExplode(Player player) {
        Level world = player.level();
        // 只对玩家造成致命伤害，不破坏方块
        world.explode(player, player.getX(), player.getY(), player.getZ(), 4.0F, Level.ExplosionInteraction.NONE);
        player.setHealth(0.0F);
//        player.sendSystemMessage(Component.literal("§c你自爆了！"));
    }
}