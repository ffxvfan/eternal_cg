package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;

public class CardLimited implements ICardAction {

    private int uses;
    private final int maxUses;
    private final String title;
    private final String description;
    private final ICardAction effect;

    public CardLimited(int uses, String title, String description, ICardAction effect) {
        this.maxUses = this.uses = uses;
        this.title = title;
        this.description = description;
        this.effect = effect;
    }

    @Override
    public void accept(BattleManager bm) {
        if(uses == 0) {
            bm.attacker.sendSystemMessage("Can not use " + title);
            bm.responseQueue.add(bm::waitForCardAction);
            return;
        }

        uses--;
        effect.accept(bm);
    }
}
