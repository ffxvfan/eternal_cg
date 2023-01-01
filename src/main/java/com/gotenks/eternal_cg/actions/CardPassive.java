package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;

import java.util.function.Consumer;

public class CardPassive {

    public String name;
    public String description;
    public Consumer<BattleManager> passive;

    public CardPassive(String name, String description, Consumer<BattleManager> passive) {
        this.name = name;
        this.description = description;
        this.passive = passive;
    }
}
