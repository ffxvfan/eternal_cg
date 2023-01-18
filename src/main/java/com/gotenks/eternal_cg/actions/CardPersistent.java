package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;

public class CardPersistent implements ICardAction {
    private final int baseUses;
    private int uses;
    private final int dmg;
    private final String title;

    public CardPersistent(int uses, int dmg, String title) {
        this.uses = this.baseUses = uses;
        this.dmg = dmg;
        this.title = title;
    }

    @Override
    public void accept(BattleManager bm) {
        if(bm.nextTurnEffects.contains(this)) return;

        bm.nextTurnEffects.add(bm1 -> {
            bm1.nextTurnEffects.add(bm2 -> {
                uses--;
                bm2.applyDamageStep(dmg, title);
                if(uses == 0) {
                    uses = baseUses;
                    bm2.attacker.sendSystemMessage("(" + title + " has worn off)");
                } else {
                    bm2.nextTurnEffects.add(this);
                }
            });
        });
    }
}
