package me.johnnydevo.bettercoalmod.blocks.matterdecompressor;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class MatterDecompressor extends Block {

    public MatterDecompressor() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.DIAMOND)
                .requiresCorrectToolForDrops()
                .strength(10.0F, 1200.0F)
                .sound(SoundType.METAL)
                .harvestLevel(1));
    }

}
