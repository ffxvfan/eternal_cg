package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.actions.ICardAction;
import com.gotenks.eternal_cg.cards.CardID;
import com.gotenks.eternal_cg.network.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BattleManager {

    public BattlePlayer attacker = null;
    public BattlePlayer defender = null;

    public LinkedList<BiConsumer<ServerPlayerEntity, ICardResponsePacket>> responseQueue = new LinkedList<>();
    public LinkedList<ICardAction> nextTurnEffects = new LinkedList<>();

    public StringBuilder currentTurnMessage = new StringBuilder();
    public int currentTurnDamage;

    public BattleManager(BattlePlayer attacker, BattlePlayer defender) {
        sendInitCardSelectionRequest(attacker);
        sendInitCardSelectionRequest(defender);

        responseQueue.add(this::getFirstPlayer);
        responseQueue.add(this::getSecondPlayer);
    }

    private void sendInitCardSelectionRequest(BattlePlayer player) {
        CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.player), new CardSelectionRequest(player.cardIDS, 60, 85, 7, 3));
    }

    private void sendCardSelectionForAliveCards(BattlePlayer player) {
        CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player.player), new CardSelectionRequest(
                (ArrayList<CardID>) player.cardIDS.stream().filter(id -> id.card.HP > 0).collect(Collectors.toList()), 60, 85, 7, 1
        ));
    }

    private void waitForNewCardSelection(ServerPlayerEntity entity, ICardResponsePacket packet) {
        // Only attacker can submit response
        if(entity != attacker.player || packet instanceof CardDisplayResponse) {
            responseQueue.add(this::waitForNewCardSelection);
            defender.sendSystemMessage("It is not your turn. Please wait for your opponent.");
            return;
        }

        attacker.setFirstCard(((CardSelectionResponse) packet).cardIDS.get(0));
    }

    public void dispatchResponse(ServerPlayerEntity entity, ICardResponsePacket packet) {
        responseQueue.remove().accept(entity, packet);
    }

    private void getFirstPlayer(ServerPlayerEntity entity, ICardResponsePacket packet) {
        attacker = new BattlePlayer(entity, ((CardSelectionResponse) packet).cardIDS);
    }

    private void getSecondPlayer(ServerPlayerEntity entity, ICardResponsePacket packet) {
        defender = new BattlePlayer(entity, ((CardSelectionResponse) packet).cardIDS);
        battleStart();
        if(attacker.getLowestHealthCardHP() > defender.getLowestHealthCardHP()) swapPlayers();
    }

    private void battleStart() {
        sendSystemMessageToBoth("BATTLE START");
        sendSystemMessageToBoth(attacker.player.getScoreboardName() + " VS " + defender.player.getScoreboardName());
        responseQueue.add(this::waitForCardAction);
    }

    public void applyDamageStep(int dmg, String msg) {
        currentTurnMessage.append(msg).append(": [+").append(dmg).append("], ");
        currentTurnDamage += dmg;
    }

    public void waitForCardAction(ServerPlayerEntity entity, ICardResponsePacket packet) {
        // Only attacker can submit response
        if(entity != attacker.player) {
            responseQueue.add(this::waitForCardAction);
            defender.sendSystemMessage("It is not your turn. Please wait for your opponent.");
            return;
        }

        // Only right card can submit response
        if(((CardDisplayResponse) packet).cardID != attacker.getCardID()) {
            responseQueue.add(this::waitForCardAction);
            attacker.sendSystemMessage("Please select an attack for " + attacker.getCard().name + ".");
            return;
        }

        // Unwind any effects from previous turn
        while(!nextTurnEffects.isEmpty()) {
            nextTurnEffects.remove().accept(this);
        }

        // Perform selected action
        switch(((CardDisplayResponse) packet).index) {
            case 0: attacker.getCard().majorAttack.accept(this); break;
            case 1: attacker.getCard().minorAttack.accept(this); break;
            case 2: attacker.getCard().buff.accept(this); break;
            default: break;
        }

        // Final check for any do-over attempts
        if(!responseQueue.isEmpty()) return;
        turnFinish();
    }

    private void turnFinish() {
        if(currentTurnDamage != 0) {
            currentTurnMessage.append("TOTAL DAMAGE: ").append(currentTurnDamage);
            sendOpposingMessageToBoth(currentTurnMessage.toString());
            defender.applyDamage(currentTurnDamage);
        }

        if(defenderHasCardsLeft()) {
            if(defender.getCard().HP <= 0) {
                sendAttackerMessageToBoth(defender.getCard().name + " has been slain!");
                responseQueue.add(this::waitForNewCardSelection);
            }
            currentTurnDamage = 0;
            currentTurnMessage.setLength(0);
            swapPlayers();
        } else {
            gameOver();
        }
    }

    private void gameOver() {
        sendSystemMessageToBoth("GAME OVER");
        sendSystemMessageToBoth(attacker.player.getScoreboardName() + "WINS. GGS");
        BattleManagerFactory.remove(attacker.player, defender.player);
    }

    private boolean defenderHasCardsLeft() {
        return defender.cardIDS.stream().anyMatch(id -> id.card.HP > 0);
    }

    private void swapPlayers() {
        BattlePlayer tmp = attacker;
        attacker = defender;
        defender = tmp;

        attacker.sendSystemMessage("You are attacking. Select an attack for " + attacker.getCard().name);
        defender.sendSystemMessage("You are defending. Please wait for your opponent to select an attack.");
    }

    public void sendSystemMessageToBoth(String s) {
        attacker.sendSystemMessage(s);
        defender.sendSystemMessage(s);
    }

    public void sendAttackerMessageToBoth(String s) {
        attacker.sendGoodMessage(s);
        defender.sendOpposingMessage(attacker.player.getScoreboardName(), s);
    }

    public void sendDefenderMessageToBoth(String s) {
        attacker.sendOpposingMessage(defender.player.getScoreboardName(), s);
        defender.sendGoodMessage(s);
    }

    public void sendOpposingMessageToBoth(String s) {
        attacker.sendOpposingMessage(defender.player.getScoreboardName(), s);
        defender.sendOpposingMessage(attacker.player.getScoreboardName(), s);
    }
}
