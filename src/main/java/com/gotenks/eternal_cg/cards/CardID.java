package com.gotenks.eternal_cg.cards;

import com.gotenks.eternal_cg.actions.*;
import com.gotenks.eternal_cg.battle.BattleManager;
import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.CardSelectionRequest;
import com.gotenks.eternal_cg.network.CardSelectionResponse;
import com.gotenks.eternal_cg.network.ICardResponsePacket;
import com.gotenks.eternal_cg.types.Type;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public enum CardID {
    NEKOMANCER("Nekomancer", 340,
            new CardAttack(new CardAttack.Builder()
                    .name("Chilled Rush")
                    .baseDmg(56)
                    .type(Type.ICE)),
            new CardAttack(new CardAttack.Builder()
                    .name("Vengeance Moon")
                    .description("slowly deal +8atk over next two plays")
                    .baseDmg(27)
                    .type(Type.MOON)
                    .withEffect(new CardPersistent(2, 8, "Vengeance Moon Effect"))),
            new ICardAction() {
                @Override
                public void accept(BattleManager bm) {
                    ArrayList<CardID> cardIDS = (ArrayList<CardID>) bm.attacker.cardIDS.stream().filter(cardID -> cardID.card.HP < 0).collect(Collectors.toList());
                    if(cardIDS.size() == 0) {
                        bm.responseQueue.add(bm::waitForCardAction);
                        bm.attacker.sendSystemMessage("No cards to revive. Please select another attack.");
                        return;
                    }
                    CardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> bm.attacker.player),
                            new CardSelectionRequest(cardIDS, 60, 85, cardIDS.size(), 1));
                    bm.responseQueue.add(this::revive);
                }

                private void revive(ServerPlayerEntity entity, ICardResponsePacket packet) {
                    BattleManager bm = BattleManagerFactory.getBattleManager(entity);
                    if(bm.attacker.player != entity) {
                        bm.responseQueue.add(this::revive);
                        bm.defender.sendSystemMessage("Opponent is selecting a card to revive. Please wait.");
                        return;
                    }
                    Card revived = ((CardSelectionResponse)packet).cardIDS.get(0).card;
                    revived.resetHP();
                    bm.sendOpposingMessageToBoth("[Raise Undead]: " + revived.name + " has been revived.");
                }
            }),

    DRAGN("DragN", 350,
            new CardAttack(new CardAttack.Builder()
                    .name("Flame Rush")
                    .type(Type.FIRE)
                    .baseDmg(53)
                    .withEffect(new CardPredicate(bm -> bm.attacker.getCard().HP <= 200, bm -> bm.applyDamageStep(8, "Rage Buff")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Slow Burn")
                    .type(Type.LAVA)
                    .description("slowly deal +5atk over the next two plays")
                    .baseDmg(10)
                    .withEffect(new CardPersistent(2, 5, "Slow Burn Effect"))
                    .withEffect(new CardPredicate(bm -> bm.attacker.getCard().HP <= 200, bm -> bm.applyDamageStep(8, "Rage Buff")))),
            null),

    EMILY("Emily", 330,
            new CardAttack(new CardAttack.Builder()
                    .name("Blazing Heat")
                    .type(Type.SUN)
                    .baseDmg(56)),
            new CardAttack(new CardAttack.Builder()
                    .name("Melt My Enemy")
                    .type(Type.LAVA)
                    .baseDmg(34)
                    .description("deal +12atk if your opponent has an Ice major")
                    .withEffect(new CardPredicate(bm -> bm.defender.getCard().majorType() == Type.ICE, bm -> bm.applyDamageStep(12, "Melt My Enemy")))),
            new CardLimited(2, "Warmth", "You can heal easily. Heal 20HP if you get below 100, twice per round.",
                    new CardPredicate(bm -> bm.attacker.getCard().HP < 100, new CardHeal(20)))),

    BIGGIE_JIMMY("Biggie Jimmy", 440,
            new CardAttack(new CardAttack.Builder()
                    .name("Trample")
                    .type(Type.FAUNA)
                    .baseDmg(76)),
            new CardAttack(new CardAttack.Builder()
                    .name("Shattering Stomp")
                    .type(Type.ROCK)
                    .description("deal +20atk next play")
                    .baseDmg(40)
                    .withEffect(new CardPersistent(1, 20, "Shattering Stomp Effect"))),
            new CardLimited(1, "Horse Power", "You can heal 60HP once per round", new CardHeal(60))),

    DOMINIC("Dominic", 340,
            new CardAttack(new CardAttack.Builder()
                    .name("Call Familiar")
                    .type(Type.FAUNA)
                    .description("+10 if there is a familiar card in your lineup")
                    .baseDmg(46)
                    .withEffect(bm -> {
                        if(bm.attacker.cardIDS.stream().anyMatch(id -> Arrays.asList(
                                "Biggie Jimmy",
                                "Nirkoyol",
                                "Spirit",
                                "Nirkoiizstrun",
                                "Hellhound",
                                "Brudzeynolrut",
                                "Motherboard").contains(id.card.name))) {
                            bm.applyDamageStep(10, "Call Familiar Effect");
                        }
                    })),
            new CardAttack(new CardAttack.Builder()
                    .name("Rose Bush")
                    .type(Type.FLORA)
                    .description("slowly deal +5atk over the next two plays")
                    .baseDmg(32)
                    .withEffect(new CardPersistent(2, 5, "Rose Bush Effect"))),
            new CardLimited(3, "Trial and Error", "Can be used 3 times per round. Roll a 10d, and if you get 3 or up, deal +15atk for that move.",
                    new CardRandom("Trial and Error Effect", 10, 3, 15))),

    NIRKOYOL("Nirkoyol", 470,
            new CardAttack(new CardAttack.Builder()
                    .name("Dovahyol")
                    .type(Type.FIRE)
                    .description("deal +10 over the two next moves if your opponent is an Ice, Fauna, or Flora constellation.")
                    .baseDmg(40)
                    .withEffect(new CardPredicate(bm -> !Collections.disjoint(
                            Arrays.asList(Type.ICE, Type.FAUNA, Type.FLORA),
                            Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType())),
                            new CardPersistent(2, 10, "Dovahyol Effect")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Charring Bite")
                    .type(Type.SUN)
                    .description("deal +25 if your opponent has more than 100HP left.")
                    .baseDmg(35)
                    .withEffect(new CardPredicate(bm -> bm.defender.getCard().HP > 100, bm -> bm.applyDamageStep(25, "Charring Bite Effect")))),
            new CardLimited(1, "Intimidating Size", "This Dragon is stage 5. Pick one move for your opponent to skip out of fear.", new CardSkip())),

    SPIRIT("Spirit", 270,
            new CardAttack(new CardAttack.Builder()
                    .name("Shadow Cat")
                    .type(Type.MOON)
                    .description("deal +10 if your opponent has a Sun constellation.")
                    .baseDmg(40)
                    .withEffect(new CardPredicate(bm ->
                            Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType()).contains(Type.SUN),
                            bm -> bm.applyDamageStep(10, "Shadow Cat Effect")))
                    .withEffect(new CardPredicate(bm -> bm.defender.getCard().HP < 100, bm -> bm.applyDamageStep(8, "Gash Buff")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Sharp Claws")
                    .type(Type.FAUNA)
                    .description("deal +20 if your opponent is an Equine familiar")
                    .withEffect(new CardPredicate(bm ->
                            Arrays.asList("Biggie Jimmy", "Brudzeynolrut").contains(bm.defender.getCard().name),
                            bm -> bm.applyDamageStep(20, "Sharp Claws Effect")))
                    .withEffect(new CardPredicate(bm -> bm.defender.getCard().HP < 100, bm -> bm.applyDamageStep(8, "Gash Buff")))),
            null),

    TENKS("Tenks", 700,
            new CardAttack(new CardAttack.Builder()
                    .name("Ground Smasher")
                    .type(Type.ROCK)
                    .description("can only be used every other move.")
                    .baseDmg(55)
                    .withEffect(bm -> bm.nextTurnEffects.add(new CardSkip()))),
            new CardLimited(1, "Spike Wall", "opponent misses one turn. Can only be used once per round",
                    new CardAttack(new CardAttack.Builder().name("Spike Wall").baseDmg(10))),
            null),

    NIRKOIIZSTRUN("Nirkoiizstrun", 530,
            new CardAttack(new CardAttack.Builder()
                    .name("Hunts In Blizzards")
                    .type(Type.ICE)
                    .description("deal +10atk if opponent has an Ice constellation")
                    .baseDmg(45)
                    .withEffect(new CardPredicate(bm ->
                            Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType()).contains(Type.ICE),
                            bm -> bm.applyDamageStep(10, "Hunts In Blizzards Effect")))
                    .withEffect(new CardPredicate(bm -> bm.attacker.cardIDS.stream().anyMatch(id -> id.card.name.equals("Nekomancer")),
                            bm -> bm.applyDamageStep(20, "Neko's Dovah Buff")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Crusher")
                    .type(Type.ROCK)
                    .description("roll a 20d, and if you get 10 or up, deal +20atk.")
                    .baseDmg(50)
                    .withEffect(new CardRandom("Crusher Effect", 20, 10, 20))
                    .withEffect(new CardPredicate(bm -> bm.attacker.cardIDS.stream().anyMatch(id -> id.card.name.equals("Nekomancer")),
                            bm -> bm.applyDamageStep(20, "Neko's Dovah Buff")))),
            null),

    HELLHOUND("Hellhound", 330,
            new CardAttack(new CardAttack.Builder()
                    .name("Hellborn")
                    .type(Type.LAVA)
                    .description("deal +10atk if opponent has an Ice major")
                    .baseDmg(30)
                    .withEffect(new CardPredicate(bm -> bm.defender.getCard().majorType() == Type.ICE, bm -> bm.applyDamageStep(10, "Hellborn Effect")))
                    .withEffect(new CardPredicate(bm -> bm.attacker.cardIDS.stream().anyMatch(id -> id.card.name.equals("Emily")), bm -> bm.applyDamageStep(10, "Emi's Wolf Buff")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Set Ablaze")
                    .type(Type.FIRE)
                    .description("deal +5atk over next two plays")
                    .baseDmg(40)
                    .withEffect(new CardPersistent(2, 5, "Set Ablaze Effect"))
                    .withEffect(new CardPredicate(bm -> bm.attacker.cardIDS.stream().anyMatch(id -> id.card.name.equals("Emily")), bm -> bm.applyDamageStep(10, "Emi's Wolf Buff")))),
            null),

    HAZY("Hazy", 440,
            new CardAttack(new CardAttack.Builder()
                    .name("Moon Dust")
                    .type(Type.MOON)
                    .description("stun your opponent and take away 10atk from their next move.")
                    .baseDmg(23)
                    .withEffect(bm -> bm.nextTurnEffects.add(bm1 -> bm1.applyDamageStep(-10, "Moon Dust Effect")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Wind Chill")
                    .type(Type.ICE)
                    .description("deal +20atk if your opponent is a Fire, Lava, or Sun major.")
                    .baseDmg(30)
                    .withEffect(new CardPredicate(bm -> Arrays.asList(Type.FIRE, Type.LAVA, Type.SUN).contains(bm.defender.getCard().majorType()),
                            bm -> bm.applyDamageStep(20, "Wind Chill effect")))),
            new CardLimited(1, "GIANT ASS", "You have a MASSIVE bandonkadonk. Choose one move to stun your opponent and cause them to skip their turn.", new CardSkip())),

    BRUDZEYNOLRUT("Brudzeynolrut", 290,
            new CardAttack(new CardAttack.Builder()
                    .name("Natural Steed")
                    .type(Type.FLORA)
                    .description("deal +25 if your opponent is a Water, Sun or Rock constellation")
                    .baseDmg(68)
                    .withEffect(new CardPredicate(bm ->
                            !Collections.disjoint(
                                    Arrays.asList(Type.WATER, Type.SUN, Type.ROCK),
                                    Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType())),
                            bm -> bm.applyDamageStep(25, "Natural Steed Effect")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Fatal Kick")
                    .type(Type.FAUNA)
                    .description("+18 if opponent is using a Player card")
                    .baseDmg(32)
                    .withEffect(new CardPredicate(bm ->
                            Arrays.asList("Nekomancer", "DragN", "Emily", "Dominic", "Tenks", "Hazy").contains(bm.defender.getCard().name),
                            bm -> bm.applyDamageStep(18, "Fatal Kick Effect")))),
            new CardLimited(1, "Swift", "Choose one move for your opponent to miss, once per round", new CardSkip())),

    MOTHERBOARD("Motherboard", 600,
            new CardAttack(new CardAttack.Builder()
                    .name("Steel Bite")
                    .type(Type.METAL)
                    .description("deal +10 if your opponent is not a Rock, Metal, or Lava constellation")
                    .baseDmg(56)
                    .withEffect(new CardPredicate(bm ->
                            Collections.disjoint(
                                    Arrays.asList(Type.ROCK, Type.METAL, Type.LAVA),
                                    Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType())),
                            bm -> bm.applyDamageStep(10, "Steel Bite Effect")))
                    .withEffect(new CardPredicate(bm ->
                            bm.attacker.cardIDS.stream().anyMatch(id -> id.card.name.equals("DragN")) &&
                            !Collections.disjoint(
                                    Arrays.asList(Type.ICE, Type.WATER, Type.FAUNA),
                                    Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType())),
                            bm -> bm.applyDamageStep(20, "DragN's Spirit Guide Buff")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Molten Breath")
                    .type(Type.LAVA)
                    .description("deal +25 to Metal constellations, and add 10 HP to yourself")
                    .baseDmg(40)
                    .withEffect(new CardPredicate(bm -> Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType()).contains(Type.METAL),
                            new CardHeal(10)))
                    .withEffect(new CardPredicate(bm -> Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType()).contains(Type.METAL),
                            bm -> bm.applyDamageStep(25, "Molten Breath Effect")))
                    .withEffect(new CardPredicate(bm ->
                            bm.attacker.cardIDS.stream().anyMatch(id -> id.card.name.equals("DragN")) &&
                                    !Collections.disjoint(
                                            Arrays.asList(Type.ICE, Type.WATER, Type.FAUNA),
                                            Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType())),
                            bm -> bm.applyDamageStep(20, "DragN's Spirit Guide Buff")))),
    null),

    YA_TE_VEO("Ya-Te-Veo", 580,
            new CardAttack(new CardAttack.Builder()
                    .name("Thorn Vines")
                    .type(Type.FLORA)
                    .description("deal +40atk if your opponent has a Rock constellation")
                    .baseDmg(50)
                    .withEffect(new CardPredicate(bm -> Arrays.asList(bm.defender.getCard().majorType(), bm.defender.getCard().minorType()).contains(Type.ROCK),
                            bm -> bm.applyDamageStep(40, "Thorn Vines Effect")))
                    .withEffect(new CardPredicate(bm -> bm.defender.getCard().name.equals("DragN"), bm -> bm.applyDamageStep(10, "F*ck DragN")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Blinding Darkness")
                    .type(Type.MOON)
                    .description("stun an opponent for one move, once per round")
                    .baseDmg(30)
                    .withEffect(new CardLimited(1, "Blinding Darkness Effect", "", new CardSkip()))
                    .withEffect(new CardPredicate(bm -> bm.defender.getCard().name.equals("DragN"), bm -> bm.applyDamageStep(10, "F*ck DragN")))),
            null),

    TICKING_ENTITY("Ticking Entity", 470,
            new CardAttack(new CardAttack.Builder()
                    .name("Crashed")
                    .type(Type.FIRE)
                    .description("deal +10 to the DragN, Tenks, and Hazy cards.")
                    .baseDmg(53)
                    .withEffect(new CardPredicate(bm -> Arrays.asList("DragN", "Tenks", "Hazy").contains(bm.defender.getCard().name),
                            bm -> bm.applyDamageStep(10, "Crashed Effect")))),
            new CardAttack(new CardAttack.Builder()
                    .name("Corrupt Entities")
                    .type(Type.MOON)
                    .description("deal +20 the Ender Dragon, Snow Leopard, and Biggie Jimmy cards.")
                    .baseDmg(42)
                    .withEffect(new CardPredicate(bm -> Arrays.asList("Ender Dragon", "Snow Leopard", "Biggie Jimmy").contains(bm.defender.getCard().name),
                            bm -> bm.applyDamageStep(20, "Corrupt Entities Effect")))),
            new CardLimited(1, "Crash Log", "Cause your opponent to miss a turn and deal +20atk to them if they are playing a player card.",
                    new CardPredicate(bm ->
                            Arrays.asList("Nekomancer", "DragN", "Emily", "Dominic", "Tenks", "Hazy").contains(bm.defender.getCard().name),
                            bm -> {
                                bm.applyDamageStep(20, "Crash Log Effect");
                                new CardSkip().accept(bm);
                            })));

    public final Card card;
    CardID(String name, int maxHP, ICardAction majorAttack, ICardAction minorAttack, ICardAction buff) {
        this.card = new Card(name, maxHP, majorAttack, minorAttack, buff);
    }

    public static CardID nameToCardID(String name) {
        return CardID.valueOf(name.toUpperCase());
    }
}