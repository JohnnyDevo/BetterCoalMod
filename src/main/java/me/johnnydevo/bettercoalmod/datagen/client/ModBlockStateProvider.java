package me.johnnydevo.bettercoalmod.datagen.client;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.setup.BlocksInit;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, BetterCoalMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(BlocksInit.MATTER_COMPRESSOR.get());
        simpleBlock(BlocksInit.MATTER_DECOMPRESSOR.get());
        simpleBlock(BlocksInit.MATTER_RECOMPRESSOR.get());
        simpleBlock(BlocksInit.COMPRESSED_COAL_BLOCK.get());
    }
}
