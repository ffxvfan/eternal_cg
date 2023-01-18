package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.cards.CardID;
import com.gotenks.eternal_cg.screen.CardDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CardDisplayRequest {

    private final CardID cardID;

    public CardDisplayRequest(CardID cardID) {
        this.cardID = cardID;
    }

    public static void encode(CardDisplayRequest msg, PacketBuffer buffer) {
        buffer.writeEnum(msg.cardID);
    }

    public static CardDisplayRequest decode(PacketBuffer buffer) {
        return new CardDisplayRequest(buffer.readEnum(CardID.class));
    }

    public static void handle(CardDisplayRequest msg, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, (DistExecutor.SafeSupplier<SafeRunnable>) () -> new SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new CardDisplay(msg.cardID));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
