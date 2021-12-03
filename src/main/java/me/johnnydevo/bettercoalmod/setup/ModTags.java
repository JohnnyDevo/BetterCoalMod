package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.ModNames;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {
    public static final class Blocks {
        public static final ITag.INamedTag<Block> COMPRESSED_COAL_BLOCK = BlockTags.bind(new ResourceLocation("forge", "storage_blocks/" + ModNames.COMPRESSED_COAL_BLOCK).toString());
        public static final ITag.INamedTag<Block> HIGH_QUALITY_CARBON_BLOCK = BlockTags.bind(new ResourceLocation("forge", "storage_blocks/" + ModNames.HIGH_QUALITY_CARBON_BLOCK).toString());

        public static final ITag.INamedTag<Block> MATTER_COMPRESSOR = BlockTags.bind(new ResourceLocation(BetterCoalMod.MOD_ID, "machines/" + ModNames.MATTER_COMPRESSOR).toString());
        public static final ITag.INamedTag<Block> MATTER_DECOMPRESSOR = BlockTags.bind(new ResourceLocation(BetterCoalMod.MOD_ID, "machines/" + ModNames.MATTER_DECOMPRESSOR).toString());
        public static final ITag.INamedTag<Block> MATTER_RECOMPRESSOR = BlockTags.bind(new ResourceLocation(BetterCoalMod.MOD_ID, "machines/" + ModNames.MATTER_RECOMPRESSOR).toString()); //proper?
    }

    public static final class Items {
        public static final ITag.INamedTag<Item> COMPRESSED_COAL = ItemTags.bind(new ResourceLocation("forge", "ingots/" + ModNames.COMPRESSED_COAL).toString());
        public static final ITag.INamedTag<Item> HIGH_QUALITY_CARBON = ItemTags.bind(new ResourceLocation("forge", "gems/" + ModNames.HIGH_QUALITY_CARBON).toString());
        public static final ITag.INamedTag<Item> CARBON_ROD = ItemTags.bind(new ResourceLocation(BetterCoalMod.MOD_ID, "rods/" + ModNames.CARBON_ROD).toString());
        public static final ITag.INamedTag<Item> CARBON_FIBER = ItemTags.bind(new ResourceLocation(BetterCoalMod.MOD_ID, "string/" + ModNames.CARBON_FIBER).toString());

        public static final ITag.INamedTag<Item> COMPRESSED_COAL_BLOCK = ItemTags.bind(new ResourceLocation("forge", "storage_blocks/" + ModNames.COMPRESSED_COAL_BLOCK).toString());
        public static final ITag.INamedTag<Item> HIGH_QUALITY_CARBON_BLOCK = ItemTags.bind(new ResourceLocation("forge", "storage_blocks/" + ModNames.HIGH_QUALITY_CARBON_BLOCK).toString());

        public static final ITag.INamedTag<Item> MATTER_COMPRESSOR = ItemTags.bind(new ResourceLocation(BetterCoalMod.MOD_ID, "machines/" + ModNames.MATTER_COMPRESSOR).toString());
        public static final ITag.INamedTag<Item> MATTER_DECOMPRESSOR = ItemTags.bind(new ResourceLocation(BetterCoalMod.MOD_ID, "machines/" + ModNames.MATTER_DECOMPRESSOR).toString());
        public static final ITag.INamedTag<Item> MATTER_RECOMPRESSOR = ItemTags.bind(new ResourceLocation(BetterCoalMod.MOD_ID, "machines/" + ModNames.MATTER_RECOMPRESSOR).toString()); //proper?
    }
}
