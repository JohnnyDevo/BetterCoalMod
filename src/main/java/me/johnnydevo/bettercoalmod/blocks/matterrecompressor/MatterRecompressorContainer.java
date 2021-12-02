package me.johnnydevo.bettercoalmod.blocks.matterrecompressor;

import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorContainer;
import me.johnnydevo.bettercoalmod.setup.ModBlocks;
import me.johnnydevo.bettercoalmod.setup.ModContainers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class MatterRecompressorContainer extends AbstractCompressorContainer {
    private static int INV_SIZE = 3;

    public MatterRecompressorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, IIntArray fields) {
        super(ModBlocks.MATTER_RECOMPRESSOR.get(), ModContainers.MATTER_RECOMPRESSOR.get(), playerInventory, pos, world, windowId, fields, INV_SIZE);

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

        layoutPlayerInventorySlots(this.playerInventory, 8, 84);
    }
}
