package me.johnnydevo.bettercoalmod.datagen.client.recipebuilders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class AbstractBetterCoalRecipeBuilder {
    protected Item result;
    protected Ingredient[] ingredients;
    protected float experience;
    protected int recipeTime;
    protected Advancement.Builder advancement = Advancement.Builder.advancement();
    protected String group;
    protected IRecipeSerializer<?> serializer;
    protected int resultAmt;

    public AbstractBetterCoalRecipeBuilder unlockedBy(String cName, ICriterionInstance criterion) {
        this.advancement.addCriterion(cName, criterion);
        return this;
    }

    public void save(Consumer<IFinishedRecipe> recipeConsumer) {
        this.save(recipeConsumer, Registry.ITEM.getKey(this.result) + this.getRecipeNameModifier());
    }

    public abstract String getRecipeNameModifier();

    public void save(Consumer<IFinishedRecipe> recipeConsumer, String s) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        ResourceLocation resourcelocation1 = new ResourceLocation(s);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        } else {
            this.save(recipeConsumer, resourcelocation1);
        }
    }

    public void save(Consumer<IFinishedRecipe> recipeConsumer, ResourceLocation resourceLocation) {
        this.ensureValid(resourceLocation);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation)).rewards(AdvancementRewards.Builder.recipe(resourceLocation)).requirements(IRequirementsStrategy.OR);
        recipeConsumer.accept(new AbstractBetterCoalRecipeBuilder.Result(
                resourceLocation,
                group == null ? "" : group,
                ingredients,
                result,
                experience,
                recipeTime,
                resultAmt,
                advancement,
                new ResourceLocation(resourceLocation.getNamespace(),
                        "recipes/" + result.getItemCategory().getRecipeFolderName() + "/" + resourceLocation.getPath()),
                serializer));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient[] ingredients;
        private final Item result;
        private final float experience;
        private final int recipeTime;
        private final int resultAmt;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<?> serializer;

        public Result(ResourceLocation id, String group, Ingredient[] ingredients, Item result, float experience, int recipeTime, int resultAmt, Advancement.Builder advancement, ResourceLocation advancementId, IRecipeSerializer<?> serializer) {
            this.id = id;
            this.group = group;
            this.ingredients = ingredients;
            this.result = result;
            this.experience = experience;
            this.recipeTime = recipeTime;
            this.resultAmt = resultAmt;
            this.advancement = advancement;
            this.advancementId = advancementId;
            this.serializer = serializer;
        }

        public void serializeRecipeData(JsonObject pJson) {
            if (!group.isEmpty()) {
                pJson.addProperty("group", group);
            }

            JsonArray ingredientsElement = new JsonArray();
            for (Ingredient ingredient : ingredients) {
                ingredientsElement.add(ingredient.toJson());
            }

            pJson.add("ingredients", ingredientsElement);

            pJson.addProperty("result", Registry.ITEM.getKey(result).toString());
            pJson.addProperty("resultamount", resultAmt);
            pJson.addProperty("experience", experience);
            pJson.addProperty("recipetime", recipeTime);
        }

        public IRecipeSerializer<?> getType() {
            return serializer;
        }

        public ResourceLocation getId() {
            return id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}