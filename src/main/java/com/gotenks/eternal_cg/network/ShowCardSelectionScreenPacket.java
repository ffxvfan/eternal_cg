package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.screen.CardSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ShowCardSelectionScreenPacket {

    private final int numCards;
    private final ArrayList<CardID> cardIDS;
    private final int width;
    private final int height;
    private final int maxCols;
    private final int maxSelections;

    public ShowCardSelectionScreenPacket(ArrayList<CardID> cardIDS, int width, int height, int maxCols, int maxSelections) {
        this.numCards = cardIDS.size();
        this.cardIDS = cardIDS;
        this.width = width;
        this.height = height;
        this.maxCols = maxCols;
        this.maxSelections = maxSelections;
    }

    public static void encode(ShowCardSelectionScreenPacket message, PacketBuffer buffer) {
        buffer.writeInt(message.numCards);
        for(int i = 0; i < message.numCards; i++) {
            buffer.writeEnum(message.cardIDS.get(i));
        }
        buffer.writeInt(message.width);
        buffer.writeInt(message.height);
        buffer.writeInt(message.maxCols);
        buffer.writeInt(message.maxSelections);
    }

    public static ShowCardSelectionScreenPacket decode(PacketBuffer buffer) {
        int numCards = buffer.readInt();
        ArrayList<CardID> tmp_cardIDS = new ArrayList<>();
        for(int i = 0; i < numCards; i++) {
            tmp_cardIDS.add(buffer.readEnum(CardID.class));
        }
        int width = buffer.readInt();
        int height = buffer.readInt();
        int maxCols = buffer.readInt();
        int maxSelections = buffer.readInt();
        return new ShowCardSelectionScreenPacket(tmp_cardIDS, width, height, maxCols, maxSelections);
    }

    public static void handle(ShowCardSelectionScreenPacket message, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, (DistExecutor.SafeSupplier<SafeRunnable>) () -> new SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new CardSelectionScreen(message.cardIDS, message.width, message.height, message.maxCols, message.maxSelections));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
