package me.johnnydevo.bettercoalmod.jei;

import com.google.common.collect.ImmutableSet;
import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.crafting.recipe.CompressingRecipe;
import me.johnnydevo.bettercoalmod.crafting.recipe.DecompressingRecipe;
import me.johnnydevo.bettercoalmod.crafting.recipe.RecompressingRecipe;
import me.johnnydevo.bettercoalmod.jei.category.CompressingCategory;
import me.johnnydevo.bettercoalmod.jei.category.DecompressingCategory;
import me.johnnydevo.bettercoalmod.jei.category.RecompressingCategory;
import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

@JeiPlugin
public class BetterCoalJeiPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID = new ResourceLocation(BetterCoalMod.MOD_ID, ModNames.JEI_PLUGIN);

    public static CompressingCategory compressingCategory;
    public static DecompressingCategory decompressingCategory;
    public static RecompressingCategory recompressingCategory;


    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        compressingCategory = new CompressingCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(compressingCategory);

        decompressingCategory = new DecompressingCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(decompressingCategory);

        recompressingCategory = new RecompressingCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(recompressingCategory);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MATTER_COMPRESSOR.get()), compressingCategory.getUid());
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MATTER_DECOMPRESSOR.get()), decompressingCategory.getUid());
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.MATTER_RECOMPRESSOR.get()), recompressingCategory.getUid());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        Set<CompressingRecipe> compressingRecipes = ImmutableSet.copyOf(manager.getAllRecipesFor(ModRecipes.Types.COMPRESSING));
        registration.addRecipes(compressingRecipes, compressingCategory.getUid());

        Set<DecompressingRecipe> decompressingRecipes = ImmutableSet.copyOf(manager.getAllRecipesFor(ModRecipes.Types.DECOMPRESSING));
        registration.addRecipes(decompressingRecipes, decompressingCategory.getUid());

        Set<RecompressingRecipe> recompressingRecipes = ImmutableSet.copyOf(manager.getAllRecipesFor(ModRecipes.Types.RECOMPRESSING));
        registration.addRecipes(recompressingRecipes, recompressingCategory.getUid());
    }
}
