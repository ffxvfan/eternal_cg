package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;
import com.gotenks.eternal_cg.types.Type;

import java.util.function.Consumer;

public class CardAttackLimited extends CardAttack implements ILimited {
    public int numUses;
    private final int MAX_NUM_USES;

    public CardAttackLimited(String name, String description, Type type, int baseDamage, Consumer<BattleManager> effect, int numUses) {
        super(name, description, type, baseDamage, effect);
        this.MAX_NUM_USES = this.numUses = numUses;
        this.action = battleManager -> {
            if(this.numUses > 0) {
                action.accept(battleManager);
                this.numUses--;
            } else {
                battleManager.attacker.sendSystemMessage("Could not use " + name + ".");
            }
        };
    }

    public void reset() {
        numUses = MAX_NUM_USES;
    }
}
