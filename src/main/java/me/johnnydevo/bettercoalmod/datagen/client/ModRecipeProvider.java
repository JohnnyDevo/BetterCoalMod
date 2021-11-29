package me.johnnydevo.bettercoalmod.datagen.client;

import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import me.johnnydevo.bettercoalmod.setup.ModItems;
import me.johnnydevo.bettercoalmod.setup.ModTags;
import mezz.jei.api.helpers.IJeiHelpers;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
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

        //ShapelessRecipeBuilder.shapeless(ModItems.HIGH_QUALITY_CARBON.get(), 9)
        //        .requires(ModTags.Items.HIGH_QUALITY_CARBON_BLOCK)
        //        .unlockedBy("has_item", has(ModItems.HIGH_QUALITY_CARBON.get()))
        //        .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.HIGH_QUALITY_CARBON_BLOCK.get())
                .define('#', ModTags.Items.HIGH_QUALITY_CARBON)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_item", has(ModItems.HIGH_QUALITY_CARBON.get()))
                .save(consumer);

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

        CompressingRecipeBuilder.compressing(ModItems.HIGH_QUALITY_CARBON.get(), Ingredient.of(ModTags.Items.COMPRESSED_COAL), 0.2f, 100, 2)
                .unlockedBy("has_item", has(ModTags.Items.COMPRESSED_COAL))
                .save(consumer);

    }
}
