package me.johnnydevo.bettercoalmod.blocks.mattercompressor;

import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class MatterCompressor extends AbstractCompressorBlock {

    public MatterCompressor() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.DIAMOND)
                .requiresCorrectToolForDrops()
                .strength(10.0F, 1200.0F)
                .sound(SoundType.METAL)
                .harvestLevel(1));
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MatterCompressorTile();
    }

    protected void makeGui(TileEntity entity, PlayerEntity player, World level, BlockPos pos) {
        if (entity instanceof MatterCompressorTile && player instanceof ServerPlayerEntity) {
            MatterCompressorTile mct = (MatterCompressorTile) entity;
            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("block.bettercoalmod.matter_compressor");
                }

                @Nullable
                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new MatterCompressorContainer(i, level, pos, playerInventory, mct.fields);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, mct::encodeExtraData);
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
    }
}
