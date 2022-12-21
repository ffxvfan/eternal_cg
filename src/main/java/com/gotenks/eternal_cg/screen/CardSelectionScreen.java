package com.gotenks.eternal_cg.screen;

import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.network.BattleInitPacket;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;

public class CardSelectionScreen extends Screen {

    private final ArrayList<CardID> cardIDS;
    private final ArrayList<Integer> selectedIndexes = new ArrayList<>();

    private final int WIDTH = 65;
    private final int HEIGHT = 85;
    private final int MAX_COLS = 5;

    public CardSelectionScreen(ArrayList<CardID> cardIDS) {
        super(new StringTextComponent("Card Selection Screen"));
        this.cardIDS = cardIDS;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int whoKnows) {
        int idx = CardSelectionUtil.mousePosToIndex(mouseX, mouseY, WIDTH, HEIGHT, MAX_COLS, cardIDS.size());
        if(idx != -1 && !selectedIndexes.contains(idx)) {
            selectedIndexes.add(idx);
            if(selectedIndexes.size() == 3) {
                this.onClose();
            }
        }
        return super.mouseReleased(mouseX, mouseY, whoKnows);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        int hover = CardSelectionUtil.mousePosToIndex(mouseX, mouseY, WIDTH, HEIGHT, MAX_COLS, cardIDS.size());
        for(int i = 0; i < cardIDS.size(); i++) {
            if(hover != i) {
                CardSelectionUtil.renderCard(matrixStack.last().pose(), cardIDS.get(i), (i % MAX_COLS) * WIDTH, (i / MAX_COLS) * HEIGHT, WIDTH, HEIGHT, 1.0f);
            }
        }

        if(hover != -1){
            CardSelectionUtil.renderCard(matrixStack.last().pose(), cardIDS.get(hover), (hover % MAX_COLS) * WIDTH, (hover / MAX_COLS) * HEIGHT, WIDTH, HEIGHT, 1.1f);
        }

        for(Integer i : selectedIndexes) {
            drawString(matrixStack, null, i.toString(), (i % MAX_COLS) * (width / 2), (i / MAX_COLS) * (HEIGHT / 2), 16777215);
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        CardPacketHandler.INSTANCE.sendToServer(new BattleInitPacket(this.minecraft.player.getId(), (ArrayList<CardID>) selectedIndexes.stream().map(i -> cardIDS.get(i))));
        super.onClose();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
