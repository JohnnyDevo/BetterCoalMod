package me.johnnydevo.bettercoalmod.blocks.abstracts;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class AbstractCompressorScreen<T extends Container> extends ContainerScreen<T> {
    protected ResourceLocation texture;

    protected AbstractCompressorScreen(T pMenuType, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenuType, pPlayerInventory, pTitle);
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        renderBackground(pMatrixStack);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        renderTooltip(pMatrixStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        RenderSystem.color4f(1, 1, 1, 1);
        minecraft.getTextureManager().bind(texture);

        int posX = (width - imageWidth) / 2;
        int posY = (height - imageHeight) / 2;

        blit(pMatrixStack, posX, posY, 0, 0, imageWidth, imageHeight);

        blit(pMatrixStack, posX + 79, posY + 35, 176, 14, (int)(getProgressArrowScale() * 24), 16);
    }

    protected abstract float getProgressArrowScale();
}
