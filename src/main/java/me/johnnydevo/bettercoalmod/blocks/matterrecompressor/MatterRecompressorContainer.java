package me.johnnydevo.bettercoalmod.blocks.matterrecompressor;

import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import me.johnnydevo.bettercoalmod.setup.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

public class MatterRecompressorContainer extends Container {
    private final IIntArray fields;

    private final TileEntity tileEntity;
    private final IItemHandler playerInventory;

    public MatterRecompressorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, IIntArray fields) {
        super(ModContainers.MATTER_RECOMPRESSOR.get(), windowId);
        tileEntity = world.getBlockEntity(pos);
        this.fields = fields;
        this.playerInventory = new InvWrapper(playerInventory);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 60, 22));
                addSlot(new SlotItemHandler(h, 1, 60, 47));
                addSlot(new SlotItemHandler(h, 2, 116, 35) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return false;
                    }

                    @Override
                    public ItemStack onTake(PlayerEntity pPlayer, ItemStack pStack) {
                        if (tileEntity instanceof MatterRecompressorTile) {
                            ((MatterRecompressorTile)tileEntity).awardExp(pPlayer);
                        }
                        return super.onTake(pPlayer, pStack);
                    }
                });
            });
        }

        layoutPlayerInventorySlots(8, 84);

        addDataSlots(fields);
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

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public boolean stillValid(PlayerEntity pPlayer) {
        return stillValid(IWorldPosCallable.create(tileEntity.getLevel(), tileEntity.getBlockPos()), pPlayer, ModBlocks.MATTER_RECOMPRESSOR.get());
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

            final int inventorySize = 3;
            final int playerInventoryEnd = inventorySize + 27;
            final int playerHotbarEnd = playerInventoryEnd + 9;

            if (pIndex == 2) {
                if (!moveItemStackTo(slotItem, inventorySize, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotItem, itemStack);
            } else if (pIndex > 2) {
                if (!moveItemStackTo(slotItem, 0, 2, false)) {
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
