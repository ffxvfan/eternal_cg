package com.gotenks.eternal_cg.screen;

import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.BattleStatePacket;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;

public class CardScreen extends Screen {

    public ArrayList<ResourceLocation> cards = new ArrayList<>();
    public ResourceLocation[] selectedCards = new ResourceLocation[3];
    private int selectedIndex = 0;

    public final int cardWidth = 60;
    public final int cardHeight = 85;
    public final int maxCols = 5;

    public CardScreen(PlayerInventory inventory) {
        super(new StringTextComponent("Card Screen"));
        for (ItemStack item: inventory.items) {
            CardID cardID = CardID.getCardByName(item.getItem().toString());
            if(cardID != null) {
                cards.add(cardID.getResourceLocation());
            }
        }
    }


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int whoKnows) {
        int idx = hoveringOver((int) mouseX, (int) mouseY);
        if(idx > -1 && selectedIndex < 3)  {
            selectedCards[selectedIndex++] = cards.get(idx);
        }

        return super.mouseReleased(mouseX, mouseY, whoKnows);
    }

    public int hoveringOver(int hoverX, int hoverY) {
        int x = (hoverX)/(cardWidth);
        int y = (hoverY)/(cardHeight);
        int idx = x + y * maxCols;
        if((-1 < x && x < maxCols) && idx < cards.size()) {
            return idx;
        } else {
            return -1;
        }
    }

    private void renderCard(Matrix4f matrix4f, ResourceLocation card, int x, int y, float translation) {
        this.minecraft.getTextureManager().bind(card);
        RenderSystem.pushMatrix();
        RenderSystem.translatef(x, y, 0);
        RenderSystem.scalef(translation, translation, 0);

        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, 0, cardHeight, 0).uv(0, 1).endVertex();
        bufferbuilder.vertex(matrix4f, cardWidth, cardHeight, 0).uv(1, 1).endVertex();
        bufferbuilder.vertex(matrix4f, cardWidth, 0, 0).uv(1, 0).endVertex();
        bufferbuilder.vertex(matrix4f, 0, 0, 0).uv(0, 0).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.popMatrix();
    }

    private void renderDeck(Matrix4f matrix4f, int hoverX, int hoverY) {
        int end = hoveringOver(hoverX, hoverY);

        if(end == -1) {
            for (int i = 0; i < cards.size(); i++) {
                int x = (cardWidth * (i % maxCols));
                int y = (cardHeight * (i / maxCols));
                renderCard(matrix4f, cards.get(i), x, y, 1);
            }
        } else {
            int i = (end + 1) % cards.size();

            for(; i != end; i = (i + 1) % cards.size()) {
                int x = (cardWidth * (i % maxCols));
                int y = (cardHeight * (i / maxCols));
                renderCard(matrix4f, cards.get(i), x, y, 1);
            }

            int x = (cardWidth * (end % maxCols));
            int y = (cardHeight * (end / maxCols));
            renderCard(matrix4f, cards.get(end), x-5, y-5, 1.1f);
        }
    }

    private void renderSelection(Matrix4f matrix4f) {
        for(int i = 0; i < selectedIndex; i++) {
            renderCard(matrix4f, selectedCards[i], maxCols * cardWidth + 50, cardHeight * i, 0.9f);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        renderDeck(matrixStack.last().pose(), mouseX, mouseY);
        renderSelection(matrixStack.last().pose());
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if(selectedIndex == 3) this.onClose();
    }

    @Override
    public void onClose() {
        CardPacketHandler.INSTANCE.sendToServer(new BattleStatePacket(BattleManagerFactory.State.INIT));
        super.onClose();
    }
}
