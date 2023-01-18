package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;

import java.util.function.Predicate;

public class CardPredicate implements ICardAction {

    private final Predicate<BattleManager> predicate;
    private final ICardAction effect;

    public CardPredicate(Predicate<BattleManager> predicate, ICardAction effect) {
        this.predicate = predicate;
        this.effect = effect;
    }

    @Override
    public void accept(BattleManager bm) {
        if(!predicate.test(bm)) {
            bm.attacker.sendSystemMessage("Could not use effect. Select a different attack.");
            bm.responseQueue.add(bm::waitForCardAction);
            return;
        }
        effect.accept(bm);
    }
}
