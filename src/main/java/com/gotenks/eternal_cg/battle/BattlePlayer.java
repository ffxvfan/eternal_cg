package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.items.CardID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;

public class BattlePlayer {
    public int entityID;
    public ArrayList<CardID> cardIDS;

    public BattlePlayer(int entityID, ArrayList<CardID> cardIDS) {
        this.entityID = entityID;
        this.cardIDS = cardIDS;
    }

    public ServerPlayerEntity getServerEntity() {
        return (ServerPlayerEntity) Minecraft.getInstance().level.getEntity(entityID);
    }

    public void sendMessage(String s) {
        getServerEntity().sendMessage(new StringTextComponent(s), getServerEntity().getUUID());
    }

    public void sendMessage(String s, TextFormatting textFormatting) {
        getServerEntity().sendMessage(new StringTextComponent(s).withStyle(textFormatting), getServerEntity().getUUID());
    }

    public void setFirstCard(CardID card) {
        Collections.swap(cardIDS, 0, cardIDS.indexOf(card));
    }
}
