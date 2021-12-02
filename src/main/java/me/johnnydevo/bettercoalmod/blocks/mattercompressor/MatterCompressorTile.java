package me.johnnydevo.bettercoalmod.blocks.mattercompressor;

import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorTile;
import me.johnnydevo.bettercoalmod.crafting.recipe.CompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import me.johnnydevo.bettercoalmod.setup.ModTileEntities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
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
    protected ItemStackHandler createHandler() {
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
    protected int getRecipeTime(CompressingRecipe recipe) {
        return recipe.getRecipeTime();
    }

    @Override
    protected float getRecipeExperience(CompressingRecipe recipe) {
        return recipe.getExperience();
    }
}
