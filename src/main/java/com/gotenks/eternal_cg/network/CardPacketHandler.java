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
        INSTANCE.registerMessage(0, BattleStatePacket.class, BattleStatePacket::encode, BattleStatePacket::decode, BattleStatePacket::handle);
        INSTANCE.registerMessage(1, BattleInitPacket.class, BattleInitPacket::encode, BattleInitPacket::decode, BattleInitPacket::handle);
    }

}
