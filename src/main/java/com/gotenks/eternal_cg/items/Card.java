package com.gotenks.eternal_cg.items;

import com.gotenks.eternal_cg.actions.*;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.ShowCardDisplayPacket;
import com.gotenks.eternal_cg.types.Type;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;

public class Card extends Item {

    public final int MAX_HEALTH;
    public int health;
    public String displayName;
    public String name;
    public Type major;
    public Type minor;
    public ArrayList<CardAction> cardActions;
    public ArrayList<CardPassive> cardPassives;

    public Card(Properties properties, String displayName, int health, Type major, Type minor, ArrayList<CardAction> cardActions, ArrayList<CardPassive> cardPassives) {
        super(properties);
        this.displayName = displayName;
        this.health = MAX_HEALTH = health;
        this.major = major;
        this.minor = minor;
        this.cardActions = cardActions;
        this.cardPassives = cardPassives;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!world.isClientSide) {
            CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new ShowCardDisplayPacket(CardID.getCardByName(name)));
        }

        return ActionResult.success(itemStack);
    }

    public void reset() {
        health = MAX_HEALTH;
        cardActions.forEach(cardAction -> {
            if(cardAction instanceof ILimited) {
                ((ILimited) cardAction).reset();
            }
        });
    }
}
