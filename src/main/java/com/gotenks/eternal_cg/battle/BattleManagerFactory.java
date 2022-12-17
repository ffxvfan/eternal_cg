package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.items.CardID;

import java.util.ArrayList;

public class BattleManagerFactory {

    public enum State {
        SELECTING,
        INIT,
    }

    private static ArrayList<BattleManager> factory = new ArrayList<>();
    private static BattleManager tmp = null;

    public static void init(CardID[] cards) {
        if(tmp == null) {
            tmp = new BattleManager();
            tmp.init(cards);
        } else {
            tmp.init(cards);
            factory.add(tmp);
            tmp = null;
        }
    }
}
