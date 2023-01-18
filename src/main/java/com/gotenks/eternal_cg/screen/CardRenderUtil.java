package com.gotenks.eternal_cg.screen;

import com.gotenks.eternal_cg.actions.ICardAction;
import com.gotenks.eternal_cg.cards.CardID;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

import java.util.Objects;

import static net.minecraft.client.gui.AbstractGui.drawString;

public class CardRenderUtil {

    private CardRenderUtil() {

    }

    public static FontRenderer font = Minecraft.getInstance().font;

    public static int mousePosToIndex(double mouseX, double mouseY, int width, int height, int cols, int max) {
        int x = (int) (mouseX / width);
        int y = (int) (mouseY / height);
        int idx = x + y * cols;
        if((-1 < x && x < cols) && idx < max) {
            return idx;
        }
        return -1;
    }

    public static int mousePosToIndex(double mouseX, double mouseY, int offsetX, int offsetY, int width, int height, int cols, int max) {
        return mousePosToIndex(mouseX - offsetX, mouseY - offsetY, width, height, cols, max);
    }

    public static void renderCard(Matrix4f matrix4f, CardID cardID, int x, int y, int width, int height, float scale) {
        Minecraft.getInstance().getTextureManager().bind(Objects.requireNonNull(cardID.card.getRegistryName()));
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 0);
        RenderSystem.scalef(scale, scale, 0);
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, 0, height, 0).uv(0, 1).endVertex();
        bufferbuilder.vertex(matrix4f, width, height, 0).uv(1, 1).endVertex();
        bufferbuilder.vertex(matrix4f, width, 0, 0).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f, 0, 0, 0).uv(0, 0).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.popMatrix();
    }

    public static void renderDescription(MatrixStack matrixStack, ICardAction action, int x, int y, int color) {
        drawString(matrixStack, font, action.toString(), x, y, color);
    }
}
