package com.gotenks.eternal_cg.screen;

import com.gotenks.eternal_cg.items.CardID;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;

public class CardSelectableIcon {
    public CardID cardID;
    public float x;
    public float y;
    public float width;
    public float height;
    private float scale = 1.0f;

    public CardSelectableIcon(CardID cardID, float x, float y, float width, float height) {
        this.cardID = cardID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(Matrix4f matrix4f) {
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

    public boolean isIn(int a, int b) {
        return x <= a && a <= x + width && y <= b && b <= y + height;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void resetScale() {
        scale = 1.0f;
    }
}
