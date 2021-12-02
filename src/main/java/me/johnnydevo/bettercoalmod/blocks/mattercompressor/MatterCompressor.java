package me.johnnydevo.bettercoalmod.blocks.mattercompressor;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

public class MatterCompressor extends RotatedPillarBlock {

    public MatterCompressor() {
        super(AbstractBlock.Properties.of(Material.METAL, MaterialColor.DIAMOND)
                .requiresCorrectToolForDrops()
                .strength(10.0F, 1200.0F)
                .sound(SoundType.METAL)
                .harvestLevel(1));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new MatterCompressorTile();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        if (pLevel.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        TileEntity entity = pLevel.getBlockEntity(pPos);
        if (entity instanceof MatterCompressorTile && pPlayer instanceof ServerPlayerEntity) {
            MatterCompressorTile mct = (MatterCompressorTile) entity;
            INamedContainerProvider containerProvider = new INamedContainerProvider() {
                @Override
                public ITextComponent getDisplayName() {
                    return new TranslationTextComponent("block.bettercoalmod.matter_compressor");
                }

                @Nullable
                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new MatterCompressorContainer(i, pLevel, pPos, playerInventory, mct.fields);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) pPlayer, containerProvider, mct::encodeExtraData);
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.hasTileEntity() && state.getBlock() != newState.getBlock()) {
            // drops everything in the inventory
            world.getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                for (int i = 0; i < h.getSlots(); i++) {
                    popResource(world, pos, h.getStackInSlot(i));
                }
            });
            world.removeBlockEntity(pos);
        }
    }
}
