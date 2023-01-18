package com.gotenks.eternal_cg.actions;

import com.gotenks.eternal_cg.battle.BattleManager;
import com.gotenks.eternal_cg.cards.CardID;
import com.gotenks.eternal_cg.types.Type;

import java.util.ArrayList;
import java.util.Random;


public class CardAttack implements ICardAction {

    public String name;
    public String description;
    public Type type;
    public int baseDmg;
    public ArrayList<ICardAction> effects;

    public CardAttack(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.type = builder.type;
        this.baseDmg = builder.baseDmg;
        this.effects = builder.effects;
    }

    private void nextTurnMissEffect(BattleManager bm) {
        bm.applyDamageStep(5, "Previous Turn Miss");
    }

    private void weaknessCalc(BattleManager bm) {
        if(Type.isWeakAgainst(type, bm.defender.getCard().majorType())) bm.applyDamageStep(7, "Major Bonus");
        if(Type.isWeakAgainst(type, bm.defender.getCard().minorType())) bm.applyDamageStep(2, "Minor Bonus");
    }

    private boolean tenksCheck(BattleManager bm) {
        return bm.defender.getCardID() == CardID.TENKS && (type == Type.FIRE || type == Type.FAUNA);
    }

    @Override
    public void accept(BattleManager bm) {
        int dmg = new Random().nextInt(baseDmg + 1);
        if(dmg <= 5) {
            bm.nextTurnEffects.add(this::nextTurnMissEffect);
            bm.sendAttackerMessageToBoth("Attack Miss.");
            return;
        } else if(tenksCheck(bm)) {
            bm.sendDefenderMessageToBoth("Tough as a Boulder Buff: Cannot be affected by fire and fauna attacks");
            return;
        }

        bm.applyDamageStep(dmg, "Roll");
        if(dmg == baseDmg) bm.applyDamageStep(10, "CRIT!");
        weaknessCalc(bm);
        if(effects != null) effects.forEach(effect -> effect.accept(bm));
    }

    @Override
    public String toString() {
        return name + "(" + type + "): " + baseDmg + "atk, " + description;
    }

    public static class Builder {
        private String name = "";
        private String description = "";
        private Type type = Type.NO_TYPE;
        private int baseDmg = 0;
        private ArrayList<ICardAction> effects = null;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder baseDmg(int baseDmg) {
            this.baseDmg = baseDmg;
            return this;
        }

        public Builder withEffect(ICardAction effect) {
            this.effects.add(effect);
            return this;
        }
    }
}
