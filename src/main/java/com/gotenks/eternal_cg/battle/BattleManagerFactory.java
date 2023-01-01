package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.items.CardID;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class BattleManagerFactory {

    private static final HashMap<ServerPlayerEntity, BattleManager> factory = new HashMap<>();

    public static void add(ServerPlayerEntity entity1, ServerPlayerEntity entity2) {
        BattleManager bm = new BattleManager(new BattlePlayer(entity1, null), new BattlePlayer(entity2, null));
        factory.put(entity1, bm);
        factory.put(entity2, bm);
    }

    public static void update(ServerPlayerEntity entity, ArrayList<CardID> cardIDS) {
        BattleManager battleManager = factory.get(entity);
        if(battleManager != null) {
            battleManager.update(entity, cardIDS);
        }
    }

    public static void actionSelection(ServerPlayerEntity entity, CardID cardID, int index) {
        BattleManager battleManager = factory.get(entity);
        if(battleManager != null) {
            battleManager.actionSelection(entity, cardID, index);
        }
    }
}
