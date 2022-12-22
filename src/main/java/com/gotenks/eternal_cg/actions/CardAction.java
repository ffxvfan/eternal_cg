package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;
import com.gotenks.eternal_cg.types.Type;

import java.util.function.Consumer;

public class CardAction {

    public String name;
    public String description;
    public Type type;
    public Consumer<BattleManager> action;

    public CardAction(String name, String description, Type type) {
        this.name = name;
        this.description = name + " (" + type.toString() + ") " + description;
        this.type = type;

        action = BattleManager::cycle;
    }




}
