package LuckyDiceMod;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory; // 修改为此行
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import java.util.function.Consumer;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

public class ModRecipes extends RecipeProvider implements IConditionBuilder {
    public ModRecipes(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // 添加升级配方（可选）
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ENHANCED_DICE.get())
                .requires(ModItems.LUCKY_DICE.get())
                .requires(Items.NETHER_STAR)
                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
                .save(consumer, new ResourceLocation("luckydice", "enhanced_dice"));
    }
}