package com.gotenks.eternal_cg.items;

import com.gotenks.eternal_cg.actions.CardAction;
import com.gotenks.eternal_cg.actions.CardPassive;
import com.gotenks.eternal_cg.screen.CardDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class Card extends Item {

    public final int MAX_HEALTH;
    public int health;
    public String name = "";
    public final CardPassive[] cardPassive;
    public CardAction[] cardActions;

    public Card(Properties properties, int health, CardAction[] cardActions, CardPassive[] cardPassives) {
        super(properties);
        this.health = MAX_HEALTH = health;
        this.cardActions = cardActions;
        this.cardPassive = cardPassives;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(world.isClientSide) {
            Minecraft.getInstance().setScreen(new CardDisplay(CardID.getCardByName(name)));
        }

        return ActionResult.success(itemStack);
    }

    public void resetHealth() {
        health = MAX_HEALTH;
    }
}
