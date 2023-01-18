package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.cards.Card;
import com.gotenks.eternal_cg.cards.CardID;
import com.gotenks.eternal_cg.network.ICardResponsePacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class BattleManagerFactory {

    private static final HashMap<ServerPlayerEntity, BattleManager> factory = new HashMap<>();

    public static void dispatchResponse(ServerPlayerEntity entity, ICardResponsePacket packet) {
        BattleManager bm = factory.get(entity);
        if(bm != null) bm.dispatchResponse(entity, packet);
    }

    public static void add(ServerPlayerEntity p1, ServerPlayerEntity p2) {
        ArrayList<CardID> p1Cards = getCardsFromInventory(p1);
        ArrayList<CardID> p2Cards = getCardsFromInventory(p2);

        if(p1Cards.size() + p2Cards.size() < 6) {
            StringTextComponent msg = new StringTextComponent("Failed to start battle: not enough cards");
            p1.sendMessage(msg, p1.getUUID());
            p2.sendMessage(msg, p2.getUUID());
            return;
        }

        BattleManager bm = new BattleManager(new BattlePlayer(p1, p1Cards), new BattlePlayer(p2, p2Cards));
        factory.put(p1, bm);
        factory.put(p2, bm);
    }

    public static BattleManager getBattleManager(ServerPlayerEntity entity) {
        return factory.get(entity);
    }

    private static ArrayList<CardID> getCardsFromInventory(ServerPlayerEntity player) {
        return (ArrayList<CardID>) player.inventory.items.stream()
                .filter(e -> e.getItem() instanceof Card)
                .map(e -> CardID.nameToCardID(((Card) e.getItem()).name))
                .collect(Collectors.toList());
    }

    public static void remove(ServerPlayerEntity entity1, ServerPlayerEntity entity2) {
        factory.remove(entity1);
        factory.remove(entity2);
    }

    public static boolean contains(ServerPlayerEntity entity) {
        return factory.containsKey(entity);
    }
 }
