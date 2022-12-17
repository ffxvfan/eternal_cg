package com.gotenks.eternal_cg;

import com.gotenks.eternal_cg.init.ItemsInit;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EternalCG.MOD_ID)
public class EternalCG
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "eternal_cg";

    public EternalCG() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::setup);

        ItemsInit.ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void setup(final FMLCommonSetupEvent event) {
        CardPacketHandler.init();
    }
}
