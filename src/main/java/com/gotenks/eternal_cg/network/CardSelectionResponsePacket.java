package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.items.CardID;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CardSelectionResponsePacket {
    private final int numCards;
    private final ArrayList<CardID> cardIDS;

    public CardSelectionResponsePacket(ArrayList<CardID> cardIDS) {
        this.numCards = cardIDS.size();
        this.cardIDS = cardIDS;
    }

    public static void encode(CardSelectionResponsePacket message, PacketBuffer buffer) {
        buffer.writeInt(message.numCards);
        for(int i = 0; i < message.numCards; i++) {
            buffer.writeEnum(message.cardIDS.get(i));
        }
    }

    public static CardSelectionResponsePacket decode(PacketBuffer buffer) {
        int numCards = buffer.readInt();
        ArrayList<CardID> tmp_cardIDS = new ArrayList<>();
        for(int i = 0; i < numCards; i++) {
            tmp_cardIDS.add(buffer.readEnum(CardID.class));
        }
        return new CardSelectionResponsePacket(tmp_cardIDS);
    }

    public static void handle(CardSelectionResponsePacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> BattleManagerFactory.update(ctx.get().getSender(), message.cardIDS));
        ctx.get().setPacketHandled(true);
    }
}
