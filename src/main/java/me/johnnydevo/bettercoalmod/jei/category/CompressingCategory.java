package me.johnnydevo.bettercoalmod.jei.category;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.crafting.recipe.CompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CompressingCategory implements IRecipeCategory<CompressingRecipe> {

    private IGuiHelper helper;

    public CompressingCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(BetterCoalMod.MOD_ID, ModNames.COMPRESSING_CATEGORY);
    }

    @Override
    public Class<? extends CompressingRecipe> getRecipeClass() {
        return CompressingRecipe.class;
    }

    @Override
    public String getTitle() {
        return new ItemStack(ModBlocks.MATTER_COMPRESSOR.get()).getDisplayName().getString();
    }

    @Override
    public IDrawable getBackground() {
        return helper.createDrawable(new ResourceLocation(BetterCoalMod.MOD_ID, "textures/gui/matter_compressor.png"), 0, 114, 82, 54);
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableIngredient(new ItemStack(ModBlocks.MATTER_COMPRESSOR.get()));
    }

    @Override
    public void setIngredients(CompressingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CompressingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();

        itemStackGroup.init(0, true, 0, 0);
        itemStackGroup.init(1, false, 60, 0);

        itemStackGroup.set(ingredients);
    }
}
