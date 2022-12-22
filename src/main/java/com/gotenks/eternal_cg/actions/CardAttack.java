package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;
import com.gotenks.eternal_cg.types.Type;

import java.util.Random;
import java.util.function.Consumer;

public class CardAttack extends CardAction {

    public int baseDamage;
    public Consumer<BattleManager> effect;

    public CardAttack(String name, String description, Type type, int baseDamage, Consumer<BattleManager> effect) {
        super(name, description, battleManager -> {
            int damage = new Random().nextInt(baseDamage);
            battleManager.sendToBoth("Selected " + description + "\n?Roll: " + damage);
            if (damage < 5) {
                battleManager.sendToBoth("Attack rolled for less than 5... opponent will deal +5atk next turn.");
                battleManager.addAfterTurn(battleManager1 -> {
                    battleManager1.sendToBoth("Previous turn was a miss, +5atk");
                    battleManager1.defender.cardIDS.get(0).card.health -= 5;
                });
            } else if (damage == baseDamage) {
                battleManager.sendToBoth("Critical! +10atk.");
                damage += 10;
            } else {
                effect.accept(battleManager);
            }
            battleManager.defender.cardIDS.get(0).card.health -= damage;
            battleManager.sendToBoth(battleManager.defender.cardIDS.get(0).name + " HP: " + battleManager.defender.cardIDS.get(0).card.health);
            battleManager.cycle();
        });
        this.baseDamage = baseDamage;
        this.effect = effect;
        this.description = name + " (" + type.toString() + ") " + description;
    }
}
