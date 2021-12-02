package me.johnnydevo.bettercoalmod.blocks.mattercompressor;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MatterCompressorScreen extends AbstractCompressorScreen<MatterCompressorContainer> {

    public MatterCompressorScreen(MatterCompressorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        texture = new ResourceLocation(BetterCoalMod.MOD_ID, "textures/gui/matter_compressor.png");
    }

    @Override
    protected float getProgressArrowScale() {
        return menu.getProgressArrowScale();
    }
}
