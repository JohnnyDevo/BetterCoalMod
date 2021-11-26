package me.johnnydevo.bettercoalmod.setup;

import me.johnnydevo.bettercoalmod.ModNames;
import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.blocks.mattercompressor.MatterCompressor;
import me.johnnydevo.bettercoalmod.blocks.matterdecompressor.MatterDecompressor;
import me.johnnydevo.bettercoalmod.blocks.matterrecompressor.MatterRecompressor;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlocksInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BetterCoalMod.MOD_ID);

    public static final RegistryObject<Block> COMPRESSED_COAL_BLOCK = BLOCKS.register(ModNames.COMPRESSED_COAL_BLOCK, () -> new Block(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(5.0F, 6.0F)));

    public static final RegistryObject<MatterCompressor> MATTER_COMPRESSOR = BLOCKS.register(ModNames.MATTER_COMPRESSOR, MatterCompressor::new);
    public static final RegistryObject<MatterDecompressor> MATTER_DECOMPRESSOR = BLOCKS.register(ModNames.MATTER_DECOMPRESSOR, MatterDecompressor::new);
    public static final RegistryObject<MatterRecompressor> MATTER_RECOMPRESSOR = BLOCKS.register(ModNames.MATTER_RECOMPRESSOR, MatterRecompressor::new);
}
