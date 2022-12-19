package com.gotenks.eternal_cg.screen;

import com.gotenks.eternal_cg.items.Card;
import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.network.BattleInitPacket;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;

public class CardScreen extends Screen {

    public ArrayList<CardSelectableIcon> cards = new ArrayList<>();
    public ArrayList<CardSelectableIcon> selected = new ArrayList<>();

    public CardScreen(PlayerInventory inventory) {
        super(new StringTextComponent("Card Screen"));

        int x = 0;
        int y = 0;
        for(ItemStack item : inventory.items) {
            CardID cardID = CardID.getCardByName(item.getItem().toString());
            if(cardID != null) {
                cards.add(new CardSelectableIcon(cardID, (x % 5) * 60, (y / 5) * 85, 60, 85));
                x++;
                y++;
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int whoKnows) {
        for(CardSelectableIcon card : selected) {
            if(card.isIn((int) mouseX, (int) mouseY)) {
                selected.remove(card);
                return super.mouseReleased(mouseX, mouseY, whoKnows);
            }
        }

        for(CardSelectableIcon card : cards) {
            if(card.isIn((int) mouseX, (int) mouseY)) {
                selected.add(new CardSelectableIcon(card.cardID, 350, selected.size() * 85 + 10, 60, 85));
                return super.mouseReleased(mouseX, mouseY, whoKnows);
            }
        }
        return super.mouseReleased(mouseX, mouseY, whoKnows);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        CardSelectableIcon renderLast = null;
        for(CardSelectableIcon card : cards) {
            if(card.isIn(mouseX, mouseY)) {
                renderLast = card;
            } else {
                card.render(matrixStack.last().pose());
            }
        }

        for(CardSelectableIcon card : selected) {
            if(card.isIn(mouseX, mouseY)) {
                renderLast = card;
            } else {
                card.render(matrixStack.last().pose());
            }
        }

        if(renderLast != null) {
            renderLast.setScale(1.1f);
            renderLast.render(matrixStack.last().pose());
            renderLast.resetScale();
        }

        if(selected.size() == 3) {
            this.onClose();
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
