package me.johnnydevo.bettercoalmod.blocks.mattercompressor;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.crafting.recipe.CompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import me.johnnydevo.bettercoalmod.setup.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.inventory.FurnaceScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class MatterCompressorTile extends LockableTileEntity implements ISidedInventory, ITickableTileEntity {
    private NonNullList<ItemStack> items;
    private final LazyOptional<? extends IItemHandler>[] handlers;

    private int progress = 0;
    private int currentRecipeTime = 0;

    private final IIntArray fields = new IIntArray() {
        @Override
        public int get(int pIndex) {
            switch (pIndex) {
                case 0:
                    return progress;
                case 1:
                    return currentRecipeTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0:
                    progress = pValue;
                    break;
                case 1:
                    currentRecipeTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public MatterCompressorTile() {
        super(ModTileEntities.MATTER_COMPRESSOR.get());
        handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
        items = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    void encodeExtraData(PacketBuffer buffer) {
        buffer.writeByte(fields.getCount());
    }

    public CompressingRecipe getRecipe() {
        if (this.level == null || getItem(0).isEmpty()) {
            return null;
        }
        return this.level.getRecipeManager().getRecipeFor(ModRecipes.Types.COMPRESSING, this, this.level).orElse(null);
    }

    private ItemStack getWorkOutput(CompressingRecipe recipe) {
        if (recipe != null) {
            return recipe.assemble(this);
        }
        return ItemStack.EMPTY;
    }

    private void doWork(CompressingRecipe recipe) {
        assert this.level != null;

        ItemStack current = getItem(1);
        ItemStack output = getWorkOutput(recipe);
        currentRecipeTime = recipe.getRecipeTime();

        if (!current.isEmpty()) {
            int newCount = current.getCount() + output.getCount();

            if (!ItemStack.matches(current, output) || newCount > output.getMaxStackSize()) {
                stopWork();
                return;
            }
        }

        if (progress < currentRecipeTime) {
            ++progress;
        } else {
            finishWork(recipe, current, output);
        }
    }

    private void finishWork(CompressingRecipe recipe, ItemStack current, ItemStack output) {
        if (!current.isEmpty()) {
            current.grow(output.getCount());
        } else {
            setItem(1, output);
        }

        progress = 0;
        currentRecipeTime = 0;
        removeItem(0, 1);
    }

    private void stopWork() {
        progress = 0;
        currentRecipeTime = 0;
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return canPlaceItem(pIndex, pItemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return pIndex == 1;
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.bettercoalmod.matter_compressor");
    }

    @Override
    protected Container createMenu(int pId, PlayerInventory pPlayer) {
        return new MatterCompressorContainer(pId, pPlayer, this, this.fields);
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty() && getItem(1).isEmpty();
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return items.get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return ItemStackHelper.removeItem(items, pIndex, pCount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ItemStackHelper.takeItem(items, pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        items.set(pIndex, pStack);
    }

    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        return level != null && level.getBlockEntity(worldPosition) == this &&
                pPlayer.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ()) <= 64;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        this.items = NonNullList.withSize(2, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tags, items);

        progress = tags.getInt("Progress");
        currentRecipeTime = tags.getInt("CurrentRecipeTime");
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        ItemStackHelper.saveAllItems(tags, items);
        tags.putInt("Progress", progress);
        tags.putInt("CurrentRecipeTime", currentRecipeTime);
        return tags;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tags = getUpdateTag();
        ItemStackHelper.saveAllItems(tags, items);
        return new SUpdateTileEntityPacket(worldPosition, 1, tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("Progress", progress);
        tags.putInt("CurrentRecipeTime", currentRecipeTime);
        return tags;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.remove && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP) {
                return this.handlers[0].cast();
            } else if (side == Direction.DOWN) {
                return this.handlers[1].cast();
            } else {
                return this.handlers[2].cast();
            }
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();

        for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
            handler.invalidate();
        }
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) {
            return;
        }

        CompressingRecipe recipe = getRecipe();
        if (recipe != null) {
            doWork(recipe);
        } else {
            stopWork();
        }
    }
}
