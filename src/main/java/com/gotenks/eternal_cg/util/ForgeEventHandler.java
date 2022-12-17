package com.gotenks.eternal_cg.util;

import com.gotenks.eternal_cg.EternalCG;
import com.gotenks.eternal_cg.command.CardCommand;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EternalCG.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void onKeyPress(InputEvent.KeyInputEvent event) {

    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(CardCommand.register());
    }
}
