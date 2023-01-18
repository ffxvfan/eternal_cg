package com.gotenks.eternal_cg.init;

import com.gotenks.eternal_cg.EternalCG;
import com.gotenks.eternal_cg.cards.CardID;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

public class ItemsInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EternalCG.MOD_ID);

    public static ItemGroup EternalCGTab = new ItemGroup("eternal_cg_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CardID.NEKOMANCER.card);
        }
    };

    static {
        Arrays.stream(CardID.values()).forEach(id -> ITEMS.register(id.name(), () -> id.card));
    }
}
