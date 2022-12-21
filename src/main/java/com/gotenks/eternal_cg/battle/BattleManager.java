package com.gotenks.eternal_cg.battle;

import com.gotenks.eternal_cg.EternalCG;
import com.gotenks.eternal_cg.cards.CardAction;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.ShowCardSelectionScreenPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.PriorityQueue;

public class BattleManager {
    public BattlePlayer player1;
    public BattlePlayer player2;

    public PriorityQueue<CardAction> actionQueue = new PriorityQueue<>();

    public BattleManager() {

    }

    public void init(BattlePlayer player) {
        if(player1 == null) {
            player1 = player;
        } else if(player2 == null) {
            player2 = player;
            start();
        }
    }

    public void start() {
        EternalCG.LOGGER.info("WELL WE MADE IT THIS FAR");
    }

    public void endBattle() {
        //BattleManagerFactory.removeBattle(this);
    }
}
