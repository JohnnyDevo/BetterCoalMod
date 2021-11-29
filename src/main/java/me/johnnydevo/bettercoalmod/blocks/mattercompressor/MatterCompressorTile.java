package me.johnnydevo.bettercoalmod.blocks.mattercompressor;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.crafting.recipe.CompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import me.johnnydevo.bettercoalmod.setup.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.FurnaceScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MatterCompressorTile extends TileEntity implements ITickableTileEntity {
    private static List<Item> allowedInputs;

    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private NonNullList<ItemStack> items;

    private int progress = 0;
    private int currentRecipeTime = 0;

    public final IIntArray fields = new IIntArray() {
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
        items = NonNullList.withSize(2, ItemStack.EMPTY);

        ClientWorld minecraft = Minecraft.getInstance().level;
        if (allowedInputs == null && minecraft != null) {
            allowedInputs = new ArrayList<>();
            List<CompressingRecipe> recipes = minecraft.getRecipeManager().getAllRecipesFor(ModRecipes.Types.COMPRESSING);
            for (CompressingRecipe recipe : recipes) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    for (ItemStack itemStack : ingredient.getItems()) {
                        allowedInputs.add(itemStack.getItem());
                    }
                }
            }
        }
    }

    void encodeExtraData(PacketBuffer buffer) {
        buffer.writeBlockPos(getBlockPos());
        buffer.writeByte(fields.getCount());
    }

    public CompressingRecipe getRecipe() {
        if (this.level == null || itemHandler.getStackInSlot(0).isEmpty()) {
            return null;
        }
        for (CompressingRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipes.Types.COMPRESSING)) {
            if (recipe.getIngredients().get(0).getItems()[0].getItem() == itemHandler.getStackInSlot(0).getItem()) {
                return recipe;
            }
        }
        return null;
    }

    private ItemStack getWorkOutput(CompressingRecipe recipe) {
        if (recipe != null) {
            return recipe.getResultItem();
        }
        return ItemStack.EMPTY;
    }

    private void doWork(CompressingRecipe recipe) {
        assert this.level != null;

        ItemStack current = itemHandler.getStackInSlot(1);
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
            setChanged();
        } else {
            finishWork(recipe);
            setChanged();
        }
    }

    private void finishWork(CompressingRecipe recipe) {
        itemHandler.insertItem(1, recipe.getResultItem().copy(), false);
        progress = 0;
        currentRecipeTime = 0;
        itemHandler.extractItem(0, 1, false);
    }

    private void stopWork() {
        progress = 0;
        currentRecipeTime = 0;
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
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0) {
                    if (allowedInputs != null) {
                        for (Item item : allowedInputs) {
                            if (item == stack.getItem()) {
                                return true;
                            }
                        }
                        return false;
                    }
                }
                return true;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!isItemValid(slot, stack)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
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
