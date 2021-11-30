package me.johnnydevo.bettercoalmod.datagen.client;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import net.minecraft.block.WoodType;
import net.minecraft.block.trees.AcaciaTree;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, BetterCoalMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ResourceLocation matterCompressorTop = new ResourceLocation(BetterCoalMod.MOD_ID, "block/" + ModNames.MATTER_COMPRESSOR);
        ResourceLocation matterCompressorSides = new ResourceLocation(BetterCoalMod.MOD_ID, "block/" + ModNames.MATTER_COMPRESSOR + "_sides");
        axisBlock(ModBlocks.MATTER_COMPRESSOR.get(), matterCompressorSides, matterCompressorTop);
        simpleBlock(ModBlocks.MATTER_DECOMPRESSOR.get());
        simpleBlock(ModBlocks.MATTER_RECOMPRESSOR.get());

        simpleBlock(ModBlocks.COMPRESSED_COAL_BLOCK.get());
        simpleBlock(ModBlocks.HIGH_QUALITY_CARBON_BLOCK.get());
    }
}
