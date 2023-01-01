package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.items.CardID;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CardDisplayResponsePacket {

    private final CardID cardID;
    private final int index;

    public CardDisplayResponsePacket(CardID cardID, int index) {
        this.cardID = cardID;
        this.index = index;
    }

    public static void encode(CardDisplayResponsePacket message, PacketBuffer buffer) {
        buffer.writeEnum(message.cardID);
        buffer.writeInt(message.index);
    }

    public static CardDisplayResponsePacket decode(PacketBuffer buffer) {
        CardID cardID = buffer.readEnum(CardID.class);
        int index = buffer.readInt();
        return new CardDisplayResponsePacket(cardID, index);
    }

    public static void handle(CardDisplayResponsePacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> BattleManagerFactory.actionSelection(ctx.get().getSender(), message.cardID, message.index));
        ctx.get().setPacketHandled(true);
    }
}
