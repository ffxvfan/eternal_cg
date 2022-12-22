package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.EternalCG;
import com.gotenks.eternal_cg.screen.CardDisplay;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class CardPacketHandler {
    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(EternalCG.MOD_ID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(0, ShowCardSelectionScreenPacket.class, ShowCardSelectionScreenPacket::encode, ShowCardSelectionScreenPacket::decode, ShowCardSelectionScreenPacket::handle);
        INSTANCE.registerMessage(1, CardSelectionResponsePacket.class, CardSelectionResponsePacket::encode, CardSelectionResponsePacket::decode, CardSelectionResponsePacket::handle);
        INSTANCE.registerMessage(2, ShowCardDisplayPacket.class, ShowCardDisplayPacket::encode, ShowCardDisplayPacket::decode, ShowCardDisplayPacket::handle);
        INSTANCE.registerMessage(3, CardDisplayResponsePacket.class, CardDisplayResponsePacket::encode, CardDisplayResponsePacket::decode, CardDisplayResponsePacket::handle);
    }

}
