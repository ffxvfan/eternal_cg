package com.gotenks.eternal_cg.util;

import com.gotenks.eternal_cg.EternalCG;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = EternalCG.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler {

    public static final KeyBinding[] keybinds = {
            new KeyBinding("key.eternal_cg.card_action_1", GLFW.GLFW_KEY_1, "key.category.eternal_cg"),
            new KeyBinding("key.eternal_cg.card_action_2", GLFW.GLFW_KEY_2, "key.category.eternal_cg"),
            new KeyBinding("key.eternal_cg.card_action_3", GLFW.GLFW_KEY_3, "key.category.eternal_cg"),
    };

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        for (KeyBinding keybind : keybinds) {
            ClientRegistry.registerKeyBinding(keybind);
        }
    }
}
