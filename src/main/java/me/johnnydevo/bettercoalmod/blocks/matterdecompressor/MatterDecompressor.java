package me.johnnydevo.bettercoalmod.blocks.matterdecompressor;

import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
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

public class MatterDecompressor extends AbstractCompressorBlock {

    public MatterDecompressor() {
        super(Properties.of(Material.METAL, MaterialColor.DIAMOND)
                .requiresCorrectToolForDrops()
                .strength(10.0F, 1200.0F)
                .sound(SoundType.METAL)
                .harvestLevel(1));
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MatterDecompressorTile();
    }

    @Override
    protected void makeGui(TileEntity entity, PlayerEntity player, World level, BlockPos pos) {
        if (entity instanceof MatterDecompressorTile && player instanceof ServerPlayerEntity) {
            MatterDecompressorTile mdt = (MatterDecompressorTile) entity;
            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("block.bettercoalmod.matter_decompressor");
                }

                @Nullable
                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new MatterDecompressorContainer(i, level, pos, playerInventory, mdt.fields);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider, mdt::encodeExtraData);
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
    }
}
