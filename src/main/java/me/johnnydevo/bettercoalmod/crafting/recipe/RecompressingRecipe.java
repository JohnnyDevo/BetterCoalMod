package me.johnnydevo.bettercoalmod.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class RecompressingRecipe implements IRecipe<IInventory> {
    private ResourceLocation id;
    private ItemStack result;
    private NonNullList<Ingredient> ingredients;
    private float experience;
    private int recipeTime;

    public RecompressingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, int recipeTime, float experience) {
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

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ret = NonNullList.create();
        ret.addAll(ingredients);
        return ret;
    }

    public float getExperience() {
        return experience;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecompressingRecipe> {

        @Override
        public RecompressingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            JsonArray ingredientsArray = JSONUtils.getAsJsonArray(pJson, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (JsonElement e : ingredientsArray) {
                ingredients.add(Ingredient.fromJson(e));
            }
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
            NonNullList<Ingredient> ingredients = NonNullList.create();
            for (int i = 0; i < ingredientCount; ++i) {
                ingredients.add(Ingredient.fromNetwork(pBuffer));
            }
            ItemStack result = pBuffer.readItem();
            int recipeTime = pBuffer.readVarInt();
            float experience = pBuffer.readFloat();
            return new RecompressingRecipe(pRecipeId, ingredients, result, recipeTime, experience);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, RecompressingRecipe pRecipe) {
            pBuffer.writeVarInt(pRecipe.ingredients.size());
            for (Ingredient ingredient : pRecipe.ingredients) {
                ingredient.toNetwork(pBuffer);
            }
            pBuffer.writeItem(pRecipe.result);
            pBuffer.writeVarInt(pRecipe.recipeTime);
            pBuffer.writeFloat(pRecipe.experience);
        }
    }
}
