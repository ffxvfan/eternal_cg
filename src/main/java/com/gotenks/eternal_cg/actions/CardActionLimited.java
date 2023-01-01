package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;

import java.util.function.Consumer;

public class CardActionLimited extends CardAction {
    public int numUses;

    public CardActionLimited(String name, String description, Consumer<BattleManager> action, int numUses) {
        super(name, description, action);
        this.numUses = numUses;
        this.action = battleManager -> {
            if(this.numUses > 0) {
                action.accept(battleManager);
                this.numUses--;
            } else {
                battleManager.attacker.sendSystemMessage("Could not use " + name + ".");
            }
        };
    }
}
