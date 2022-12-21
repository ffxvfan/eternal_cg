package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.items.CardID;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;

public class BattlePlayer {
    public int entityID;
    public ArrayList<CardID> cards;

    public BattlePlayer(int entityID, ArrayList<CardID> cards) {
        this.entityID = entityID;
        this.cards = cards;
    }

    public ServerPlayerEntity getServerEntity() {
        return (ServerPlayerEntity) Minecraft.getInstance().level.getEntity(entityID);
    }
}
