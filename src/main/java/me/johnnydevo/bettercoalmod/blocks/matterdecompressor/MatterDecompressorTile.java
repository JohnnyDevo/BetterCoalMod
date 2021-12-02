package me.johnnydevo.bettercoalmod.blocks.matterdecompressor;

import me.johnnydevo.bettercoalmod.crafting.recipe.DecompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import me.johnnydevo.bettercoalmod.setup.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MatterDecompressorTile extends TileEntity implements ITickableTileEntity {
    private static List<Item> allowedInputs;

    private ItemStackHandler itemHandler = createHandler();
    private LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    private int progress = 0;
    private int currentRecipeTime = 0;
    private int burnProgress = 0;
    private int currentBurnTime = 0;
    private float savedExp = 0.0F;

    public final IIntArray fields = new IIntArray() {
        @Override
        public int get(int pIndex) {
            switch (pIndex) {
                case 0:
                    return progress;
                case 1:
                    return currentRecipeTime;
                case 2:
                    return burnProgress;
                case 3:
                    return currentBurnTime;
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
                    break;
                case 2:
                    burnProgress = pValue;
                    break;
                case 3:
                    currentBurnTime = pValue;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public MatterDecompressorTile() {
        super(ModTileEntities.MATTER_DECOMPRESSOR.get());

        ClientWorld minecraft = Minecraft.getInstance().level;
        if (allowedInputs == null && minecraft != null) {
            allowedInputs = new ArrayList<>();
            List<DecompressingRecipe> recipes = minecraft.getRecipeManager().getAllRecipesFor(ModRecipes.Types.DECOMPRESSING);
            for (DecompressingRecipe recipe : recipes) {
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

    public DecompressingRecipe getRecipe() {
        if (this.level == null || itemHandler.getStackInSlot(0).isEmpty()) {
            return null;
        }
        for (DecompressingRecipe recipe : level.getRecipeManager().getAllRecipesFor(ModRecipes.Types.DECOMPRESSING)) {
            if (recipe.getIngredients().get(0).getItems()[0].getItem() == itemHandler.getStackInSlot(0).getItem()) {
                return recipe;
            }
        }
        return null;
    }

    private ItemStack getWorkOutput(DecompressingRecipe recipe) {
        if (recipe != null) {
            return recipe.getResultItem();
        }
        return ItemStack.EMPTY;
    }

    private void doWork(DecompressingRecipe recipe) {
        assert this.level != null;

        ItemStack current = itemHandler.getStackInSlot(2);
        ItemStack output = getWorkOutput(recipe);
        currentRecipeTime = recipe.getCookingTime();

        if (!current.isEmpty()) {
            int newCount = current.getCount() + output.getCount();

            if (!current.sameItem(output) || newCount > output.getMaxStackSize()) {
                stopWork();
                return;
            }
        }

        if (++progress >= currentRecipeTime) {
            finishWork(recipe);
            setChanged();
        }
    }

    private void finishWork(DecompressingRecipe recipe) {
        itemHandler.insertItem(2, recipe.getResultItem().copy(), false);
        progress = 0;
        currentRecipeTime = 0;
        savedExp += recipe.getExperience();
        itemHandler.extractItem(0, 1, false);
    }

    private void stopWork() {
        progress = 0;
        currentRecipeTime = 0;
    }

    private boolean handleFuel(DecompressingRecipe recipe) {
        boolean consumeFuel = true;
        if (burnProgress < currentBurnTime) {
            burnProgress++;
            consumeFuel = false;
        }
        if (recipe != null) {
            if (consumeFuel) {
                ItemStack fuel = itemHandler.getStackInSlot(1);
                if (fuel.isEmpty()) {
                    resetBurn();
                    return false;
                }
                Integer burnTime = fuel.getBurnTime(IRecipeType.SMELTING);
                if (burnTime == -1) {
                    burnTime = FurnaceTileEntity.getFuel().get(fuel.getItem());
                }
                if (burnTime != null && burnTime > 0) {
                    itemHandler.extractItem(1, 1, false);
                    currentBurnTime = burnTime;
                    burnProgress = 0;
                } else {
                    resetBurn();
                    return false;
                }
            }
            return true;
        } else {
            if (burnProgress >= currentBurnTime) {
                resetBurn();
            }
        }
        return false;
    }

    public void resetBurn() {
        currentBurnTime = 0;
        burnProgress = 0;
        System.out.println("burn reset");
        setChanged();
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);

        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tags, items);
        itemHandler.insertItem(0, items.get(0), false);
        itemHandler.insertItem(1, items.get(1), false);
        itemHandler.insertItem(2, items.get(2), false);

        progress = tags.getInt("Progress");
        currentRecipeTime = tags.getInt("CurrentRecipeTime");
        burnProgress = tags.getInt("BurnProgress");
        currentBurnTime = tags.getInt("CurrentBurnTime");
        savedExp = tags.getFloat("savedExp");
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);

        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
        items.set(0, itemHandler.getStackInSlot(0));
        items.set(1, itemHandler.getStackInSlot(1));
        items.set(2, itemHandler.getStackInSlot(2));
        ItemStackHelper.saveAllItems(tags, items);

        tags.putInt("Progress", progress);
        tags.putInt("CurrentRecipeTime", currentRecipeTime);
        tags.putInt("BurnProgress", burnProgress);
        tags.putInt("CurrentBurnTime", currentBurnTime);
        tags.putFloat("savedExp", savedExp);
        return tags;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tags = getUpdateTag();

        NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
        items.set(0, itemHandler.getStackInSlot(0));
        items.set(1, itemHandler.getStackInSlot(1));
        items.set(2, itemHandler.getStackInSlot(2));
        ItemStackHelper.saveAllItems(tags, items);

        return new SUpdateTileEntityPacket(worldPosition, 1, tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("Progress", progress);
        tags.putInt("CurrentRecipeTime", currentRecipeTime);
        tags.putInt("BurnProgress", burnProgress);
        tags.putInt("CurrentBurnTime", currentBurnTime);
        tags.putFloat("savedExp", savedExp);
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
        return new ItemStackHandler(3) {
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
                if (slot == 1) {
                    if (stack.isEmpty()) {
                        return false;
                    }
                    Integer burnTime = stack.getBurnTime(IRecipeType.SMELTING);
                    if (burnTime == -1) {
                        burnTime = FurnaceTileEntity.getFuel().get(stack.getItem());
                    }
                    return (burnTime != null && burnTime > 0);
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

    public void awardExp(PlayerEntity player) {
        int reward = (int)savedExp;
        player.giveExperiencePoints(reward);
        savedExp -= reward;
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide) {
            return;
        }

        DecompressingRecipe recipe = getRecipe();
        if (handleFuel(recipe)) {
            doWork(recipe);
        } else {
            stopWork();
        }
    }
}
