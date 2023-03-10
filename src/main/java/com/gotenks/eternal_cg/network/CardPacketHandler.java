package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.EternalCG;
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
        INSTANCE.registerMessage(0, CardSelectionRequest.class, CardSelectionRequest::encode, CardSelectionRequest::decode, CardSelectionRequest::handle);
        INSTANCE.registerMessage(1, CardSelectionResponse.class, CardSelectionResponse::encode, CardSelectionResponse::decode, CardSelectionResponse::handle);
        INSTANCE.registerMessage(2, CardDisplayRequest.class, CardDisplayRequest::encode, CardDisplayRequest::decode, CardDisplayRequest::handle);
        INSTANCE.registerMessage(3, CardDisplayResponse.class, CardDisplayResponse::encode, CardDisplayResponse::decode, CardDisplayResponse::handle);
    }
}
