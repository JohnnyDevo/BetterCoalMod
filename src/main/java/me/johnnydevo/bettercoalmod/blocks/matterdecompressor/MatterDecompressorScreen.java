package me.johnnydevo.bettercoalmod.blocks.matterdecompressor;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.johnnydevo.bettercoalmod.BetterCoalMod;
import me.johnnydevo.bettercoalmod.blocks.abstracts.AbstractCompressorScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class MatterDecompressorScreen extends AbstractCompressorScreen<MatterDecompressorContainer> {

    public MatterDecompressorScreen(MatterDecompressorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        texture = new ResourceLocation(BetterCoalMod.MOD_ID, "textures/gui/matter_decompressor.png");
    }

    @Override
    protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        super.renderBg(pMatrixStack, pPartialTicks, pX, pY);
        int posX = (width - imageWidth) / 2;
        int posY = (height - imageHeight) / 2;
        blit(pMatrixStack, posX + 79, posY + 35, 176, 14, (int)(menu.getProgressArrowScale() * 24), 16);
    }

    @Override
    protected float getProgressArrowScale() {
        return menu.getProgressArrowScale();
    }
}
