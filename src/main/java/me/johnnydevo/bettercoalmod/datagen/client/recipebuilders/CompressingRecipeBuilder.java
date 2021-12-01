package me.johnnydevo.bettercoalmod.datagen.client.recipebuilders;

import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

public class CompressingRecipeBuilder extends AbstractBetterCoalRecipeBuilder {

    public CompressingRecipeBuilder(IItemProvider result, Ingredient ingredient, float experience, int recipeTime, IRecipeSerializer<?> serializer, int resultAmt) {
        this.result = result.asItem();
        this.ingredients = new Ingredient[]{ingredient};
        this.experience = experience;
        this.recipeTime = recipeTime;
        this.serializer = serializer;
        this.resultAmt = resultAmt;
    }

    public static CompressingRecipeBuilder build(IItemProvider result, Ingredient ingredient, float experience, int recipeTime, int resultAmt) {
        return new CompressingRecipeBuilder(result, ingredient, experience, recipeTime, ModRecipes.Serializers.COMPRESSING.get(), resultAmt);
    }

    @Override
    public String getRecipeNameModifier() {
        return "_from_compressing";
    }
}