package com.gotenks.eternal_cg.cards;

import com.gotenks.eternal_cg.actions.CardAttack;
import com.gotenks.eternal_cg.actions.ICardAction;
import com.gotenks.eternal_cg.network.CardDisplayRequest;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.types.Type;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import static com.gotenks.eternal_cg.init.ItemsInit.EternalCGTab;

public class Card extends Item {

    public String name;
    public final int maxHP;
    public int HP;
    public ICardAction majorAttack;
    public ICardAction minorAttack;
    public ICardAction buff;

    public Card(String name, int maxHP, ICardAction majorAttack, ICardAction minorAttack, ICardAction buff) {
        super(new Item.Properties().tab(EternalCGTab));
        this.name = name;
        this.maxHP = this.HP = maxHP;
        this.majorAttack = majorAttack;
        this.minorAttack = minorAttack;
        this.buff = buff;
    }

    public void resetHP() {
        HP = maxHP;
    }

    public Type majorType() {
        return ((CardAttack)majorAttack).type;
    }

    public Type minorType() {
        return ((CardAttack)minorAttack).type;
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(!world.isClientSide) {
            CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new CardDisplayRequest(CardID.nameToCardID(name)));
        }
        return ActionResult.success(itemStack);
    }
}
