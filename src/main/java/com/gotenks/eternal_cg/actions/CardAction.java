package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;

import java.util.function.Consumer;

public class CardAction {

    public String name;
    public String description;
    public Consumer<BattleManager> action;

    public CardAction(String name, String description, Consumer<BattleManager> action) {
        this.name = name;
        this.description = description;
        this.action = action;
    }
}
