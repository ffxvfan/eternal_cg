package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;

public class CardHeal implements ICardAction {

    private final int healAmt;

    public CardHeal(int healAmt) {
        this.healAmt = healAmt;
    }

    @Override
    public void accept(BattleManager bm) {
        // constrain healing amount
        int prevHP = bm.attacker.getCard().HP;
        int hp = prevHP + healAmt;
        bm.attacker.getCard().HP = Math.min(hp, bm.attacker.getCard().maxHP);

        bm.sendOpposingMessageToBoth(bm.attacker.getCard().name + " healed for " + prevHP + " -> " + hp + ".");
    }
}
