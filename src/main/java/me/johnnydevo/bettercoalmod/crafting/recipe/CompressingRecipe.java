package me.johnnydevo.bettercoalmod.crafting.recipe;

import com.google.gson.JsonObject;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class CompressingRecipe extends SingleItemRecipe {
    public CompressingRecipe(ResourceLocation pId, Ingredient pIngredient, ItemStack pResult) {
        super(ModRecipes.Types.COMPRESSING, ModRecipes.Serializers.COMPRESSING.get(), pId, "", pIngredient, pResult);
    }

    @Override
    public boolean matches(IInventory inv, World level) {
        return this.ingredient.test(inv.getItem(0));
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CompressingRecipe> {

        @Override
        public CompressingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            Ingredient ingredient = Ingredient.fromJson(pJson.get("ingredient"));
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(pJson, "result"));
            int count = JSONUtils.getAsInt(pJson, "count");

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);
            return new CompressingRecipe(pRecipeId, ingredient, result);
        }

        @Nullable
        @Override
        public CompressingRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            return new CompressingRecipe(pRecipeId, ingredient, result);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, CompressingRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
        }
    }
}
