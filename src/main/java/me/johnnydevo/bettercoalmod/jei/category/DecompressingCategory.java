package me.johnnydevo.bettercoalmod.jei.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.matrix.MatrixStack;
import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.crafting.recipe.CompressingRecipe;
import me.johnnydevo.bettercoalmod.crafting.recipe.DecompressingRecipe;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class DecompressingCategory implements IRecipeCategory<DecompressingRecipe> {

    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final IDrawableAnimated animatedFire;
    private final ResourceLocation guiLocation;
    private final IDrawable gui;
    private final IDrawable icon;
    private final String title;
    private final ResourceLocation uid;

    public DecompressingCategory(IGuiHelper helper) {
        guiLocation = new ResourceLocation(BetterCoalMod.MOD_ID, "textures/gui/matter_decompressor.png");
        cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<Integer, IDrawableAnimated>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return helper.drawableBuilder(guiLocation, 176, 14, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
        animatedFire = helper.createAnimatedDrawable(
                helper.createDrawable(guiLocation, 176, 0, 14, 14),
                300, IDrawableAnimated.StartDirection.TOP, false);
        gui = helper.createDrawable(guiLocation, 47, 13, 96, 59);
        icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.MATTER_DECOMPRESSOR.get()));
        title = new TranslationTextComponent("block.bettercoalmod.matter_decompressor").getString();
        uid = new ResourceLocation(BetterCoalMod.MOD_ID, ModNames.DECOMPRESSING_CATEGORY);
    }

    @Override
    public ResourceLocation getUid() {
        return uid;
    }

    @Override
    public Class<? extends DecompressingRecipe> getRecipeClass() {
        return DecompressingRecipe.class;
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
    public void setIngredients(DecompressingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, DecompressingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();

        itemStackGroup.init(0, true, 8, 39);
        itemStackGroup.init(1, false, 68, 21);

        itemStackGroup.set(ingredients);
    }

    @Override
    public void draw(DecompressingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontRenderer = minecraft.font;

        float exp = recipe.getExperience();
        if (exp > 0) {
            TranslationTextComponent expString = new TranslationTextComponent("gui.jei.category.smelting.experience", exp);
            int stringWidth = fontRenderer.width(expString);
            fontRenderer.draw(matrixStack, expString, gui.getWidth() - stringWidth, 0, 0xFF808080);
        }

        int time = recipe.getCookingTime();

        if (time > 0) {
            int inSeconds = time / 20;
            TranslationTextComponent timeString = new TranslationTextComponent("gui.jei.category.smelting.time.seconds", inSeconds);
            int stringWidth = fontRenderer.width(timeString);
            fontRenderer.draw(matrixStack, timeString, gui.getWidth() - stringWidth, 45, 0xFF808080);
        }
        if (time <= 0) {
            time = 200;
        }
        cachedArrows.getUnchecked(time).draw(matrixStack, 32, 22);
        animatedFire.draw(matrixStack, 10, 24);
    }
}
