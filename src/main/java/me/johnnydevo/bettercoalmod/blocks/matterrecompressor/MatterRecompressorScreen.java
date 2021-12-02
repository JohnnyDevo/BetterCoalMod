package me.johnnydevo.bettercoalmod.blocks.matterrecompressor;

import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MatterRecompressorScreen extends AbstractCompressorScreen<MatterRecompressorContainer> {

    public MatterRecompressorScreen(MatterRecompressorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        texture = new ResourceLocation(BetterCoalMod.MOD_ID, "textures/gui/matter_recompressor.png");
    }

    @Override
    protected float getProgressArrowScale() {
        return menu.getProgressArrowScale();
    }
}
