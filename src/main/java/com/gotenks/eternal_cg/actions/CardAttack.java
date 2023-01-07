package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;
import com.gotenks.eternal_cg.types.Type;

import java.util.Random;
import java.util.function.Consumer;

public class CardAttack extends CardAction {

    public int baseDamage;
    public Type type;
    public Consumer<BattleManager> effect;

    public CardAttack(String name, String description, Type type, int baseDamage, Consumer<BattleManager> effect) {
        super(name, description, battleManager -> {
            int damage = new Random().nextInt(baseDamage + 1);
            battleManager.sendAttackerMessageToBoth("Selected " + name);
            battleManager.sendAttackerMessageToBoth("?Roll: " + damage);
            if (damage < 5) {
                battleManager.sendAttackerMessageToBoth("Attack rolled for less than 5... opponent will deal +5atk next turn.");
                battleManager.addNextTurn(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("Previous turn was a miss, +5atk");
                    battleManager1.defender.applyDamage(5);
                });
                effect.accept(battleManager);
                return;
            } else if (damage == baseDamage) {
                battleManager.sendAttackerMessageToBoth("Critical! +10atk.");
                damage += 10;
            }
            effect.accept(battleManager);

            int majorAddl = Type.typeDamageMod(type, battleManager.defender.getCard().major, true);
            int minorAddl = Type.typeDamageMod(type, battleManager.defender.getCard().minor, false);
            if(majorAddl > 0) {
                battleManager.sendAttackerMessageToBoth(type.name() + " has major advantage over " + battleManager.defender.getCard().major + ". +7atk!" );
                battleManager.defender.applyDamage(7);
            }
            if(minorAddl > 0) {
                battleManager.sendAttackerMessageToBoth(type.name() + " has minor advantage over " + battleManager.defender.getCard().minor + ". +2atk!" );
                battleManager.defender.applyDamage(2);
            }
            battleManager.defender.applyDamage(damage);
        });
        this.type = type;
        this.baseDamage = baseDamage;
        this.effect = effect;
        this.description = name + " (" + type.toString() + ")";
    }
}
