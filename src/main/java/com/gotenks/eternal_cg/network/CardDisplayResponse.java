package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.cards.CardID;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CardDisplayResponse implements ICardResponsePacket {

    public final CardID cardID;
    public final int index;

    public CardDisplayResponse(CardID cardID, int index) {
        this.cardID = cardID;
        this.index = index;
    }

    public static void encode(CardDisplayResponse msg, PacketBuffer buffer) {
        buffer.writeEnum(msg.cardID);
        buffer.writeInt(msg.index);
    }

    public static CardDisplayResponse decode(PacketBuffer buffer) {
        CardID cardID = buffer.readEnum(CardID.class);
        int index = buffer.readInt();
        return new CardDisplayResponse(cardID, index);
    }

    public static void handle(CardDisplayResponse msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> BattleManagerFactory.dispatchResponse(ctx.get().getSender(), msg));
        ctx.get().setPacketHandled(true);
    }
}
