package me.johnnydevo.bettercoalmod.datagen.client;

import me.johnnydevo.bettercoalmod.datagen.client.recipebuilders.CompressingRecipeBuilder;
import me.johnnydevo.bettercoalmod.datagen.client.recipebuilders.DecompressingRecipeBuilder;
import me.johnnydevo.bettercoalmod.datagen.client.recipebuilders.RecompressingRecipeBuilder;
import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import me.johnnydevo.bettercoalmod.setup.ModItems;
import me.johnnydevo.bettercoalmod.setup.ModTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import java.util.function.Consumer;

public class ModRecipeProvider extends ForgeRecipeProvider {
    public ModRecipeProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        buildStorageRecipes(consumer);
        buildMachineRecipes(consumer);
        buildToolRecipes(consumer);
        buildCompressingRecipes(consumer);
    }

    private void buildStorageRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(ModItems.COMPRESSED_COAL.get(), 9)
                .requires(ModTags.Items.COMPRESSED_COAL_BLOCK)
                .unlockedBy("has_item", has(ModItems.COMPRESSED_COAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.COMPRESSED_COAL_BLOCK.get())
                .define('#', ModTags.Items.COMPRESSED_COAL)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_item", has(ModItems.COMPRESSED_COAL.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModItems.HIGH_QUALITY_CARBON.get(), 9)
                .requires(ModTags.Items.HIGH_QUALITY_CARBON_BLOCK)
                .unlockedBy("has_item", has(ModItems.HIGH_QUALITY_CARBON.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.HIGH_QUALITY_CARBON_BLOCK.get())
                .define('#', ModTags.Items.HIGH_QUALITY_CARBON)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_item", has(ModItems.HIGH_QUALITY_CARBON.get()))
                .save(consumer);
    }

    private void buildMachineRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModBlocks.MATTER_COMPRESSOR.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('C', ModTags.Items.COMPRESSED_COAL_BLOCK)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .define('c', ModTags.Items.COMPRESSED_COAL)
                .define('f', Items.FURNACE)
                .pattern("dCd")
                .pattern("DfD")
                .pattern("cdc")
                .unlockedBy("has_item", has(ModItems.COMPRESSED_COAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.MATTER_DECOMPRESSOR.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .define('f', Items.FURNACE)
                .pattern("ddd")
                .pattern("DfD")
                .pattern("ddd")
                .unlockedBy("has_item", has(Items.DIAMOND))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.MATTER_RECOMPRESSOR.get())
                .define('D', Tags.Items.STORAGE_BLOCKS_DIAMOND)
                .define('C', ModTags.Items.HIGH_QUALITY_CARBON_BLOCK)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .define('c', ModTags.Items.HIGH_QUALITY_CARBON)
                .define('f', Items.FURNACE)
                .pattern("dCd")
                .pattern("DfD")
                .pattern("cdc")
                .unlockedBy("has_item", has(ModItems.HIGH_QUALITY_CARBON.get()))
                .save(consumer);
    }

    private void buildToolRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModItems.CARBON_AXE.get())
                .define('R', ModTags.Items.CARBON_ROD)
                .define('I', ModTags.Items.COMPRESSED_COAL)
                .pattern("II ")
                .pattern("IR ")
                .pattern(" R ")
                .unlockedBy("has_item", has(ModItems.COMPRESSED_COAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.CARBON_BOW.get())
                .define('F', ModTags.Items.CARBON_FIBER)
                .define('R', ModTags.Items.CARBON_ROD)
                .pattern(" RF")
                .pattern("R F")
                .pattern(" RF")
                .unlockedBy("has_item", has(ModItems.COMPRESSED_COAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.CARBON_HOE.get())
                .define('R', ModTags.Items.CARBON_ROD)
                .define('I', ModTags.Items.COMPRESSED_COAL)
                .pattern("II ")
                .pattern(" R ")
                .pattern(" R ")
                .unlockedBy("has_item", has(ModItems.COMPRESSED_COAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.CARBON_PICKAXE.get())
                .define('R', ModTags.Items.CARBON_ROD)
                .define('I', ModTags.Items.COMPRESSED_COAL)
                .pattern("III")
                .pattern(" R ")
                .pattern(" R ")
                .unlockedBy("has_item", has(ModItems.COMPRESSED_COAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.CARBON_SHOVEL.get())
                .define('R', ModTags.Items.CARBON_ROD)
                .define('I', ModTags.Items.COMPRESSED_COAL)
                .pattern(" I ")
                .pattern(" R ")
                .pattern(" R ")
                .unlockedBy("has_item", has(ModItems.COMPRESSED_COAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.CARBON_SWORD.get())
                .define('R', ModTags.Items.CARBON_ROD)
                .define('I', ModTags.Items.COMPRESSED_COAL)
                .pattern(" I ")
                .pattern(" I ")
                .pattern(" R ")
                .unlockedBy("has_item", has(ModItems.COMPRESSED_COAL.get()))
                .save(consumer);
    }

    private void buildCompressingRecipes(Consumer<IFinishedRecipe> consumer) {
        //compressing
        CompressingRecipeBuilder.build(Items.DIAMOND, Ingredient.of(ItemTags.COALS), 0.2f, 800, 1)
                .unlockedBy("has_item", has(ItemTags.COALS))
                .save(consumer);

        CompressingRecipeBuilder.build(Items.GOLD_INGOT, Ingredient.of(Items.GOLDEN_CARROT), 0.1f, 200, 1)
                .unlockedBy("has_item", has(Items.GOLDEN_CARROT))
                .save(consumer);

        CompressingRecipeBuilder.build(Items.QUARTZ, Ingredient.of(Items.NETHERRACK), 0.0f, 800, 1)
                .unlockedBy("has_item", has(Items.NETHERRACK))
                .save(consumer);

        //decompressing
        DecompressingRecipeBuilder.build(ModItems.HIGH_QUALITY_CARBON.get(), Ingredient.of(Tags.Items.GEMS_DIAMOND), 0.2f, 200, 1)
                .unlockedBy("has_item", has(Tags.Items.GEMS_DIAMOND))
                .save(consumer);

        DecompressingRecipeBuilder.build(Blocks.SAND, Ingredient.of(Tags.Items.GLASS), 0.0f, 200, 1)
                .unlockedBy("has_item", has(Tags.Items.GLASS))
                .save(consumer);

        DecompressingRecipeBuilder.build(Items.BONE_MEAL, Ingredient.of(Items.BONE), 0.1f, 100, 4)
                .unlockedBy("has_item", has(Items.BONE))
                .save(consumer);

        //recompressing
        RecompressingRecipeBuilder.build(ModItems.COMPRESSED_COAL.get(), Ingredient.of(ModTags.Items.HIGH_QUALITY_CARBON), Ingredient.of(ModTags.Items.HIGH_QUALITY_CARBON),0.4f, 400, 2)
                .unlockedBy("has_item", has(ModTags.Items.HIGH_QUALITY_CARBON))
                .save(consumer);

        RecompressingRecipeBuilder.build(ModItems.CARBON_FIBER.get(), Ingredient.of(ModTags.Items.HIGH_QUALITY_CARBON), Ingredient.of(Items.STRING),0.2f, 200, 3)
                .unlockedBy("has_item", has(ModTags.Items.HIGH_QUALITY_CARBON))
                .save(consumer);

        RecompressingRecipeBuilder.build(ModItems.CARBON_ROD.get(), Ingredient.of(ModTags.Items.HIGH_QUALITY_CARBON), Ingredient.of(Items.STICK),0.2f, 200, 4)
                .unlockedBy("has_item", has(ModTags.Items.HIGH_QUALITY_CARBON))
                .save(consumer);
    }
}
