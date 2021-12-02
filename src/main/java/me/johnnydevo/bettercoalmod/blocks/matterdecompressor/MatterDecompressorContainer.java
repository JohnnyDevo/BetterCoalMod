package me.johnnydevo.bettercoalmod.blocks.matterdecompressor;

import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorContainer;
import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import me.johnnydevo.bettercoalmod.setup.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class MatterDecompressorContainer extends AbstractCompressorContainer {
    private static int INV_SIZE = 3;

    public MatterDecompressorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, IIntArray fields) {
        super(ModBlocks.MATTER_DECOMPRESSOR.get(), ModContainers.MATTER_DECOMPRESSOR.get(), playerInventory, pos, world, windowId, fields, INV_SIZE);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 56, 53));
                addSlot(new SlotItemHandler(h, 1, 56, 17) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        if (stack.isEmpty()) {
                            return false;
                        }
                        Integer burnTime = stack.getBurnTime(IRecipeType.SMELTING);
                        if (burnTime == -1) {
                            burnTime = FurnaceTileEntity.getFuel().get(stack.getItem());
                        }
                        return (burnTime != null && burnTime > 0);
                    }
                });
                addSlot(new SlotItemHandler(h, 2, 116, 35) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return false;
                    }

                    @Override
                    public ItemStack onTake(PlayerEntity pPlayer, ItemStack pStack) {
                        if (tileEntity instanceof MatterDecompressorTile) {
                            ((MatterDecompressorTile)tileEntity).awardExp(pPlayer);
                        }
                        return super.onTake(pPlayer, pStack);
                    }
                });
            });
        }

        layoutPlayerInventorySlots(this.playerInventory, 8, 84);
    }

    public float getBurnTimeScale() {
        if (fields != null && fields.get(3) > 0) {
            return (float)fields.get(2) / (float)fields.get(3);
        } else return 0;
    }
}
