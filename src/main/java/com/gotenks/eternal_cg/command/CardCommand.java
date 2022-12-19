package com.gotenks.eternal_cg.command;

import com.gotenks.eternal_cg.battle.PendingBattleManager;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CardCommand {
    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("eternalcg")
                .then(Commands.literal("battle")
                    .then(Commands.argument("player", EntityArgument.player())
                    .executes(ctx -> {
                        ServerPlayerEntity sender = ctx.getSource().getPlayerOrException();
                        ServerPlayerEntity receiver = EntityArgument.getPlayer(ctx, "player");

                        PendingBattleManager.add(sender, receiver);
                        return 1;
                    }))
                    .then(Commands.literal("cancel")
                    .executes(ctx -> {
                        ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
                        PendingBattleManager.remove(player);
                        return 1;
                    }))
                    .then(Commands.literal("accept")
                    .executes(ctx -> {
                        ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
                        PendingBattleManager.publish(player);
                        return 1;
                    }))
                    .then(Commands.literal("deny")
                    .executes(ctx -> {
                        ServerPlayerEntity player = ctx.getSource().getPlayerOrException();
                        PendingBattleManager.remove(player);
                        return 1;
                    }))
                );
    }
}
