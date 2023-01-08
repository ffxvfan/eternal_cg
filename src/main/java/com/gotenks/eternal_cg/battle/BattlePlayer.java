package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.EternalCG;
import com.gotenks.eternal_cg.items.Card;
import com.gotenks.eternal_cg.items.CardID;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Collections;

public class BattlePlayer {
    public ServerPlayerEntity player;
    public ArrayList<CardID> cardIDS;

    public BattlePlayer(ServerPlayerEntity player, ArrayList<CardID> cardIDS) {
        this.player = player;
        this.cardIDS = cardIDS;
    }

    public void sendMessage(String s) {
        player.sendMessage(new StringTextComponent(s), player.getUUID());
    }

    public void sendSystemMessage(String s) {
        player.sendMessage(new TranslationTextComponent("[%s] " + s, new StringTextComponent("SYSTEM").withStyle(TextFormatting.BOLD, TextFormatting.GRAY)), player.getUUID());
        EternalCG.LOGGER.debug("[SYSTEM_TO_PLAYER] " + s);
    }

    public Card getCard() {
        return cardIDS.get(0).card;
    }

    public CardID getCardID() {
        return cardIDS.get(0);
    }

    public void applyDamage(int damage) {
        getCard().health -= damage;
    }

    public void setFirstCard(CardID card) {
        Collections.swap(cardIDS, 0, cardIDS.indexOf(card));
    }
}
