package me.johnnydevo.bettercoalmod.crafting.recipe;

import com.google.gson.JsonArray;
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

public class RecompressingRecipe implements IRecipe<IInventory> {
    private ResourceLocation id;
    private ItemStack result;
    private Ingredient[] ingredients;
    private float experience;
    private int recipeTime;

    public RecompressingRecipe(ResourceLocation id, Ingredient[] ingredients, ItemStack result, int recipeTime, float experience) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.experience = experience;
        this.recipeTime = recipeTime;
    }

    @Override
    public boolean matches(IInventory inv, World level) {
        return false;
    }

    @Override
    public ItemStack assemble(IInventory pInv) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.Serializers.RECOMPRESSING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipes.Types.RECOMPRESSING;
    }

    public int getRecipeTime() {
        return recipeTime;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecompressingRecipe> {

        @Override
        public RecompressingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            JsonArray ingredientsArray = JSONUtils.getAsJsonArray(pJson, "ingredients");
            Ingredient[] ingredients = new Ingredient[]{Ingredient.fromJson(ingredientsArray.get(0)), Ingredient.fromJson(ingredientsArray.get(1))};
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(pJson, "result"));
            int resultAmount = JSONUtils.getAsInt(pJson, "resultamount");
            int recipeTime = JSONUtils.getAsInt(pJson, "recipetime");
            float experience = JSONUtils.getAsFloat(pJson, "experience");

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), resultAmount);
            return new RecompressingRecipe(pRecipeId, ingredients, result, recipeTime, experience);
        }

        @Nullable
        @Override
        public RecompressingRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            int ingredientCount = pBuffer.readVarInt();
            //System.out.println("ingredientCount = " + ingredientCount);
            Ingredient[] ingredients = new Ingredient[ingredientCount];
            for (int i = 0; i < ingredientCount; ++i) {
                ingredients[i] = Ingredient.fromNetwork(pBuffer);
                //System.out.println("ingredient " + i + " = " + ingredients[i]);
            }
            ItemStack result = pBuffer.readItem();
            int recipeTime = pBuffer.readVarInt();
            //System.out.println("recipe time " + recipeTime);
            float experience = pBuffer.readFloat();
            return new RecompressingRecipe(pRecipeId, ingredients, result, recipeTime, experience);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, RecompressingRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.ingredients.length);
            pRecipe.ingredients[0].toNetwork(pBuffer);
            pRecipe.ingredients[1].toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.recipeTime);
            pBuffer.writeFloat(pRecipe.experience);
        }
    }
}
