package com.gotenks.eternal_cg.items;

import com.gotenks.eternal_cg.EternalCG;
import com.gotenks.eternal_cg.actions.*;
import com.gotenks.eternal_cg.battle.BattleManager;
import com.gotenks.eternal_cg.network.CardPacketHandler;
import com.gotenks.eternal_cg.network.ShowCardSelectionScreenPacket;
import com.gotenks.eternal_cg.types.Type;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static com.gotenks.eternal_cg.init.ItemsInit.EternalCGTab;

public enum CardID {
    NEKOMANCER("nekomancer", new Card(new Item.Properties().tab(EternalCGTab), "Nekomancer", 340, Type.ICE, Type.MOON, new ArrayList<>(Arrays.asList(
            new CardAttack("Chilled Rush", "56atk", Type.ICE, 56, battleManager -> {}),
            new CardAttack("Vengeance", "27atk, slowly deal +8atk over next two plays.", Type.MOON, 27, battleManager -> {
                battleManager.attacker.sendMessage("Vengeance effect will be applied on the next two rounds");
                battleManager.addNextRound(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("Vengeance effect: +8atk");
                    battleManager1.defender.applyDamage(8);
                    battleManager1.addNextRound(battleManager2 -> {
                        battleManager2.sendAttackerMessageToBoth("Vengeance effect: +8atk");
                        battleManager2.defender.applyDamage(8);
                        battleManager2.attacker.sendSystemMessage("Vengeance effect has worn off...");
                    });
                });
            }),
            new CardActionLimited("Raise Undead", "Raise one dead card. Can be used once per round.", battleManager -> {
                battleManager.attacker.sendSystemMessage("Select a card to revive.");
                CardPacketHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> battleManager.attacker.player),
                        new ShowCardSelectionScreenPacket((ArrayList<CardID>) battleManager.attacker.cardIDS.stream().filter(cardID -> cardID.card.health < 0).collect(Collectors.toList()), 150, 200, 2, 1));
                battleManager.state = BattleManager.State.REVIVAL;
            }, 1)
    )), null)),

    DRAGN("dragn", new Card(new Item.Properties().tab(EternalCGTab), "DragN", 350, Type.FIRE, Type.LAVA, new ArrayList<>(Arrays.asList(
            new CardAttack("Flame Rush", "53atk", Type.FIRE, 53, battleManager -> {}),
            new CardAttack("Slow Burn", "10atk, slowly deal +5atk over the next two plays", Type.LAVA, 10, battleManager -> {
                battleManager.attacker.sendSystemMessage("Slow Burn effect will be applied on the next two rounds");
                battleManager.addNextRound(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("Slow Burn effect: +5atk");
                    battleManager1.defender.applyDamage(5);
                    battleManager1.addNextRound(battleManager2 -> {
                        battleManager2.sendAttackerMessageToBoth("Slow Burn effect: +5atk");
                        battleManager2.defender.applyDamage(5);
                        battleManager2.attacker.sendSystemMessage("Slow Burn effect has worn off...");
                    });
                });
            })
    )), new ArrayList<>(Arrays.asList(
            new CardPassive("Rage", "Deal 8+ damage when on 200HP or lower", battleManager -> {
                if(battleManager.attacker.getCard().health <= 200) {
                    battleManager.sendAttackerMessageToBoth("Rage effect applied: +8 damage");
                    battleManager.defender.applyDamage(8);
                }
            })
    )))),

    EMILY("emily", new Card(new Item.Properties().tab(EternalCGTab), "Emily", 330, Type.SUN, Type.LAVA, new ArrayList<>(Arrays.asList(
            new CardAttack("Blazing Heat", "56atk", Type.SUN, 56, battleManager -> {}),
            new CardAttack("Melt My Enemy", "34atk, deal +12atk if your opponent has an Ice major", Type.LAVA, 34, battleManager -> {
                if(battleManager.defender.getCard().major == Type.ICE) {
                    battleManager.sendAttackerMessageToBoth("Defender is Ice type, +12atk");
                    battleManager.defender.applyDamage(12);
                }
            }),
            new CardActionLimited("Warmth", "You can heal easily. Heal 20HP if you get below 100, twice per round.", battleManager -> {
                if(battleManager.attacker.getCard().health <= 100) {
                    battleManager.sendAttackerMessageToBoth("Warmth: 20HP restored");
                    battleManager.attacker.getCard().health += 20;
                } else {
                    battleManager.attacker.sendSystemMessage("Could not use Warmth.");
                }
            }, 2)
    )), null)),

    BIGGIE_JIMMY("biggie_jimmy", new Card(new Item.Properties().tab(EternalCGTab), "Biggie Jimmy", 440, Type.FAUNA, Type.ROCK, new ArrayList<>(Arrays.asList(
            new CardAttack("Trample", "76atk", Type.FAUNA, 76, battleManager -> {}),
            new CardAttack("Shattering Stomp", "40atk, deal +20atk next play", Type.ROCK, 40, battleManager -> {
                battleManager.attacker.sendMessage("Shattering Stomp effect will be applied next round.");
                battleManager.addNextRound(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("Shattering Stomp effect: +20atk");
                    battleManager1.defender.applyDamage(20);
                });
            }),
            new CardActionLimited("Horse Power", "You can heal 60HP once per round.", battleManager -> {
                battleManager.sendAttackerMessageToBoth("Horse Power: 60HP restored");
                battleManager.attacker.getCard().health += 60;
            }, 1)
    )), null)),

    DOMINIC("dominic", new Card(new Item.Properties().tab(EternalCGTab), "Dominic", 340, Type.FAUNA, Type.FLORA, new ArrayList<>(Arrays.asList(
            new CardAttack("Call Familiar", "46atk, +10 if there is a familiar card in your lineup", Type.FAUNA, 46, battleManager -> {
                for(CardID cardID : battleManager.attacker.cardIDS) {
                    if(cardID.name.equals("biggie_jimmy")
                    || cardID.name.equals("brudzeynolrut")
                    || cardID.name.equals("hellhound")
                    || cardID.name.equals("motherboard")
                    || cardID.name.equals("nirkoiizstrun")
                    || cardID.name.equals("nirkoyol")
                    || cardID.name.equals("spirit")
                    || cardID.name.equals("ticking_entity")
                    || cardID.name.equals("ya_te_veo")) {
                        battleManager.sendAttackerMessageToBoth("Call Familiar: +10atk");
                        battleManager.defender.applyDamage(10);
                    } else {
                        battleManager.attacker.sendSystemMessage("No familiars in lineup.");
                    }
                }
            }),
            new CardAttack("Rose Bush", "32atk, slowly deal +5atk over the next two plays", Type.FLORA, 32, battleManager -> {
                battleManager.attacker.sendMessage("Rose Bush effect will be applied on the next two rounds");
                battleManager.addNextRound(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("Rose Bush effect: +5atk");
                    battleManager1.defender.applyDamage(5);
                    battleManager1.addNextRound(battleManager2 ->{
                        battleManager2.sendAttackerMessageToBoth("Rose Bush effect: +5atk");
                        battleManager2.defender.applyDamage(5);
                        battleManager2.sendSystemMessageToBoth("Rose Bush effect has worn off...");
                    });
                });
            })
    )), null)),

    NIRKOYOL("nirkoyol", new Card(new Item.Properties().tab(EternalCGTab), "Nirkoyol", 470, Type.FIRE, Type.SUN, new ArrayList<>(Arrays.asList(
            new CardAttack("Dovahyol", "40atk, deal +10 over the two next moves if your opponent is an Ice, Fauna, or Flora Constellation.", Type.FIRE, 40, battleManager -> {
                battleManager.attacker.sendSystemMessage("Dovahyol effect will be applied on the next two rounds");
                battleManager.addNextRound(battleManager1 -> {
                    if(battleManager1.defender.getCard().major == Type.ICE || battleManager1.defender.getCard().minor == Type.ICE
                            || battleManager1.defender.getCard().major == Type.FAUNA || battleManager1.defender.getCard().minor == Type.FAUNA
                            || battleManager1.defender.getCard().major == Type.FLORA || battleManager1.defender.getCard().minor == Type.FLORA) {
                        battleManager1.sendAttackerMessageToBoth("Dovahyol effect: +10atk");
                        battleManager1.defender.applyDamage(10);
                    }
                    battleManager1.addNextRound(battleManager2 -> {
                        if(battleManager2.defender.getCard().major == Type.ICE || battleManager2.defender.getCard().minor == Type.ICE
                                || battleManager2.defender.getCard().major == Type.FAUNA || battleManager2.defender.getCard().minor == Type.FAUNA
                                || battleManager2.defender.getCard().major == Type.FLORA || battleManager2.defender.getCard().minor == Type.FLORA) {
                            battleManager2.sendAttackerMessageToBoth("Dovahyol effect: +10atk");
                            battleManager2.defender.applyDamage(10);
                        }
                        battleManager2.sendSystemMessageToBoth("Dovahyol effect has worn off...");
                    });
                });
            }),
            new CardAttack("Charring Bite", "35atk, deal +25 if your opponent has more than 100HP left.", Type.SUN, 35, battleManager -> {
                if(battleManager.defender.getCard().health >= 100) {
                    battleManager.sendAttackerMessageToBoth("Charring Bite effect: + 25atk");
                    battleManager.defender.applyDamage(25);
                }
            }),
            new CardActionLimited("Intimidating Size", "This dragon is Stage 5. Pick one move for your opponent to skip out of fear." , battleManager -> {
                battleManager.addNextTurn(battleManager1 -> {
                    battleManager1.sendDefenderMessageToBoth("Intimidating Size effect: Turn skipped!");
                    while(!battleManager1.actionQueue.getFirst().equals(battleManager1.attackEnd)) {
                        battleManager1.actionQueue.remove();
                    }
                    battleManager1.actionQueue.remove().accept(battleManager1);
                });
            }, 1)
    )), null)),

    SPIRIT("spirit", new Card(new Item.Properties().tab(EternalCGTab), "Spirit", 270, Type.MOON, Type.FAUNA, new ArrayList<>(Arrays.asList(
            new CardAttack("Shadow Cat", "40atk, deal +10 if your opponent has a Sun constellation.", Type.MOON, 40, battleManager -> {
                if(battleManager.defender.getCard().major == Type.SUN || battleManager.defender.getCard().minor == Type.SUN) {
                    battleManager.sendAttackerMessageToBoth("Shadow Cat effect: +10atk");
                    battleManager.defender.applyDamage(10);
                }
            }),
            new CardAttack("Sharp Claws", "35atk, deal +20 if your opponent is an Equine familiar", Type.FAUNA, 35, battleManager -> {
                if(battleManager.defender.getCard().name.equals("biggie_jimmy") || battleManager.defender.getCard().name.equals("brudzeynolrut")) {
                    battleManager.sendAttackerMessageToBoth("Sharp Claws effect: +20atk");
                    battleManager.defender.applyDamage(20);
                }
            })
    )), new ArrayList<>(Arrays.asList(
            new CardPassive("Gash", "You can slice and gash your opponents, causing bleed damage. +8atk if your opponent is under 100HP.", battleManager -> {
                if(battleManager.defender.getCard().health < 100) {
                    battleManager.sendAttackerMessageToBoth("Gash effect: +8atk");
                    battleManager.defender.applyDamage(8);
                }
            })
    )))),

    TENKS("tenks", new Card(new Item.Properties().tab(EternalCGTab), "Tenks", 700, Type.ROCK, Type.METAL, new ArrayList<>(Arrays.asList(
            new CardAttack("Ground Smasher", "55atk, can only be used every other move.", Type.ROCK, 55, battleManager -> {
                battleManager.addNextRound(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("Ground Smasher effect: can only be used every other move.");
                    while(!battleManager1.actionQueue.getFirst().equals(battleManager1.attackEnd)) {
                        battleManager1.actionQueue.remove();
                    }
                    battleManager1.actionQueue.remove().accept(battleManager1);
                });
            }),
            new CardAttackLimited("Spike Wall", "10atk, opponent misses one turn. Can only be use once per round", Type.METAL, 10, battleManager -> {
                battleManager.addNextTurn(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("Spike Wall effect: Turn Skipped!");
                    while(battleManager1.actionQueue.getFirst().equals(battleManager1.attackEnd)) {
                        battleManager1.actionQueue.remove();
                    }
                    battleManager1.actionQueue.remove().accept(battleManager1);
                });
            }, 1)
    )), new ArrayList<>(Arrays.asList(
            new CardPassive("Tough as a Boulder", "Cannot be affected by fire or fauna attacks", battleManager -> {
                battleManager.addNextTurn(battleManager1 -> {
                    battleManager1.attacker.getCard().cardActions.forEach(cardAction -> {
                        if(cardAction instanceof CardAttack && battleManager1.actionQueue.contains(cardAction.action)
                        && (((CardAttack)cardAction).type == Type.FIRE || ((CardAttack)cardAction).type == Type.FAUNA)) {
                            battleManager1.sendDefenderMessageToBoth("Tough as a Boulder effect: " + cardAction.name + " cannot harm Tenks!");
                            battleManager1.actionQueue.remove(cardAction.action);
                        }
                    });
                });
            })
    )))),

    NIRKOIIZSTRUN("nirkoiizstrun", new Card(new Item.Properties().tab(EternalCGTab), "Nirkoiizstrun", 530, Type.ICE, Type.ROCK, new ArrayList<>(Arrays.asList(
            new CardAttack("Hunts In Blizzards", "45atk, deal +10atk if opponent has an Ice constellation", Type.ICE, 45, battleManager -> {
                if(battleManager.defender.getCard().major == Type.ICE || battleManager.defender.getCard().minor == Type.ICE) {
                    battleManager.sendAttackerMessageToBoth("Hunts In Blizzards effect: +10atk");
                    battleManager.defender.applyDamage(10);
                }
            }),
            new CardAttack("Crusher", "50atk, roll a 20d, and if you get 10 or up, deal +20atk.", Type.ROCK, 50, battleManager -> {
                int roll = new Random().nextInt(21);
                battleManager.sendSystemMessageToBoth("Crusher effect: rolling 20d");
                battleManager.sendSystemMessageToBoth("?Roll: " + roll);
                if(roll >= 10) {
                    battleManager.sendAttackerMessageToBoth("Crusher effect: +20atk");
                    battleManager.defender.applyDamage(20);
                } else {
                    battleManager.sendSystemMessageToBoth("no effect...");
                }
            })
    )), new ArrayList<>(Arrays.asList(
            new CardPassive("Neko's Dovah", "Deal +20atk for each move if Nekomancer is in your lineup", battleManager -> {
                for(CardID cardID : battleManager.attacker.cardIDS) {
                    if(cardID.name.equals("nekomancer")) {
                        battleManager.sendAttackerMessageToBoth("Neko's Dovah effect: +20atk");
                        battleManager.defender.applyDamage(20);
                    }
                }
            })
    )))),

    HELLHOUND("hellhound", new Card(new Item.Properties().tab(EternalCGTab), "Hellhound", 330, Type.LAVA, Type.FIRE, new ArrayList<>(Arrays.asList(
            new CardAttack("Hellborn", "30atk, deal +10atk if opponent has an Ice major", Type.LAVA, 30, battleManager -> {
                if(battleManager.defender.getCard().major == Type.ICE) {
                    battleManager.sendAttackerMessageToBoth("Hellborn effect: +10atk");
                    battleManager.defender.applyDamage(10);
                }
            }),
            new CardAttack("Set Ablaze", "40atk, deal +5atk over next two plays", Type.FIRE, 40, battleManager -> {
                battleManager.attacker.sendSystemMessage("Set Ablaze effect will be applied on the next two rounds.");
                battleManager.addNextRound(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("Set Ablaze effect: +5atk");
                    battleManager1.defender.applyDamage(5);
                    battleManager1.addNextRound(battleManager2 -> {
                        battleManager2.sendAttackerMessageToBoth("Set Ablaze effect: +5atk");
                        battleManager2.defender.applyDamage(5);
                        battleManager2.attacker.sendSystemMessage("Set Ablaze effect has worn off...");
                    });
                });
            })
    )), new ArrayList<>(Arrays.asList(
            new CardPassive("Emi's Wolf", "Deal +10atk each move if Emily is in your card lineup", battleManager -> {
                for(CardID cardID : battleManager.attacker.cardIDS) {
                    if(cardID.name.equals("emily")) {
                        battleManager.sendAttackerMessageToBoth("Emi's Wolf effect: +10atk");
                        battleManager.defender.applyDamage(10);
                    }
                }
            })
    )))),

    HAZY("hazy", new Card(new Item.Properties().tab(EternalCGTab), "Hazy", 440, Type.MOON, Type.ICE, new ArrayList<>(Arrays.asList(
            new CardAttack("Moon Dust", "23atk, sun your opponent and take away 10atk from their next move.", Type.MOON, 23, battleManager -> {
                battleManager.addNextTurn(battleManager1 -> {
                    battleManager1.sendDefenderMessageToBoth("Moon Dust effect: 10atk reduction.");
                    battleManager1.defender.getCard().health += 10;
                });
            }),
            new CardAttack("Wind Chill", "30atk, deal +20atk if your opponent is a Fire, Lava, or Sun major.", Type.ICE, 30, battleManager -> {
                if(battleManager.defender.getCard().major == Type.FIRE || battleManager.defender.getCard().major == Type.LAVA || battleManager.defender.getCard().major == Type.SUN) {
                    battleManager.sendAttackerMessageToBoth("Wind Chill effect: +20atk");
                    battleManager.defender.applyDamage(20);
                }
            }),
            new CardAction("GIANT ASS", "You have a MASSIVE bandonkadonk. Choose one move to stun your opponent and cause them to skip their turn.", battleManager -> {
                battleManager.addNextTurn(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("GIANT ASS effect: Turn Skipped!");
                    while(!battleManager1.actionQueue.getFirst().equals(battleManager1.attackEnd)) {
                        battleManager1.actionQueue.remove();
                    }
                    battleManager1.actionQueue.remove().accept(battleManager1);
                });
            })
    )), null)),

    BRUDZEYNOLRUT("brudzeynolrut", new Card(new Item.Properties().tab(EternalCGTab), "Brudzeynolrut", 290, Type.FLORA, Type.FAUNA, new ArrayList<>(Arrays.asList(
            new CardAttack("Natural Steed", "68atk, deal +25 if your opponent is a Water, Sun, or Rock constellation.", Type.FLORA, 68, battleManager -> {
                if(battleManager.defender.getCard().major == Type.WATER || battleManager.defender.getCard().minor == Type.WATER
                || battleManager.defender.getCard().major == Type.SUN || battleManager.defender.getCard().minor == Type.SUN
                || battleManager.defender.getCard().major == Type.ROCK || battleManager.defender.getCard().minor == Type.ROCK) {
                    battleManager.sendAttackerMessageToBoth("Natural Steed effect: +25atk");
                    battleManager.defender.applyDamage(25);
                }
            }),
            new CardAttack("Fatal Kick", "32atk, +18 if opponent is using a Player card", Type.FAUNA, 32, battleManager -> {
                if(battleManager.defender.getCard().name.equals("emily")
                || battleManager.defender.getCard().name.equals("dragn")
                || battleManager.defender.getCard().name.equals("hazy")
                || battleManager.defender.getCard().name.equals("tenks")
                || battleManager.defender.getCard().name.equals("nekomancer")
                || battleManager.defender.getCard().name.equals("dominic")) {
                    battleManager.sendAttackerMessageToBoth("Fatal Kick effect: +18atk");
                    battleManager.defender.applyDamage(18);
                }
            }),
            new CardActionLimited("Swift", "Choose one move for your opponent to miss, once per round", battleManager -> {
                battleManager.addNextTurn(battleManager1 -> {
                    battleManager1.sendAttackerMessageToBoth("Swift effect: Turn Skipped!");
                    while(!battleManager1.actionQueue.getFirst().equals(battleManager1.attackEnd)) {
                        battleManager1.actionQueue.remove();
                    }
                    battleManager1.actionQueue.remove().accept(battleManager1);
                });
            }, 1)
    )), null)),

    MOTHERBOARD("motherboard", new Card(new Item.Properties().tab(EternalCGTab), "Motherboard", 600, Type.METAL, Type.LAVA, new ArrayList<>(Arrays.asList(
            new CardAttack("Steel Bite", "56atk, deal +10 if your opponent is not a Rock, Metal, or Lava constellation.", Type.METAL, 56, battleManager -> {
                if(battleManager.defender.getCard().major != Type.ROCK || battleManager.defender.getCard().minor != Type.ROCK
                || battleManager.defender.getCard().major != Type.METAL || battleManager.defender.getCard().minor != Type.METAL
                || battleManager.defender.getCard().major != Type.LAVA || battleManager.defender.getCard().minor != Type.LAVA) {
                    battleManager.sendAttackerMessageToBoth("Steel Bite effect: +10atk");
                    battleManager.defender.applyDamage(10);
                }
            }),
            new CardAttack("Molten Breath", "40atk, deal +25 to Metal constellations, and add 10HP to yourself.", Type.LAVA, 40, battleManager -> {
                if(battleManager.defender.getCard().major == Type.METAL || battleManager.defender.getCard().minor == Type.METAL) {
                    battleManager.sendAttackerMessageToBoth("Molten Breath effect: +25atk, heal 10HP");
                    battleManager.defender.applyDamage(25);
                    battleManager.attacker.getCard().health += 10;
                }
            })
    )), new ArrayList<>(Arrays.asList(
            new CardPassive("DragN's Spirit Guide", "If DragN is in your lineup, deal +20atk to Ice, Water, and Fauna constellations.", battleManager -> {
                if(battleManager.defender.getCard().major == Type.ICE || battleManager.defender.getCard().minor == Type.ICE
                || battleManager.defender.getCard().major == Type.WATER || battleManager.defender.getCard().minor == Type.WATER
                || battleManager.defender.getCard().major == Type.FAUNA || battleManager.defender.getCard().minor == Type.FAUNA) {
                    battleManager.sendAttackerMessageToBoth("DragN's Spirit Guide effect: +20atk");
                    battleManager.defender.applyDamage(20);
                }
            })
    )))),

    YA_TE_VEO("ya_te_veo", new Card(new Item.Properties().tab(EternalCGTab), "Ya-Te-Veo", 580, Type.FLORA, Type.MOON, new ArrayList<>(Arrays.asList(
            new CardAttack("Thorn Vines", "50atk, deal +40atk if your opponent has a Rock constellation.", Type.FLORA, 50, battleManager -> {
                if(battleManager.defender.getCard().major == Type.ROCK || battleManager.defender.getCard().minor == Type.ROCK) {
                    battleManager.sendAttackerMessageToBoth("Thorn Vines effect: +40atk");
                    battleManager.defender.applyDamage(40);
                }
            }),
            new CardAttackLimited("Blinding Darkness", "30atk, stun an opponent for one move, once per round", Type.MOON, 30, battleManager -> {
                battleManager.addNextTurn(battleManager1 -> {
                    battleManager1.sendDefenderMessageToBoth("Blinding Darkness effect: Turn Skipped!");
                    while(!battleManager1.actionQueue.getFirst().equals(battleManager1.attackEnd)) {
                        battleManager1.actionQueue.remove();
                    }
                    battleManager1.actionQueue.remove().accept(battleManager1);
                });
            }, 1)
    )), new ArrayList<>(Arrays.asList(
            new CardPassive("F*ck DragN", "Automatically deal +10atk to the DragN card once per move.", battleManager -> {
                if(battleManager.defender.getCard().name.equals("dragn")) {
                    battleManager.sendAttackerMessageToBoth("F*ck DragN effect: +10atk.");
                    battleManager.defender.applyDamage(10);
                }
            })
    )))),

    TICKING_ENTITY("ticking_entity", new Card(new Item.Properties().tab(EternalCGTab), "Ticking Entity", 470, Type.FIRE, Type.MOON, new ArrayList<>(Arrays.asList(
            new CardAttack("Crashed", "52atk, deal +10 to the DragN, Tenks, and Hazy cards.", Type.FIRE, 53, battleManager -> {
                if(battleManager.defender.getCard().name.equals("dragn")
                || battleManager.defender.getCard().name.equals("tenks")
                || battleManager.defender.getCard().name.equals("hazy")) {
                    battleManager.sendAttackerMessageToBoth("Crashed effect: +10atk");
                    battleManager.defender.applyDamage(10);
                }
            }),
            new CardAttack("Corrupt Entities", "42atk, deal +20 the Ender Dragon, Snow Leopard and Biggie Jimmy cards", Type.MOON, 42, battleManager -> {
                if(battleManager.defender.getCard().name.equals("ender_dragon")
                || battleManager.defender.getCard().name.equals("snow_leopard")
                || battleManager.defender.getCard().name.equals("biggie_jimmy")) {
                    battleManager.sendAttackerMessageToBoth("Corrupt Entities effect: +20atk");
                    battleManager.defender.applyDamage(20);
                }
            }),
            new CardAction("Crash Log", "Cause your opponent to miss a turn and deal +20atk to them if they are playing a Player card.", battleManager -> {
                battleManager.addNextTurn(battleManager1 -> {
                    battleManager1.sendDefenderMessageToBoth("Crash Log effect: Turn Skipped!");
                    while(!battleManager1.actionQueue.getFirst().equals(battleManager1.attackEnd)) {
                        battleManager1.actionQueue.remove();
                    }
                    battleManager1.actionQueue.remove().accept(battleManager1);
                });
            })
    )), null));

    public final String name;
    public final Card card;

    CardID(String name, Card card) {
        this.name = name;
        this.card = card;
        this.card.name = name;
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
