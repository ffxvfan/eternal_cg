package com.gotenks.eternal_cg.items;

import com.gotenks.eternal_cg.EternalCG;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

import static com.gotenks.eternal_cg.init.ItemsInit.EternalCGTab;

public enum CardID {
    BIGGIE_JIMMY("biggie_jimmy", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    BRUDZEYNOLRUT("brudzeynolrut", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    DOMINIC("dominic", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    DRAGN("dragn", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    EMILY("emily", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    HAZY("hazy", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    HELLHOUND("hellhound", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    MOTHERBOARD("motherboard", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    NEKOMANCER("nekomancer", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    NIRKOIIZSTRUN("nirkoiizstrun", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    NIRKOYOL("nirkoyol", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    SPIRIT("spirit", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    TENKS("tenks", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    TICKING_ENTITY("ticking_entity", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    YA_TE_VEO("ya_te_veo", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null));

    public final String name;
    public final Card card;

    CardID(String name, Card card) {
        this.name = name;
        this.card = card;
    }

    public ResourceLocation getResourceLocation() {
        return new ResourceLocation(EternalCG.MOD_ID, "textures/items/" + name + ".png");
    }

    //TODO: HACK delete this when card bag impl
    public static CardID getCardByName(String name) {
        for (CardID cardID : values()) {
            if(Objects.equals(name.toLowerCase(), cardID.name.toLowerCase())) {
                return cardID;
            }
        }
        return null;
    }
}
