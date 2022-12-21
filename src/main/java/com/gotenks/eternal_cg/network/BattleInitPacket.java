package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.battle.BattlePlayer;
import com.gotenks.eternal_cg.items.CardID;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;

public class BattleInitPacket {
    public BattlePlayer player;

    public BattleInitPacket(int entityID, ArrayList<CardID> cards) {
        player = new BattlePlayer(entityID, cards);
    }

    public static void encode(BattleInitPacket message, PacketBuffer buffer) {
        for(CardID card : message.player.cards) {
            buffer.writeEnum(card);
        }
        buffer.writeInt(message.player.entityID);
    }

    public static BattleInitPacket decode(PacketBuffer buffer) {
        ArrayList<CardID> cards = new ArrayList<>(Arrays.asList(
                buffer.readEnum(CardID.class),
                buffer.readEnum(CardID.class),
                buffer.readEnum(CardID.class)
        ));
        int entityID = buffer.readInt();
        return new BattleInitPacket(entityID, cards);
    }

    public static void handle(BattleInitPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> BattleManagerFactory.init(message.player));
        ctx.get().setPacketHandled(true);
    }
}
