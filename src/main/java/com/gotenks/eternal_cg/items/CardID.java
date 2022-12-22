package com.gotenks.eternal_cg.items;

import com.gotenks.eternal_cg.EternalCG;
import com.gotenks.eternal_cg.actions.CardAction;
import com.gotenks.eternal_cg.actions.CardAttack;
import com.gotenks.eternal_cg.battle.BattleManager;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.ShowCardSelectionScreenPacket;
import com.gotenks.eternal_cg.types.Type;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Objects;

import static com.gotenks.eternal_cg.init.ItemsInit.EternalCGTab;

public enum CardID {
    NEKOMANCER("nekomancer", new Card(new Item.Properties().tab(EternalCGTab), 340, new CardAction[]{
            new CardAttack("Chilled Rush", "56atk", Type.ICE, 56, battleManager -> {}),
            new CardAttack("Vengeance", "27atk, slowly deal +8atk over next two plays.", Type.MOON, 27, battleManager -> {
                battleManager.attacker.sendMessage("Vengeance effect will be applied on the next two rounds");
                battleManager.addAfterRound(battleManager1 -> {
                    battleManager1.sendToBoth("Vengeance effect: +8atk");
                    battleManager1.defender.cardIDS.get(0).card.health -= 8;
                    battleManager1.addAfterRound(battleManager2 -> {
                        battleManager2.sendToBoth("Vengeance effect: +8atk");
                        battleManager2.defender.cardIDS.get(0).card.health -= 8;
                        battleManager2.attacker.sendMessage("Vengeance effect has worn off...");
                    });
                });
            }),
            new CardAction("Raise Undead", "Raise one dead card. Can be used once per round.", battleManager -> {
                battleManager.attacker.sendMessage("Select a card to revive.");
                CardPacketHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> battleManager.attacker.getServerEntity()),
                        new ShowCardSelectionScreenPacket((ArrayList<CardID>) battleManager.attacker.cardIDS.stream().filter(cardID -> cardID.card.health < 0), 150, 200, 2, 1));
                battleManager.state = BattleManager.State.REVIVAL;
            })
    }, null)),

    BIGGIE_JIMMY("biggie_jimmy", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    BRUDZEYNOLRUT("brudzeynolrut", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    DOMINIC("dominic", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    DRAGN("dragn", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    EMILY("emily", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    HAZY("hazy", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    HELLHOUND("hellhound", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
    MOTHERBOARD("motherboard", new Card(new Item.Properties().tab(EternalCGTab), 100, null, null)),
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
        card.name = this.name;
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
