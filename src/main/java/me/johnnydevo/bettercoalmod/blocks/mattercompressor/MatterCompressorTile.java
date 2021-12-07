package me.johnnydevo.bettercoalmod.blocks.mattercompressor;

import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorTile;
import me.johnnydevo.bettercoalmod.crafting.recipe.CompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import me.johnnydevo.bettercoalmod.setup.ModTileEntities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MatterCompressorTile extends AbstractCompressorTile<CompressingRecipe> {
    private static List<Item> allowedInputs;

    public MatterCompressorTile() {
        super(ModTileEntities.MATTER_COMPRESSOR.get());
        recipeType = ModRecipes.Types.COMPRESSING;
        if (allowedInputs == null) {
            allowedInputs = new ArrayList<>();
            populateInputs(allowedInputs);
            if (allowedInputs.size() == 0) {
                allowedInputs = null;
            }
        }
        outputSlot = 1;
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
    }

    @Override
    protected ItemStackHandler[] createHandlers() {
        ItemStackHandler[] retval = new ItemStackHandler[2];
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
                return handlers[1].cast();
            }
        }
        return super.getCapability(cap, side);
    }

    @Override
    protected int getRecipeTime(CompressingRecipe recipe) {
        return recipe.getRecipeTime();
    }

    @Override
    protected float getRecipeExperience(CompressingRecipe recipe) {
        return recipe.getExperience();
    }
}
