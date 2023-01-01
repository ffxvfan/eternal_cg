package com.gotenks.eternal_cg.screen;

import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.CardSelectionResponsePacket;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CardSelectionScreen extends Screen {

    private final ArrayList<CardID> cardIDS;
    private final ArrayList<Integer> selectedIndexes = new ArrayList<>();
    private final int WIDTH;
    private final int HEIGHT;
    private final int MAX_COLS;
    private final int MAX_SELECTIONS;

    public CardSelectionScreen(ArrayList<CardID> cardIDS, int width, int height, int maxCols, int maxSelections) {
        super(new StringTextComponent("Card Selection Screen"));
        this.cardIDS = cardIDS;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.MAX_COLS = maxCols;
        this.MAX_SELECTIONS = maxSelections;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int whoKnows) {
        int idx = CardRenderUtil.mousePosToIndex(mouseX, mouseY, WIDTH, HEIGHT, MAX_COLS, cardIDS.size());
        if(selectedIndexes.contains(idx)) {
            selectedIndexes.remove((Integer)idx);
        } else if(idx != -1 && !selectedIndexes.contains(idx)) {
            selectedIndexes.add(idx);
            if(selectedIndexes.size() == MAX_SELECTIONS) {
                this.onClose();
            }
        }
        return super.mouseReleased(mouseX, mouseY, whoKnows);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        int hover = CardRenderUtil.mousePosToIndex(mouseX, mouseY, WIDTH, HEIGHT, MAX_COLS, cardIDS.size());
        for(int i = 0; i < cardIDS.size(); i++) {
            if(hover != i) {
                CardRenderUtil.renderCard(matrixStack.last().pose(), cardIDS.get(i), (i % MAX_COLS) * WIDTH, (i / MAX_COLS) * HEIGHT, WIDTH, HEIGHT, 1.0f);
            }
        }

        if(hover != -1){
            CardRenderUtil.renderCard(matrixStack.last().pose(), cardIDS.get(hover), (hover % MAX_COLS) * WIDTH, (hover / MAX_COLS) * HEIGHT, WIDTH, HEIGHT, 1.1f);
        }

        for(int i = 0; i < selectedIndexes.size(); i++) {
            drawString(matrixStack, CardRenderUtil.font, String.valueOf(i), (selectedIndexes.get(i) % MAX_COLS) * WIDTH + WIDTH/2, (selectedIndexes.get(i) / MAX_COLS) * HEIGHT + HEIGHT/2, 16777215);
        }

//        for(Integer i : selectedIndexes) {
//            drawString(matrixStack, CardRenderUtil.font, i.toString(), (i % MAX_COLS) * WIDTH, (i / MAX_COLS) * HEIGHT, 16777215);
//        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        CardPacketHandler.INSTANCE.sendToServer(new CardSelectionResponsePacket((ArrayList<CardID>) selectedIndexes.stream().map(cardIDS::get).collect(Collectors.toList())));
        super.onClose();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
