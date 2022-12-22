package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.ShowCardSelectionScreenPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.HashMap;

public class PendingBattleManager {
    private static final HashMap<ServerPlayerEntity, ServerPlayerEntity> pending = new HashMap<>();

    private static boolean isPending(ServerPlayerEntity player) {
        return pending.containsKey(player) || pending.containsValue(player);
    }

    public static boolean add(ServerPlayerEntity player1, ServerPlayerEntity player2) {
        if((isPending(player1) || isPending(player2)) || player1 == player2) {
            return false;
        }
        pending.put(player1, player2);
        return true;
    }

    public static boolean remove(ServerPlayerEntity player1, ServerPlayerEntity player2) {
        if(!isPending(player1) || !isPending(player2)) {
            return false;
        }
        pending.remove(player1);
        return true;
    }

    public static boolean publish(ServerPlayerEntity player1, ServerPlayerEntity player2) {
        if(pending.containsKey(player1) || !pending.containsValue(player1)) {
            return false;
        }

        ArrayList<CardID> player1Cards = (ArrayList<CardID>) player1.inventory.items.stream().filter(e -> CardID.getCardByName(e.getItem().toString()) != null);
        CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player1), new ShowCardSelectionScreenPacket(player1Cards, 60, 85, 5, 3));

        ArrayList<CardID> player2Cards = (ArrayList<CardID>) player2.inventory.items.stream().filter(e -> CardID.getCardByName(e.getItem().toString()) != null);
        CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player2), new ShowCardSelectionScreenPacket(player2Cards, 60, 85, 5, 3));

        BattleManagerFactory.add(player1.getId(), player2.getId());
        remove(player1, player2);
        return true;
    }

    public static ArrayList<String> listAllRequests(ServerPlayerEntity player) {
        ArrayList<String> names = new ArrayList<>();
        pending.forEach((sender, receiver) -> {
            if(receiver == player) {
                names.add(sender.getScoreboardName());
            }
        });
        return names;
    }
}
