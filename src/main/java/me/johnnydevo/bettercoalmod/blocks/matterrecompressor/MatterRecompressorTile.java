package me.johnnydevo.bettercoalmod.blocks.matterrecompressor;

import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorTile;
import me.johnnydevo.bettercoalmod.crafting.recipe.RecompressingRecipe;
import me.johnnydevo.bettercoalmod.setup.ModRecipes;
import me.johnnydevo.bettercoalmod.setup.ModTileEntities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class MatterRecompressorTile extends AbstractCompressorTile<RecompressingRecipe> implements ITickableTileEntity {
    private static List<Item> allowedInputs;

    public MatterRecompressorTile() {
        super(ModTileEntities.MATTER_RECOMPRESSOR.get());
        recipeType = ModRecipes.Types.RECOMPRESSING;
        if (allowedInputs == null) {
            allowedInputs = new ArrayList<>();
            populateInputs(allowedInputs);
            if (allowedInputs.size() == 0) {
                allowedInputs = null;
            }
        }
        outputSlot = 2;
        inputSlots = new int[]{0, 1};
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
        return new ItemStackHandler(3) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 0 || slot == 1) {
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
    protected int getRecipeTime(RecompressingRecipe recipe) {
        return recipe.getRecipeTime();
    }

    @Override
    protected float getRecipeExperience(RecompressingRecipe recipe) {
        return recipe.getExperience();
    }
}
