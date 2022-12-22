package com.gotenks.eternal_cg.screen;

import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.network.CardDisplayResponsePacket;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class CardDisplay extends Screen {

    private final CardID cardID;
    private int index = -1;

    public CardDisplay(CardID cardID) {
        super(new StringTextComponent(cardID.name + " screen"));
        this.cardID = cardID;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int whoKnows) {
        index = CardRenderUtil.mousePosToIndex(mouseX, mouseY, this.width - 160, 85, 1, cardID.card.cardActions.length);
        if(index != -1) {
            onClose();
        }
        return super.mouseReleased(mouseX, mouseY, whoKnows);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        CardRenderUtil.renderCard(matrixStack.last().pose(), cardID, 10, 10, 120, 170, 1.0f);
        for(int i = 0; i < cardID.card.cardActions.length; i++) {
            drawString(matrixStack, CardRenderUtil.font, cardID.card.cardActions[i].description, 160, i * 85, 16777215);

            int idx = CardRenderUtil.mousePosToIndex(mouseX, mouseY, this.width - 160, 85, 1, cardID.card.cardActions.length);
            if(idx != -1) {
                AbstractGui.fill(matrixStack, 160, i * 85, this.width - 160, 85, 16777215);
            }
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        CardPacketHandler.INSTANCE.sendToServer(new CardDisplayResponsePacket(this.minecraft.player.getId(), index));
        super.onClose();
    }
}
