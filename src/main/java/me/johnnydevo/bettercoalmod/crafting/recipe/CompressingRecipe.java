package me.johnnydevo.bettercoalmod.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SingleItemRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class CompressingRecipe extends SingleItemRecipe {
    private final int recipeTime;
    private final float experience;

    public CompressingRecipe(ResourceLocation pId, Ingredient pIngredient, ItemStack pResult, int recipeTime, float experience) {
        super(ModRecipes.Types.COMPRESSING, ModRecipes.Serializers.COMPRESSING.get(), pId, "", pIngredient, pResult);
        this.recipeTime = recipeTime;
        this.experience = experience;
    }

    public float getExperience() {
        return experience;
    }

    @Override
    public boolean matches(IInventory inv, World level) {
        return this.ingredient.test(inv.getItem(0));
    }

    public int getRecipeTime() {
        return recipeTime;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CompressingRecipe> {

        @Override
        public CompressingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            JsonArray ingredientsArray = JSONUtils.getAsJsonArray(pJson, "ingredients");
            Ingredient ingredient = Ingredient.fromJson(ingredientsArray.get(0));
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(pJson, "result"));
            int resultAmount = JSONUtils.getAsInt(pJson, "resultamount");
            int recipeTime = JSONUtils.getAsInt(pJson, "recipetime");
            float experience = JSONUtils.getAsFloat(pJson, "experience");

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), resultAmount);
            return new CompressingRecipe(pRecipeId, ingredient, result, recipeTime, experience);
        }

        @Nullable
        @Override
        public CompressingRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            ItemStack result = pBuffer.readItem();
            int recipeTime = pBuffer.readVarInt();
            float experience = pBuffer.readFloat();
            return new CompressingRecipe(pRecipeId, ingredient, result, recipeTime, experience);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, CompressingRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.recipeTime);
            pBuffer.writeFloat(pRecipe.experience);
        }
    }
}
