package me.johnnydevo.bettercoalmod.jei.category;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.crafting.recipe.RecompressingRecipe;
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
import net.minecraft.util.text.TranslationTextComponent;

public class RecompressingCategory implements IRecipeCategory<RecompressingRecipe> {

    private IGuiHelper helper;

    public RecompressingCategory(IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation(BetterCoalMod.MOD_ID, ModNames.RECOMPRESSING_CATEGORY);
    }

    @Override
    public Class<? extends RecompressingRecipe> getRecipeClass() {
        return RecompressingRecipe.class;
    }

    @Override
    public String getTitle() {
        return new TranslationTextComponent("block.bettercoalmod.matter_recompressor").getString();
    }

    @Override
    public IDrawable getBackground() {
        return helper.createDrawable(new ResourceLocation(BetterCoalMod.MOD_ID, "textures/gui/matter_recompressor.png"), 47, 13, 96, 59);
    }

    @Override
    public IDrawable getIcon() {
        return helper.createDrawableIngredient(new ItemStack(ModBlocks.MATTER_RECOMPRESSOR.get()));
    }

    @Override
    public void setIngredients(RecompressingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecompressingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();

        itemStackGroup.init(0, true, 0, 0);
        itemStackGroup.init(1, false, 60, 0);

        itemStackGroup.set(ingredients);
    }
}
