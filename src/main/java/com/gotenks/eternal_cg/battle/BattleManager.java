package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.ShowCardDisplayPacket;
import net.minecraftforge.fml.network.PacketDistributor;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;

public class BattleManager {

    public enum State {
        INIT,
        START,
        REVIVAL,
        TURN
    }

    public BattlePlayer attacker;
    public BattlePlayer defender;
    public State state;
    private int turnCount = 0;

    public static Consumer<BattleManager> attackerStart;
    public static Consumer<BattleManager> attackerEnd;
    public static final Consumer<BattleManager> turnBookend = battleManager -> battleManager.turnCount++;
    public static final Consumer<BattleManager> roundBookend = battleManager -> {};

    public LinkedList<Consumer<BattleManager>> actionQueue = new LinkedList<>();

    public BattleManager(BattlePlayer attacker, BattlePlayer defender) {
        this.attacker = attacker;
        this.defender = defender;
        this.state = State.INIT;

        attackerStart = battleManager -> {
            battleManager.attacker.sendMessage("You're attacking. Select an attack.");
            CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(battleManager.attacker::getServerEntity), new ShowCardDisplayPacket(battleManager.attacker.cardIDS.get(0)));
        };

        attackerEnd = battleManager -> {
            battleManager.attacker.sendMessage("Attack phase over.");
            battleManager.swapPlayers();
            int startIdx = battleManager.actionQueue.indexOf(turnBookend);
            int endIdx = battleManager.actionQueue.indexOf(roundBookend);
            battleManager.actionQueue.add(endIdx, attackerEnd);
            battleManager.actionQueue.add(endIdx + 1, turnBookend);
            battleManager.actionQueue.add(startIdx, attackerStart);
            battleManager.actionQueue.remove(turnBookend);
            battleManager.cycle();
        };
    }

    public void update(int entityID, ArrayList<CardID> cardIDS) {
        switch(state) {
            case INIT:
                init(entityID, cardIDS);
                break;
            case START:
                determineFirstPlayer();
                actionQueue.add(attackerStart);
                actionQueue.add(attackerEnd);
                actionQueue.add(turnBookend);
                actionQueue.add(roundBookend);
                cycle();
                break;
            case REVIVAL:
                cardIDS.get(0).card.resetHealth();
                state = State.TURN;
                cycle();
            default:
                break;
        }
    }

    public void cycle() {
        actionQueue.remove().accept(this);
    }

    public void actionSelection(int entityID, CardID cardID, int index) {
        if(attacker.entityID != entityID || cardID != attacker.cardIDS.get(0)) {
            return;
        }

        int idx = actionQueue.indexOf(attackerEnd);
        actionQueue.add(idx, attacker.cardIDS.get(0).card.cardActions[index].action);
        cycle();
    }

    public void addAfterRound(Consumer<BattleManager> consumer) {
        int idx = actionQueue.indexOf(roundBookend);
        actionQueue.add(idx + 1, consumer);
    }

    public void addAfterTurn(Consumer<BattleManager> consumer) {
        int idx = actionQueue.indexOf(turnBookend);
        actionQueue.add(idx + 1, consumer);
    }

    private void init(int entityID, ArrayList<CardID> cardIDS) {
        BattlePlayer player = attacker.entityID == entityID ? attacker : defender;
        if(player.cardIDS == null) {
            player.cardIDS = cardIDS;
        }
        state = State.START;
    }

    private void swapPlayers() {
        BattlePlayer tmp = attacker;
        attacker = defender;
        defender = tmp;
    }

    private void determineFirstPlayer() {
        int minHealth = Integer.MAX_VALUE;
        CardID minCard = null;
        for(CardID cardID : attacker.cardIDS) {
            if(cardID.card.health < minHealth) {
                minHealth = cardID.card.health;
                minCard = cardID;
            }
        }

        for(CardID cardID : defender.cardIDS) {
            if(cardID.card.health < minHealth) {
                minHealth = cardID.card.health;
                minCard = cardID;
            }
        }

        if(defender.cardIDS.contains(minCard)) {
            swapPlayers();
        }
        state = State.TURN;
    }

    public void sendToBoth(String s) {
        attacker.sendMessage(s);
        defender.sendMessage(s);
    }




}
