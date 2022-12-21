package com.gotenks.eternal_cg.battle;

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
        CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player1), new ShowCardSelectionScreenPacket());
        CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player2), new ShowCardSelectionScreenPacket());
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
