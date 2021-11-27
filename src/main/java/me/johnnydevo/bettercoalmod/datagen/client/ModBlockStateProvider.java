package me.johnnydevo.bettercoalmod.datagen.client;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, BetterCoalMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.MATTER_COMPRESSOR.get());
        simpleBlock(ModBlocks.MATTER_DECOMPRESSOR.get());
        simpleBlock(ModBlocks.MATTER_RECOMPRESSOR.get());

        simpleBlock(ModBlocks.COMPRESSED_COAL_BLOCK.get());
        simpleBlock(ModBlocks.HIGH_QUALITY_CARBON_BLOCK.get());
    }
}
