package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.items.CardID;
import com.gotenks.eternal_cg.screen.CardDisplay;
import com.gotenks.eternal_cg.screen.CardSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ShowCardDisplayPacket {

    private final CardID cardID;

    public ShowCardDisplayPacket(CardID cardID) {
        this.cardID = cardID;
    }

    public static void encode(ShowCardDisplayPacket message, PacketBuffer buffer) {
        buffer.writeEnum(message.cardID);
    }

    public static ShowCardDisplayPacket decode(PacketBuffer buffer) {
        return new ShowCardDisplayPacket(buffer.readEnum(CardID.class));
    }

    public static void handle(ShowCardDisplayPacket message, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, (DistExecutor.SafeSupplier<SafeRunnable>) () -> new SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new CardDisplay(message.cardID));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
