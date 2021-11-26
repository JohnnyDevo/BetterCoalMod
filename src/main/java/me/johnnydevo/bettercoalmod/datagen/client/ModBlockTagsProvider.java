package me.johnnydevo.bettercoalmod.datagen.client;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.setup.BlocksInit;
import me.johnnydevo.bettercoalmod.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator pGenerator, ExistingFileHelper existingFileHelper) {
        super(pGenerator, BetterCoalMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(ModTags.Blocks.COMPRESSED_COAL_BLOCK).add(BlocksInit.COMPRESSED_COAL_BLOCK.get());
        tag(ModTags.Blocks.MATTER_COMPRESSOR).add(BlocksInit.MATTER_COMPRESSOR.get());
        tag(ModTags.Blocks.MATTER_RECOMPRESSOR).add(BlocksInit.MATTER_RECOMPRESSOR.get());
        tag(ModTags.Blocks.MATTER_DECOMPRESSOR).add(BlocksInit.MATTER_DECOMPRESSOR.get());

        tag(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.COMPRESSED_COAL_BLOCK);
    }
}
