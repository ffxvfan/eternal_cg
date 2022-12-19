package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.screen.CardSelectableIcon;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class BattleInitPacket {

    public CardID[] cardIDS;

    public BattleInitPacket(CardID[] cardIDS) {
        this.cardIDS = cardIDS;
    }

    public static void encode(BattleInitPacket message, PacketBuffer buffer) {
        buffer.writeEnum(message.cardIDS[0]);
        buffer.writeEnum(message.cardIDS[1]);
        buffer.writeEnum(message.cardIDS[2]);
    }

    public static BattleInitPacket decode(PacketBuffer buffer) {
        return new BattleInitPacket(new CardID[]{buffer.readEnum(CardID.class), buffer.readEnum(CardID.class), buffer.readEnum(CardID.class)});
    }

    public static void handle(BattleInitPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            BattleManagerFactory.init(message.cardIDS);
        });
        ctx.get().setPacketHandled(true);
    }
}
