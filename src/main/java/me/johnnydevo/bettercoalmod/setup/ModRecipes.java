package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.crafting.recipe.CompressingRecipe;
import me.johnnydevo.bettercoalmod.crafting.recipe.DecompressingRecipe;
import me.johnnydevo.bettercoalmod.crafting.recipe.RecompressingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {
    public static class Types {
        public static final IRecipeType<CompressingRecipe> COMPRESSING = IRecipeType.register(BetterCoalMod.MOD_ID + ModNames.COMPRESSING_RECIPES);
        public static final IRecipeType<DecompressingRecipe> DECOMPRESSING = IRecipeType.register(BetterCoalMod.MOD_ID + ModNames.DECOMPRESSING_RECIPES);
        public static final IRecipeType<RecompressingRecipe> RECOMPRESSING = IRecipeType.register(BetterCoalMod.MOD_ID + ModNames.RECOMPRESSING_RECIPES);

    }

    public static class Serializers {
        public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BetterCoalMod.MOD_ID);

        public static final RegistryObject<IRecipeSerializer<CompressingRecipe>> COMPRESSING = RECIPE_SERIALIZERS.register(ModNames.COMPRESSING_RECIPES, CompressingRecipe.Serializer::new);
        public static final RegistryObject<IRecipeSerializer<DecompressingRecipe>> DECOMPRESSING = RECIPE_SERIALIZERS.register(ModNames.DECOMPRESSING_RECIPES, DecompressingRecipe.Serializer::new);
        public static final RegistryObject<IRecipeSerializer<RecompressingRecipe>> RECOMPRESSING = RECIPE_SERIALIZERS.register(ModNames.RECOMPRESSING_RECIPES, RecompressingRecipe.Serializer::new);
    }

}
