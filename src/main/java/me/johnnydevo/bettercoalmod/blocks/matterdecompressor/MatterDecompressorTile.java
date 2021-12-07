package me.johnnydevo.bettercoalmod.blocks.matterdecompressor;

import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorTile;
import me.johnnydevo.bettercoalmod.crafting.recipe.DecompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import me.johnnydevo.bettercoalmod.setup.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MatterDecompressorTile extends AbstractCompressorTile<DecompressingRecipe> implements ITickableTileEntity {
    private static List<Item> allowedInputs;

    private int burnProgress = 0;
    private int currentBurnTime = 0;

    public MatterDecompressorTile() {
        super(ModTileEntities.MATTER_DECOMPRESSOR.get());
        recipeType = ModRecipes.Types.DECOMPRESSING;
        if (allowedInputs == null) {
            allowedInputs = new ArrayList<>();
            populateInputs(allowedInputs);
            if (allowedInputs.size() == 0) {
                allowedInputs = null;
            }
        }
        outputSlot = 2;
        inputSlots = new int[]{0};
    }

    @Override
    protected IIntArray makeFields() {
        return new IIntArray() {
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
    }

    @Override
    protected ItemStackHandler[] createHandlers() {
        ItemStackHandler[] retval = new ItemStackHandler[3];
        retval[0] = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (allowedInputs != null) {
                    for (Item item : allowedInputs) {
                        if (item == stack.getItem()) {
                            return true;
                        }
                    }
                    return false;
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
        retval[1] = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (stack.isEmpty()) {
                    return false;
                }
                Integer burnTime = stack.getBurnTime(IRecipeType.SMELTING);
                if (burnTime == -1) {
                    burnTime = FurnaceTileEntity.getFuel().get(stack.getItem());
                }
                return (burnTime != null && burnTime > 0);
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
        retval[2] = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
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
        return retval;
    }

    @Override
    public <C> LazyOptional<C> getCapability(Capability<C> cap, Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.UP) {
                return handlers[0].cast();
            } else if (side == Direction.DOWN) {
                return handlers[2].cast();
            } else if (side == Direction.NORTH || side == Direction.SOUTH || side == Direction.EAST || side == Direction.WEST) {
                return handlers[1].cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected int getRecipeTime(DecompressingRecipe recipe) {
        return recipe.getCookingTime();
    }

    @Override
    protected float getRecipeExperience(DecompressingRecipe recipe) {
        return recipe.getExperience();
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

    private boolean handleFuel(DecompressingRecipe recipe) {
        boolean consumeFuel = true;
        if (burnProgress < currentBurnTime) {
            burnProgress++;
            consumeFuel = false;
        }
        if (recipe != null) {
            if (consumeFuel) {
                ItemStack fuel = itemHandlers[1].getStackInSlot(0);
                if (fuel.isEmpty()) {
                    resetBurn();
                    return false;
                }
                Integer burnTime = fuel.getBurnTime(IRecipeType.SMELTING);
                if (burnTime == -1) {
                    burnTime = FurnaceTileEntity.getFuel().get(fuel.getItem());
                }
                if (burnTime != null && burnTime > 0) {
                    itemHandlers[1].extractItem(0, 1, false);
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
        setChanged();
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);
        burnProgress = tags.getInt("BurnProgress");
        currentBurnTime = tags.getInt("CurrentBurnTime");
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);
        tags.putInt("BurnProgress", burnProgress);
        tags.putInt("CurrentBurnTime", currentBurnTime);
        return tags;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("BurnProgress", burnProgress);
        tags.putInt("CurrentBurnTime", currentBurnTime);
        return tags;
    }
}
