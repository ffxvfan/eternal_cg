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

    private int openTime = 0;
    private final int threshold = 10;

    public CardDisplay(CardID cardID) {
        super(new StringTextComponent(cardID.name + " screen"));
        this.cardID = cardID;
    }

    @Override
    public void tick() {
        if(openTime < threshold) {
            openTime++;
        }
        super.tick();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int whoKnows) {
        int index = CardRenderUtil.mousePosToIndex(mouseX, mouseY, this.width / 2 - 50, 0, this.width / 2 + 50, 70, 1, cardID.card.cardActions.size());
        if(index != -1 && threshold <= openTime) {
            CardPacketHandler.INSTANCE.sendToServer(new CardDisplayResponsePacket(cardID, index));
            onClose();
        }
        return super.mouseReleased(mouseX, mouseY, whoKnows);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        CardRenderUtil.renderCard(matrixStack.last().pose(), cardID, 10, 10, 150, 200, 1f);
        for(int i = 0; i < cardID.card.cardActions.size(); i++) {
            drawString(matrixStack, CardRenderUtil.font, cardID.card.cardActions.get(i).description, this.width / 2 - 50, (i + 1) * 50, 16777215);
        }

        int idx = CardRenderUtil.mousePosToIndex(mouseX, mouseY, this.width / 2 - 50, 0, this.width / 2 + 50, 70, 1, cardID.card.cardActions.size());
        if(idx != -1) {
            AbstractGui.fill(matrixStack, this.width / 2 - 52, (idx + 1) * 50 - 20, this.width, (idx + 1) * 50 + 25, Integer.MAX_VALUE);
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
