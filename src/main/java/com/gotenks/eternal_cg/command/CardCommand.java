package com.gotenks.eternal_cg.command;

import com.gotenks.eternal_cg.battle.PendingBattleManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CardCommand {

    public static IFormattableTextComponent systemOutput(String s) {
        return new StringTextComponent(s).withStyle(TextFormatting.ITALIC, TextFormatting.GRAY);
    }

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("eternalcg")
                .then(Commands.literal("battle")
                    .then(Commands.argument("player", EntityArgument.player())
                    .executes(ctx -> {
                        ServerPlayerEntity sender = ctx.getSource().getPlayerOrException();
                        ServerPlayerEntity receiver = EntityArgument.getPlayer(ctx, "player");

                        if(PendingBattleManager.add(sender, receiver)) {
                            sender.sendMessage(systemOutput(receiver.getScoreboardName() + " has received your battle request!"), sender.getUUID());
                            receiver.sendMessage(systemOutput(sender.getScoreboardName() + " has sent you a battle request.\nType /eternalcg accept " + sender.getScoreboardName() + " to accept the battle!"), receiver.getUUID());
                        } else {
                            sender.sendMessage(systemOutput("Could not add battle.\nYou or " + receiver.getScoreboardName() + " is currently in a pending battle"), sender.getUUID());
                        }
                        return 1;
                    })))
                .then(Commands.literal("cancel")
                    .then(Commands.argument("player", EntityArgument.player())
                    .executes(ctx -> {
                        ServerPlayerEntity sender = ctx.getSource().getPlayerOrException();
                        ServerPlayerEntity receiver = EntityArgument.getPlayer(ctx, "player");

                        if(PendingBattleManager.remove(sender, receiver)) {
                            sender.sendMessage(systemOutput("Battle request cancelled"), sender.getUUID());
                            receiver.sendMessage(systemOutput(sender.getScoreboardName() + " has cancelled your battle request"), receiver.getUUID());
                        } else {
                            sender.sendMessage(systemOutput("Could not cancel battle request.\nYou may not have any pending requests"), sender.getUUID());
                        }
                        return 1;
                    })))
                .then(Commands.literal("accept")
                    .then(Commands.argument("player", EntityArgument.player())
                    .executes(ctx -> {
                        ServerPlayerEntity sender = ctx.getSource().getPlayerOrException();
                        ServerPlayerEntity receiver = EntityArgument.getPlayer(ctx, "player");

                        if(PendingBattleManager.publish(sender, receiver)) {
                            sender.sendMessage(systemOutput("Accepted battle with " + receiver.getScoreboardName()), sender.getUUID());
                            receiver.sendMessage(systemOutput(sender.getScoreboardName() + " has accepted your battle request!"), receiver.getUUID());
                        } else {
                            sender.sendMessage(systemOutput("Could not accept battle request.\nYou may not have any pending requests"), sender.getUUID());
                        }
                        return 1;
                    })))
                .then(Commands.literal("list")
                .executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
                    player.sendMessage(systemOutput("Listed are your pending requests:"), player.getUUID());
                    for(String name : PendingBattleManager.listAllRequests(player)) {
                        player.sendMessage(systemOutput(" - " + name), player.getUUID());
                    }
                    return 1;
                }));
    }
}
