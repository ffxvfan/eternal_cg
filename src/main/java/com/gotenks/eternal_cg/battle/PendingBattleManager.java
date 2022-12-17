package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.BattleStatePacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;

public class PendingBattleManager {

    public static IFormattableTextComponent output(String s) {
        return new StringTextComponent(s).withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    }

    private static class Container {
        ServerPlayerEntity player1;
        ServerPlayerEntity player2;

        public Container(ServerPlayerEntity player1, ServerPlayerEntity player2) {
            this.player1 = player1;
            this.player2 = player2;
        }

        public boolean contains(ServerPlayerEntity player) {
            return player1 == player || player2 == player;
        }
    }

    private static ArrayList<Container> pending;

    public static boolean isPending(ServerPlayerEntity player) {
        for(Container container : pending) {
            if(container.contains(player)) {
                return true;
            }
        }
        return false;
    }

    public static void add(ServerPlayerEntity player1, ServerPlayerEntity player2) {
        if(isPending(player1)) {
            player1.sendMessage(output("You are already waiting for a battle..."), player1.getUUID());
            return;
        }

        if(isPending(player2)) {
            player1.sendMessage(output(player2.getScoreboardName() + " is currently deliberating battle request approval..."), player1.getUUID());
            return;
        }
        pending.add(new Container(player1, player2));
    }

    public static void remove(ServerPlayerEntity player) {
        Container toRemove = null;
        boolean wasRemoved = false;
        System.out.println("reach1");
        for(Container container : pending) {
            if(container.contains(player)) {
                System.out.println("reach3");

                toRemove = container;
                wasRemoved = pending.remove(container);
                break;
            }
        }

        if(wasRemoved) {
            toRemove.player1.sendMessage(output("Pending battle removed."), toRemove.player1.getUUID());
            toRemove.player2.sendMessage(output("Pending battle removed."), toRemove.player2.getUUID());
        } else {
            System.out.println("reach2");

            player.sendMessage(output("No pending battle requests..."), player.getUUID());
        }
    }

    public static void publish(ServerPlayerEntity player) {
        for(Container container : pending) {
            if(container.contains(player)) {
                CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> container.player1), new BattleStatePacket(BattleManagerFactory.State.SELECTING));
                CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> container.player2), new BattleStatePacket(BattleManagerFactory.State.SELECTING));
                pending.remove(container);
                break;
            }
        }
    }
}
