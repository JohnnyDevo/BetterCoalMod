package me.johnnydevo.bettercoalmod.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class DecompressingRecipe extends AbstractCookingRecipe {

    public DecompressingRecipe(ResourceLocation pId, Ingredient pIngredient, ItemStack pResult, float experience, int cookingTime) {
        super(ModRecipes.Types.DECOMPRESSING, pId,"", pIngredient, pResult, experience, cookingTime);
    }

    @Override
    public boolean matches(IInventory inv, World level) {
        return ingredient.test(inv.getItem(0));
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.Serializers.DECOMPRESSING.get();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<DecompressingRecipe> {

        @Override
        public DecompressingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            JsonArray ingredientsArray = JSONUtils.getAsJsonArray(pJson, "ingredients");
            Ingredient ingredient = Ingredient.fromJson(ingredientsArray.get(0));
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(pJson, "result"));
            int resultAmount = JSONUtils.getAsInt(pJson, "resultamount");
            int recipeTime = JSONUtils.getAsInt(pJson, "cookingtime");
            int experience = JSONUtils.getAsInt(pJson, "experience");

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), resultAmount);
            return new DecompressingRecipe(pRecipeId, ingredient, result, experience, recipeTime);
        }

        @Nullable
        @Override
        public DecompressingRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int recipeTime = pBuffer.readVarInt();
            float experience = pBuffer.readFloat();
            return new DecompressingRecipe(pRecipeId, ingredient, result, experience, recipeTime);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, DecompressingRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.cookingTime);
            pBuffer.writeFloat(pRecipe.experience);
        }
    }
}
