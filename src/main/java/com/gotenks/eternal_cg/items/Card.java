package com.gotenks.eternal_cg.items;

import com.gotenks.eternal_cg.cards.CardAction;
import com.gotenks.eternal_cg.cards.CardPassive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Optional;

public class Card extends Item {

    public final CardPassive[] cardPassive;
    public int health;
    public CardAction[] cardActions;
    private boolean selected = false;

    public Card(Properties properties, int health, CardAction[] cardActions, CardPassive[] cardPassives) {
        super(properties);
        this.health = health;
        this.cardActions = cardActions;
        this.cardPassive = cardPassives;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(world.isClientSide) return ActionResult.pass(itemStack);
        selected = !selected;

        return ActionResult.success(itemStack);
    }
}
