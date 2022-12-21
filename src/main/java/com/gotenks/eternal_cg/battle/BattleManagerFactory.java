package com.gotenks.eternal_cg.battle;

import java.util.ArrayList;

public class BattleManagerFactory {

    private static final ArrayList<BattleManager> factory = new ArrayList<>();
    private static BattleManager tmp = null;

    public static void init(BattlePlayer player) {
        if(tmp == null) {
            tmp = new BattleManager();
            tmp.init(player);
        } else {
            tmp.init(player);
            factory.add(tmp);
            tmp = null;
        }
    }
}
