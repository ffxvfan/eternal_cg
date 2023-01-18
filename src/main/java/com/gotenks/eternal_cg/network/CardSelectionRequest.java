package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.cards.CardID;
import com.gotenks.eternal_cg.screen.CardSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class CardSelectionRequest {

    private final int numCards;
    private final ArrayList<CardID> cardIDS;
    private final int width;
    private final int height;
    private final int maxCols;
    private final int maxSelections;

    public CardSelectionRequest(ArrayList<CardID> cardIDS, int width, int height, int maxCols, int maxSelections) {
        this.numCards = cardIDS.size();
        this.cardIDS = cardIDS;
        this.width = width;
        this.height = height;
        this.maxCols = maxCols;
        this.maxSelections = maxSelections;
    }

    public static void encode(CardSelectionRequest msg, PacketBuffer buffer) {
        buffer.writeInt(msg.numCards);
        for(int i = 0; i < msg.numCards; i++) {
            buffer.writeEnum(msg.cardIDS.get(i));
        }
        buffer.writeInt(msg.width);
        buffer.writeInt(msg.height);
        buffer.writeInt(msg.maxCols);
        buffer.writeInt(msg.maxSelections);
    }

    public static CardSelectionRequest decode(PacketBuffer buffer) {
        int numCards = buffer.readInt();
        ArrayList<CardID> tmp_cardIDS = new ArrayList<>();
        for(int i = 0; i < numCards; i++) {
            tmp_cardIDS.add(buffer.readEnum(CardID.class));
        }
        int width = buffer.readInt();
        int height = buffer.readInt();
        int maxCols = buffer.readInt();
        int maxSelections = buffer.readInt();
        return new CardSelectionRequest(tmp_cardIDS, width, height, maxCols, maxSelections);
    }

    public static void handle(CardSelectionRequest msg, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, (DistExecutor.SafeSupplier<SafeRunnable>) () -> new SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new CardSelectionScreen(msg.cardIDS, msg.width, msg.height, msg.maxCols, msg.maxSelections));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
