package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.items.CardID;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CardDisplayResponsePacket {

    private final int entityID;
    private final CardID cardID;
    private final int index;

    public CardDisplayResponsePacket(int entityID, CardID cardID, int index) {
        this.entityID = entityID;
        this.cardID = cardID;
        this.index = index;
    }

    public static void encode(CardDisplayResponsePacket message, PacketBuffer buffer) {
        buffer.writeInt(message.entityID);
        buffer.writeEnum(message.cardID);
        buffer.writeInt(message.index);
    }

    public static CardDisplayResponsePacket decode(PacketBuffer buffer) {
        int entityID = buffer.readInt();
        CardID cardID = buffer.readEnum(CardID.class);
        int index = buffer.readInt();
        return new CardDisplayResponsePacket(entityID, cardID, index);
    }

    public static void handle(CardDisplayResponsePacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> BattleManagerFactory.actionSelection(message.entityID, message.cardID, message.index));
        ctx.get().setPacketHandled(true);
    }
}
