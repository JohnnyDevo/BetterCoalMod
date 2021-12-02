package me.johnnydevo.bettercoalmod.blocks.abstracts;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;

public abstract class AbstractCompressorContainer extends Container {
    protected Block blockType;
    protected IIntArray fields;

    protected TileEntity tileEntity;
    protected IItemHandler playerInventory;

    private final int inventorySize;

    protected AbstractCompressorContainer(Block blockType, @Nullable ContainerType<?> pMenuType, PlayerInventory playerInventory, BlockPos pos, World world, int pContainerId, IIntArray fields, int inventorySize) {
        super(pMenuType, pContainerId);
        this.blockType = blockType;
        this.fields = fields;
        this.playerInventory = new InvWrapper(playerInventory);
        tileEntity = world.getBlockEntity(pos);
        addDataSlots(fields);
        this.inventorySize = inventorySize;
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void layoutPlayerInventorySlots(IItemHandler handler, int leftColPos, int topRowPos) {
        // Player inventory
        addSlotBox(handler, 9, leftColPos, topRowPos, 9, 18, 3, 18);

        // Hotbar
        topRowPos += 58;
        addSlotRange(handler, 0, leftColPos, topRowPos, 9, 18);
    }

    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()), pPlayer, blockType);
    }

    public float getProgressArrowScale() {
        if (fields != null && fields.get(1) > 0) {
            return (float)fields.get(0) / (float)fields.get(1);
        } else return 0;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity pPlayer, int pIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();

            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (pIndex == 1) {
                if (!moveItemStackTo(slotItem, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotItem, itemStack);
            } else if (pIndex > inventorySize - 1) {
                if (!moveItemStackTo(slotItem, 0, inventorySize - 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(slotItem, inventorySize, playerHotbarEnd, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotItem.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, slotItem);
        }

        return itemStack;
    }
}
