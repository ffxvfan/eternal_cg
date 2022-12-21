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

    public ShowCardSelectionScreenPacket() {

    }

    public static void encode(ShowCardSelectionScreenPacket message, PacketBuffer buffer) {

    }

    public static ShowCardSelectionScreenPacket decode(PacketBuffer buffer) {
        return new ShowCardSelectionScreenPacket();
    }

    public static void handle(ShowCardSelectionScreenPacket message, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, (DistExecutor.SafeSupplier<SafeRunnable>) () -> new SafeRunnable() {
            @Override
            public void run() {
                Minecraft.getInstance().setScreen(new CardSelectionScreen(
                        (ArrayList<CardID>)Minecraft.getInstance().player.inventory.items.stream().filter(e -> CardID.getCardByName(e.getItem().toString()) != null))
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
