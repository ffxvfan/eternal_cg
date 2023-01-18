package com.gotenks.eternal_cg.command;

import com.gotenks.eternal_cg.battle.BattleManagerFactory;
import com.gotenks.eternal_cg.battle.PendingBattleFactory;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.stream.Collectors;

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
                        if(BattleManagerFactory.contains(sender)) {
                            sender.sendMessage(systemOutput("You are already in a battle."), sender.getUUID());
                        } else if(BattleManagerFactory.contains(receiver)) {
                            sender.sendMessage(systemOutput(receiver.getScoreboardName() + " is already in a battle."), sender.getUUID());
                        } else {
                            sender.sendMessage(systemOutput(receiver.getScoreboardName() + " has received your battle request!"), sender.getUUID());
                            receiver.sendMessage(systemOutput(sender.getScoreboardName() + " has sent you a battle request!\nType /eternalcg accept " + sender.getScoreboardName() + " to battle!"), receiver.getUUID());
                            PendingBattleFactory.add(sender, receiver);
                        }
                        return 1;
                    })))
                .then(Commands.literal("accept")
                    .then(Commands.argument("player", EntityArgument.player())
                    .executes(ctx -> {
                        ServerPlayerEntity sender = ctx.getSource().getPlayerOrException();
                        ServerPlayerEntity receiver = EntityArgument.getPlayer(ctx, "player");
                        PendingBattleFactory.remove(sender, receiver);
                        return 1;
                    })))
                .then(Commands.literal("list")
                .executes(ctx -> {
                    ServerPlayerEntity sender = ctx.getSource().getPlayerOrException();
                    String msg = "Current outgoing requests:" +
                            PendingBattleFactory.listOutgoingRequests(sender).collect(Collectors.joining("\n-")) +
                            "Current incoming requests:" +
                            PendingBattleFactory.listIncomingRequests(sender).collect(Collectors.joining("\n-"));
                    sender.sendMessage(systemOutput(msg), sender.getUUID());
                    return 1;
                }));
    }
}
