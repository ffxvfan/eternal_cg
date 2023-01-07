package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.EternalCG;
import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.ShowCardSelectionScreenPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BattleManager {

    public enum State {
        INIT,
        START,
        REVIVAL,
        TURN,
        SKIP,
        SELECT,
    }

    public BattlePlayer attacker;
    public BattlePlayer defender;
    public State state;
    public int turnCount = 0;

    public LinkedList<Consumer<BattleManager>> actionQueue = new LinkedList<>();
    public Consumer<BattleManager> turnBookend;
    public Consumer<BattleManager> roundBookend;
    public Consumer<BattleManager> attackStart;
    public Consumer<BattleManager> attackEnd;


    public BattleManager(BattlePlayer attacker, BattlePlayer defender) {
        this.attacker = attacker;
        this.defender = defender;
        this.state = State.INIT;

        attackStart = battleManager -> {
            battleManager.attacker.sendSystemMessage("Your turn. Select an attack for " + battleManager.attacker.getCard().displayName + ".");
            battleManager.defender.sendSystemMessage("Your opponent is selecting an attack...");
            EternalCG.LOGGER.info("ATTACK_START");
            EternalCG.LOGGER.info(this::toString);
        };

        attackEnd = battleManager -> {
            battleManager.sendSystemMessageToBoth(battleManager.defender.getCard().displayName + " HP: " + battleManager.defender.getCard().health);
            if(battleManager.defender.getCard().health <= 0) {
                battleManager.sendSystemMessageToBoth(battleManager.defender.getCard().displayName + " has fainted!");
                battleManager.attacker.sendSystemMessage("Please wait for your opponent to select a new card.");
                ArrayList<CardID> cardIDS = (ArrayList<CardID>) battleManager.defender.cardIDS.stream().filter(cardID -> cardID.card.health > 0).collect(Collectors.toList());
                if(cardIDS.size() > 0) {
                    CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> battleManager.defender.player), new ShowCardSelectionScreenPacket(cardIDS, 60, 85, 5, 1));
                    state = State.SELECT;
                } else {
                    battleManager.sendSystemMessageToBoth(battleManager.attacker.player.getScoreboardName() + " WINS!");
                    battleManager.sendSystemMessageToBoth("GAME OVER. GGS");
                    battleManager.attacker.cardIDS.forEach(cardID -> cardID.card.reset());
                    battleManager.defender.cardIDS.forEach(cardID -> cardID.card.reset());
                    BattleManagerFactory.remove(battleManager.attacker.player, battleManager.defender.player);
                }
            } else {
                battleManager.defender.sendSystemMessage("Defense phase over.");
                battleManager.attacker.sendSystemMessage("Attack phase over.");
                battleManager.sendSystemMessageToBoth("Swapping roles.");
                battleManager.swapPlayers();
                battleManager.actionQueue.remove().accept(battleManager);
            }
            EternalCG.LOGGER.info("ATTACK_END");
            EternalCG.LOGGER.info(this::toString);
        };

        turnBookend = battleManager -> {
            battleManager.actionQueue.addFirst(battleManager.attackStart);
            battleManager.actionQueue.add(battleManager.actionQueue.indexOf(battleManager.roundBookend), battleManager.attackEnd);
            battleManager.actionQueue.add(battleManager.turnBookend);
            battleManager.turnCount++;
            battleManager.actionQueue.remove().accept(battleManager);
            if(battleManager.turnCount % 2 == 0) {
                battleManager.actionQueue.remove().accept(battleManager);
            }
            EternalCG.LOGGER.info("TURN_BOOKEND");
            EternalCG.LOGGER.info(this::toString);
        };

        roundBookend = battleManager -> {
            battleManager.actionQueue.add(battleManager.actionQueue.indexOf(battleManager.roundBookend) + 1, battleManager.attackStart);
            battleManager.actionQueue.add(battleManager.actionQueue.indexOf(battleManager.turnBookend), battleManager.attackEnd);
            battleManager.actionQueue.add(battleManager.roundBookend);
            battleManager.turnCount++;
            battleManager.sendSystemMessageToBoth("Round Over!");
            battleManager.actionQueue.remove().accept(battleManager);
            EternalCG.LOGGER.info("ROUND_BOOKEND");
            EternalCG.LOGGER.info(this::toString);
        };

        actionQueue.add(attackStart);
        actionQueue.add(attackEnd);
        actionQueue.add(turnBookend);
        actionQueue.add(roundBookend);
    }

    public void update(ServerPlayerEntity entity, ArrayList<CardID> cardIDS) {
        BattlePlayer player = attacker.player == entity ? attacker : defender;
        switch(state) {
            case INIT:
                if(player.cardIDS == null) {
                    player.cardIDS = cardIDS;
                }
                if(attacker.cardIDS == null || defender.cardIDS == null) {
                    break;
                }
                state = State.START;
            case START:
                sendSystemMessageToBoth("BATTLE START!");
                Optional<CardID> minAttacker = attacker.cardIDS.stream().min(Comparator.comparingInt(cardID -> cardID.card.health));
                Optional<CardID> minDefender = defender.cardIDS.stream().min(Comparator.comparingInt(cardID -> cardID.card.health));
                if(minAttacker.get().card.health > minDefender.get().card.health) {
                    swapPlayers();
                }
                state = State.TURN;
            case TURN:
                actionQueue.remove().accept(this);
                break;
            case REVIVAL:
                break;
            case SKIP:
                break;
            case SELECT:
                player.setFirstCard(cardIDS.get(0));
                state = State.TURN;
                actionQueue.remove().accept(this);
                break;
            default:
                break;
        }
    }

    public void actionSelection(ServerPlayerEntity entity, CardID cardID, int index) {
        if(entity != attacker.player || cardID != attacker.getCardID()) {
            return;
        }

        addToTurn(cardID.card.cardActions.get(index).action);
        if(cardID.card.cardPassives != null) {
            actionQueue.addAll(actionQueue.indexOf(attackEnd), cardID.card.cardPassives.stream().map(e -> e.passive).collect(Collectors.toList()));
        }

        while(!actionQueue.getFirst().equals(attackEnd)) {
            actionQueue.remove().accept(this);
        }
        actionQueue.remove().accept(this);
    }

    public void addToTurn(Consumer<BattleManager> consumer) {
        actionQueue.add(actionQueue.indexOf(attackEnd), consumer);
    }

    public void addNextTurn(Consumer<BattleManager> consumer) {
        actionQueue.add(actionQueue.indexOf(roundBookend), consumer);
    }

    public void addNextRound(Consumer<BattleManager> consumer) {
        actionQueue.add(consumer);
    }

    private void swapPlayers() {
        BattlePlayer tmp = attacker;
        attacker = defender;
        defender = tmp;
    }

    public void sendSystemMessageToBoth(String s) {
        attacker.sendSystemMessage(s);
        defender.sendSystemMessage(s);
    }

    public void sendAttackerMessageToBoth(String s) {
        attacker.player.sendMessage(new TranslationTextComponent("[%s] " + s, new StringTextComponent(attacker.player.getScoreboardName()).withStyle(TextFormatting.DARK_GREEN, TextFormatting.BOLD)), attacker.player.getUUID());
        defender.player.sendMessage(new TranslationTextComponent("[%s] " + s, new StringTextComponent(attacker.player.getScoreboardName()).withStyle(TextFormatting.DARK_RED, TextFormatting.BOLD)), defender.player.getUUID());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("*************\n");
        for(Consumer<BattleManager> consumer : actionQueue) {
            if (attackStart.equals(consumer)) {
                stringBuilder.append("attackStart\n");
            } else if(attackEnd.equals(consumer)) {
                stringBuilder.append("attackEnd\n");
            } else if(turnBookend.equals(consumer)) {
                stringBuilder.append("turnBookend\n");
            } else if(roundBookend.equals(consumer)) {
                stringBuilder.append("roundBookend\n");
            } else {
                stringBuilder.append(consumer.toString()).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
