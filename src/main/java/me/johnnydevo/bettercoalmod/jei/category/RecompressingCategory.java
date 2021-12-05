package me.johnnydevo.bettercoalmod.jei.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.crafting.recipe.CompressingRecipe;
import me.johnnydevo.bettercoalmod.crafting.recipe.RecompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class RecompressingCategory implements IRecipeCategory<RecompressingRecipe> {

    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final ResourceLocation guiLocation;
    private final IDrawable gui;
    private final IDrawable icon;
    private final String title;
    private final ResourceLocation uid;

    public RecompressingCategory(IGuiHelper helper) {
        guiLocation = new ResourceLocation(BetterCoalMod.MOD_ID, "textures/gui/matter_recompressor.png");
        cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<Integer, IDrawableAnimated>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return helper.drawableBuilder(guiLocation, 176, 14, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        gui = helper.createDrawable(guiLocation, 47, 13, 96, 59);
        icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.MATTER_RECOMPRESSOR.get()));
        title = new TranslationTextComponent("block.bettercoalmod.matter_recompressor").getString();
        uid = new ResourceLocation(BetterCoalMod.MOD_ID, ModNames.RECOMPRESSING_CATEGORY);
    }

    @Override
    public ResourceLocation getUid() {
        return uid;
    }

    @Override
    public Class<? extends RecompressingRecipe> getRecipeClass() {
        return RecompressingRecipe.class;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return gui;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(RecompressingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, RecompressingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();

        itemStackGroup.init(0, true, 12, 8);
        itemStackGroup.init(1, true, 12, 33);
        itemStackGroup.init(2, false, 68, 21);

        itemStackGroup.set(ingredients);
    }

    @Override
    public void draw(RecompressingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        int time = recipe.getRecipeTime();
        if (time <= 0) {
            time = 200;
        }
        cachedArrows.getUnchecked(time).draw(matrixStack, 32, 22);
    }
}
