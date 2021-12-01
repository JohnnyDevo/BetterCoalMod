package me.johnnydevo.bettercoalmod.datagen.client.recipebuilders;

import me.johnnydevo.bettercoalmod.crafting.recipe.DecompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

public class DecompressingRecipeBuilder extends AbstractBetterCoalRecipeBuilder {

    public DecompressingRecipeBuilder(IItemProvider result, Ingredient ingredient, float experience, int recipeTime, IRecipeSerializer<DecompressingRecipe> serializer, int resultAmt) {
        this.result = result.asItem();
        this.ingredients = new Ingredient[]{ingredient};
        this.experience = experience;
        this.recipeTime = recipeTime;
        this.serializer = serializer;
        this.resultAmt = resultAmt;
    }

    public static DecompressingRecipeBuilder build(IItemProvider result, Ingredient ingredient, float experience, int recipeTime, int resultAmt) {
        return new DecompressingRecipeBuilder(result, ingredient, experience, recipeTime, ModRecipes.Serializers.DECOMPRESSING.get(), resultAmt);
    }

    @Override
    public String getRecipeNameModifier() {
        return "_from_decompressing";
    }
}