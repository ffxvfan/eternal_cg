package com.gotenks.eternal_cg.screen;

import com.gotenks.eternal_cg.cards.CardID;
import com.gotenks.eternal_cg.network.CardDisplayResponse;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class CardDisplay extends Screen {

    private final CardID cardID;

    private int openTime = 0;
    private final int threshold = 5;
    private int numSelectable = 2;

    public CardDisplay(CardID cardID) {
        super(new StringTextComponent(cardID.card.name + " screen"));
        this.cardID = cardID;
        if(this.cardID.card.buff != null) numSelectable = 3;
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
        int index = CardRenderUtil.mousePosToIndex(mouseX, mouseY, this.width / 2 - 50, 0, this.width / 2 + 50, 40, 1, numSelectable);
        if(index != -1 && threshold <= openTime) {
            CardPacketHandler.INSTANCE.sendToServer(new CardDisplayResponse(cardID, index));
            onClose();
        }
        return super.mouseReleased(mouseX, mouseY, whoKnows);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);

        CardRenderUtil.renderCard(matrixStack.last().pose(), cardID, 10, 10, 150, 200, 1f);
        CardRenderUtil.renderDescription(matrixStack, cardID.card.majorAttack, 160, 40, 16777215);
        CardRenderUtil.renderDescription(matrixStack, cardID.card.minorAttack, 160, 80, 16777215);
        if(cardID.card.buff != null) CardRenderUtil.renderDescription(matrixStack, cardID.card.buff, 160, 120, 16777215);

        int idx = CardRenderUtil.mousePosToIndex(mouseX, mouseY, this.width / 2 - 50, 0, this.width / 2 + 50, 40, 1, numSelectable);
        if(idx != -1) AbstractGui.fill(matrixStack, this.width / 2 - 50, (idx + 1) * 50 - 25, this.width, (idx + 1) * 50 + 25, Integer.MAX_VALUE);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
