package me.johnnydevo.bettercoalmod.blocks.mattercompressor;

import me.johnnydevo.bettercoalmod.setup.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;

public class MatterCompressorContainer extends Container {
    private IInventory inventory;
    private IIntArray fields;

    public MatterCompressorContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(id, playerInventory, new MatterCompressorTile(), new IntArray(buffer.readByte()));
    }

    public MatterCompressorContainer(int id, PlayerInventory playerInventory, IInventory inventory, IIntArray fields) {
        super(ModContainers.MATTER_COMPRESSOR.get(), id);
        this.inventory = inventory;
        this.fields = fields;

        addSlot(new Slot(inventory, 0, 56, 35));
        addSlot(new Slot(inventory, 1, 117, 35) {
            @Override
            public boolean mayPlace(ItemStack pStack) {
                return false;
            }
        });

        //player backpack
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                int index = x + y * 9;
                int posX = 8 + x * 18;
                int posY = 84 + y * 18;
                addSlot(new Slot(playerInventory, index + 9, posX, posY));
            }
        }

        //player hotbar
        for (int x = 0; x < 9; ++x) {
            addSlot(new Slot(playerInventory, x, 8 + x * 18, 142));
        }

        addDataSlots(fields);
    }

    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        return inventory.stillValid(pPlayer);
    }

    public int getProgressArrowScale() {
        return Math.max(fields.get(0) * 24 / MatterCompressorTile.WORK_TIME, 0);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity pPlayer, int pIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();

            final int inventorySize = 2;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (pIndex == 1) {
                if (!moveItemStackTo(slotItem, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotItem, itemStack);
            } else if (pIndex != 0) {
                if (!moveItemStackTo(slotItem, 0, 1, false)) {
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
