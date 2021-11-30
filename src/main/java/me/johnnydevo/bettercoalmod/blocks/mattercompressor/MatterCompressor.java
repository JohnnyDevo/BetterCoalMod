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

import javax.annotation.Nullable;

public class MatterCompressor extends Block {

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
                    return new TranslationTextComponent("screen.bettercoalmod.matter_compressor");
                }

                @Nullable
                @Override
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return new MatterCompressorContainer(i, pLevel, pPos, playerInventory, playerEntity, mct.fields);
                }
            };
            NetworkHooks.openGui((ServerPlayerEntity) pPlayer, containerProvider, mct::encodeExtraData);
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext c) {
        return defaultBlockState().setValue(HorizontalBlock.FACING, c.getHorizontalDirection().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof MatterCompressorTile) {
                InventoryHelper.dropContents(world, pos, (IInventory) tileEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(HorizontalBlock.FACING, rot.rotate(state.getValue(HorizontalBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(HorizontalBlock.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(HorizontalBlock.FACING);
    }
}
