package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;

import java.util.Random;

public class CardRandom implements ICardAction {

    private final String title;
    private final int nSides;
    private final int threshold;
    private final int dmg;

    public CardRandom(String title, int nSides, int threshold, int dmg) {
        this.title = title;
        this.nSides = nSides;
        this.threshold = threshold;
        this.dmg = dmg;
    }

    @Override
    public void accept(BattleManager bm) {
        int roll = new Random().nextInt(nSides);
        if(threshold <= roll) {
            bm.sendOpposingMessageToBoth(title + " rolls..." + roll + "!");
            bm.nextTurnEffects.add(bm1 -> bm1.applyDamageStep(dmg, title));
        } else {
            bm.sendOpposingMessageToBoth(title + " rolls..." + roll + ". Better luck next time.");
        }
    }
}
