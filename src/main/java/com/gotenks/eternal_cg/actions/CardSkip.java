package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;

public class CardSkip implements ICardAction {
    @Override
    public void accept(BattleManager bm) {
        bm.sendAttackerMessageToBoth(bm.defender.player.getScoreboardName() + "'s turn was skipped!");
        bm.attacker.sendSystemMessage("Your opponent's turn was skipped. Select another attack");
        bm.responseQueue.add(bm::waitForCardAction);
    }
}
