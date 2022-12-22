package com.gotenks.eternal_cg.screen;

import com.gotenks.eternal_cg.items.CardID;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

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

    public static void renderCard(Matrix4f matrix4f, CardID cardID, int x, int y, int width, int height, float scale) {
        Minecraft.getInstance().getTextureManager().bind(cardID.getResourceLocation());
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
}
