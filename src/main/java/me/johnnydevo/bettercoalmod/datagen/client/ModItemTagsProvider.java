package me.johnnydevo.bettercoalmod.datagen.client;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.setup.ItemsInit;
import me.johnnydevo.bettercoalmod.setup.ModTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(DataGenerator gen, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(gen, blockTagProvider, BetterCoalMod.MOD_ID, existingFileHelper);
    }

    @Override
    public void addTags() {
        copy(ModTags.Blocks.COMPRESSED_COAL_BLOCK, ModTags.Items.COMPRESSED_COAL_BLOCK);
        copy(ModTags.Blocks.MATTER_COMPRESSOR, ModTags.Items.MATTER_COMPRESSOR);
        copy(ModTags.Blocks.MATTER_DECOMPRESSOR, ModTags.Items.MATTER_DECOMPRESSOR);
        copy(ModTags.Blocks.MATTER_RECOMPRESSOR, ModTags.Items.MATTER_RECOMPRESSOR);

        tag(ModTags.Items.COMPRESSED_COAL).add(ItemsInit.COMPRESSED_COAL.get());
        tag(Tags.Items.INGOTS).addTag(ModTags.Items.COMPRESSED_COAL);
    }
}
