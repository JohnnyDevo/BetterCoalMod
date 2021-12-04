package me.johnnydevo.bettercoalmod.blocks.abstracts;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCompressorTile<T extends IRecipe<IInventory>> extends TileEntity implements ITickableTileEntity {
    public final IIntArray fields = makeFields();
    protected IRecipeType<T> recipeType;

    protected ItemStackHandler itemHandler = createHandler();
    protected LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    protected int progress = 0;
    protected int currentRecipeTime = 0;
    protected float savedExp = 0.0F;

    protected int outputSlot;
    protected int[] inputSlots;

    public AbstractCompressorTile(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    protected abstract IIntArray makeFields();

    protected abstract ItemStackHandler createHandler();

    protected abstract int getRecipeTime(T recipe);

    protected abstract float getRecipeExperience(T recipe);

    public void encodeExtraData(PacketBuffer buffer) {
        buffer.writeBlockPos(getBlockPos());
        buffer.writeByte(fields.getCount());
    }

    protected void populateInputs(List<Item> allowedInputs) {
        ClientWorld minecraft = Minecraft.getInstance().level;
        if (minecraft != null) {
            List<T> recipes = minecraft.getRecipeManager().getAllRecipesFor(recipeType);
            for (T recipe : recipes) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    for (ItemStack itemStack : ingredient.getItems()) {
                        allowedInputs.add(itemStack.getItem());
                    }
                }
            }
        }
    }

    protected T getRecipe() {
        if (this.level == null) {
            return null;
        }
        for (int i : inputSlots) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                return null;
            }
        }
        for (T recipe : level.getRecipeManager().getAllRecipesFor(recipeType)) {
            ArrayList<ItemStack> items = new ArrayList<>();
            for (int i : inputSlots) {
                items.add(itemHandler.getStackInSlot(i));
            }

            NonNullList<Ingredient> matches = NonNullList.create();
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            for (Ingredient ingredient : ingredients) {
                for (int j = 0; j < items.size(); ++j) {
                    if (ingredient.test(items.get(j))) {
                        matches.add(ingredient);
                        items.remove(items.get(j));
                        break;
                    }
                }
            }
            if (matches.containsAll(ingredients)) {
                return recipe;
            }
        }
        return null;
    }

    protected ItemStack getWorkOutput(T recipe) {
        if (recipe != null) {
            return recipe.getResultItem();
        }
        return ItemStack.EMPTY;
    }

    protected void stopWork() {
        progress = 0;
        currentRecipeTime = 0;
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

        T recipe = getRecipe();
        if (recipe != null) {
            doWork(recipe);
        } else {
            stopWork();
        }
    }

    protected void doWork(T recipe) {
        ItemStack current = itemHandler.getStackInSlot(outputSlot);
        ItemStack output = getWorkOutput(recipe);
        currentRecipeTime = getRecipeTime(recipe);

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

    protected void finishWork(T recipe) {
        itemHandler.insertItem(outputSlot, recipe.getResultItem().copy(), false);
        progress = 0;
        currentRecipeTime = 0;
        savedExp += getRecipeExperience(recipe);
        for (int i : inputSlots) {
            itemHandler.extractItem(i, 1, false);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    @Override
    public <C> LazyOptional<C> getCapability(Capability<C> cap, Direction side) {
        if (!this.remove && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tags) {
        super.load(state, tags);

        int inventorySize = outputSlot + 1;
        NonNullList<ItemStack> items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tags, items);
        for (int i = 0; i < inventorySize; ++i) {
            itemHandler.insertItem(i, items.get(i), false);
        }

        progress = tags.getInt("Progress");
        currentRecipeTime = tags.getInt("CurrentRecipeTime");
        savedExp = tags.getFloat("savedExp");
    }

    @Override
    public CompoundNBT save(CompoundNBT tags) {
        super.save(tags);

        int inventorySize = outputSlot + 1;
        NonNullList<ItemStack> items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        for (int i = 0; i < inventorySize; ++i) {
            items.set(i, itemHandler.getStackInSlot(i));
        }
        ItemStackHelper.saveAllItems(tags, items);

        tags.putInt("Progress", progress);
        tags.putInt("CurrentRecipeTime", currentRecipeTime);
        tags.putFloat("savedExp", savedExp);
        return tags;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tags = getUpdateTag();

        int inventorySize = outputSlot + 1;
        NonNullList<ItemStack> items = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        for (int i = 0; i < inventorySize; ++i) {
            items.set(i, itemHandler.getStackInSlot(i));
        }
        ItemStackHelper.saveAllItems(tags, items);

        return new SUpdateTileEntityPacket(worldPosition, 1, tags);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tags = super.getUpdateTag();
        tags.putInt("Progress", progress);
        tags.putInt("CurrentRecipeTime", currentRecipeTime);
        tags.putFloat("savedExp", savedExp);
        return tags;
    }
}
