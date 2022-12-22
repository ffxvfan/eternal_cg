package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.items.CardID;

import java.util.ArrayList;
import java.util.HashMap;

public class BattleManagerFactory {

    private static final HashMap<Integer, BattleManager> factory = new HashMap<>();

    public static void add(int entityID1, int entityID2) {
        BattleManager bm = new BattleManager(new BattlePlayer(entityID1, null), new BattlePlayer(entityID2, null));
        factory.put(entityID1, bm);
        factory.put(entityID2, bm);
    }

    public static void update(int entityID, ArrayList<CardID> cardIDS) {
        BattleManager battleManager = factory.get(entityID);
        if(battleManager != null) {
            battleManager.update(entityID, cardIDS);
        }
    }

    public static void actionSelection(int entityID, CardID cardID, int index) {
        BattleManager battleManager = factory.get(entityID);
        if(battleManager != null) {
            battleManager.actionSelection(entityID, cardID, index);
        }
    }
}
