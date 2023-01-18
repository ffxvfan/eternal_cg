package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.cards.CardID;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CardSelectionResponse implements ICardResponsePacket {
    private final int numCards;
    public final ArrayList<CardID> cardIDS;

    public CardSelectionResponse(ArrayList<CardID> cardIDS) {
        this.numCards = cardIDS.size();
        this.cardIDS = cardIDS;
    }

    public static void encode(CardSelectionResponse msg, PacketBuffer buffer) {
        buffer.writeInt(msg.numCards);
        for(int i = 0; i < msg.numCards; i++) {
            buffer.writeEnum(msg.cardIDS.get(i));
        }
    }

    public static CardSelectionResponse decode(PacketBuffer buffer) {
        int numCards = buffer.readInt();
        ArrayList<CardID> tmp_cardIDS = new ArrayList<>();
        for(int i = 0; i < numCards; i++) {
            tmp_cardIDS.add(buffer.readEnum(CardID.class));
        }
        return new CardSelectionResponse(tmp_cardIDS);
    }

    public static void handle(CardSelectionResponse msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> BattleManagerFactory.dispatchResponse(ctx.get().getSender(), msg));
        ctx.get().setPacketHandled(true);
    }
}
