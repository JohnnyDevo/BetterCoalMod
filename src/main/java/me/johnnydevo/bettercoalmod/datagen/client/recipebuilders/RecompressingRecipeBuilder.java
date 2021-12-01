package me.johnnydevo.bettercoalmod.datagen.client.recipebuilders;

import me.johnnydevo.bettercoalmod.crafting.recipe.RecompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

public class RecompressingRecipeBuilder extends AbstractBetterCoalRecipeBuilder {

    public RecompressingRecipeBuilder(IItemProvider result, Ingredient ingredientOne, Ingredient ingredientTwo, float experience, int recipeTime, IRecipeSerializer<RecompressingRecipe> serializer, int resultAmt) {
        this.result = result.asItem();
        this.ingredients = new Ingredient[]{ingredientOne, ingredientTwo};
        this.experience = experience;
        this.recipeTime = recipeTime;
        this.serializer = serializer;
        this.resultAmt = resultAmt;
    }

    public static RecompressingRecipeBuilder build(IItemProvider result, Ingredient ingredientOne, Ingredient ingredientTwo, float experience, int recipeTime, int resultAmt) {
        return new RecompressingRecipeBuilder(result, ingredientOne, ingredientTwo, experience, recipeTime, ModRecipes.Serializers.RECOMPRESSING.get(), resultAmt);
    }

    @Override
    public String getRecipeNameModifier() {
        return "_from_recompressing";
    }
}