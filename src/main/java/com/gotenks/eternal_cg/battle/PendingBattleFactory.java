package com.gotenks.eternal_cg.battle;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.*;
import java.util.stream.Stream;

public class PendingBattleFactory {

    // Represents a player and their sent-request pool
    private static final HashMap<ServerPlayerEntity, HashSet<ServerPlayerEntity>> pending = new HashMap<>();

    public static void add(ServerPlayerEntity sender, ServerPlayerEntity receiver) {
        if(!pending.containsKey(sender)) pending.put(sender, new HashSet<>());
        pending.get(sender).add(receiver);
    }

    public static void remove(ServerPlayerEntity sender, ServerPlayerEntity receiver) {
        if(pending.containsKey(receiver) && pending.get(receiver).contains(sender)) {
            pending.remove(sender);
            pending.remove(receiver);
            BattleManagerFactory.add(sender, receiver);
        }
    }

    public static Stream<String> listOutgoingRequests(ServerPlayerEntity player) {
        return pending.get(player).stream().map(PlayerEntity::getScoreboardName);
    }

    public static Stream<String> listIncomingRequests(ServerPlayerEntity player) {
        return pending.keySet().stream().filter(key -> pending.get(key).contains(player)).map(PlayerEntity::getScoreboardName);
    }
}
