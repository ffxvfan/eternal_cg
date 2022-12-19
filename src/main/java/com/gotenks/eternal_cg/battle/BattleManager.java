package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.cards.CardAction;
import com.gotenks.eternal_cg.items.CardID;

import java.util.PriorityQueue;

public class BattleManager {
    public CardID[] player1 = null;
    public CardID[] player2 = null;

    public PriorityQueue<CardAction> actionQueue = new PriorityQueue<>();

    public BattleManager() {

    }

    public void init(CardID[] cards) {
        if(player1 == null) {
            player1 = cards;
        } else if(player2 == null) {
            player2 = cards;
            start();
        }
    }

    public void start() {


    }


    public void endBattle() {
        //BattleManagerFactory.removeBattle(this);
    }
}
