package com.gotenks.eternal_cg.network;

import com.gotenks.eternal_cg.battle.BattleManager;
import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.screen.CardScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BattleStatePacket {

    public BattleManagerFactory.State state;

    public BattleStatePacket(BattleManagerFactory.State state) {
        this.state = state;
    }

    public static void encode(BattleStatePacket message, PacketBuffer buffer) {
        buffer.writeEnum(message.state);
    }

    public static BattleStatePacket decode(PacketBuffer buffer) {
        return new BattleStatePacket(buffer.readEnum(BattleManagerFactory.State.class));
    }

    public static void handle(BattleStatePacket message, Supplier<NetworkEvent.Context> ctx) {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, (DistExecutor.SafeSupplier<SafeRunnable>) () -> handleClient(message, ctx));
        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, (DistExecutor.SafeSupplier<SafeRunnable>) () -> handleServer(message, ctx));
        ctx.get().setPacketHandled(true);
    }

    private static SafeRunnable handleServer(BattleStatePacket message, Supplier<NetworkEvent.Context> ctx) {
        return new SafeRunnable() {
            @Override
            public void run() {
                ctx.get().enqueueWork(() -> {

                });
            }
        };
    }

    private static SafeRunnable handleClient(BattleStatePacket message, Supplier<NetworkEvent.Context> ctx) {
        return new SafeRunnable() {
            @Override
            public void run() {
                ctx.get().enqueueWork(() -> {
                    switch(message.state) {
                        case SELECTING:
                            Minecraft.getInstance().setScreen(new CardScreen(Minecraft.getInstance().player.inventory));
                            break;
                        default:
                            break;
                    }
                });
            }
        };
    }
}
