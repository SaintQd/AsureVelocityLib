package org.saintqd.asurevelocitylib.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import org.saintqd.asurevelocitylib.AsureVelocityLib;
import org.saintqd.asurevelocitylib.managers.LangManager;
import org.saintqd.asurevelocitylib.utils.VinUtils;

public class VinVelocityLibCommands {

    public static BrigadierCommand setupCommands(final ProxyServer proxy) {
        return new BrigadierCommand(
                BrigadierCommand.literalArgumentBuilder("vinvelocitylib")
                        .requires(source -> source.hasPermission("vinvelocitylib.admin"))
                        .then(BrigadierCommand.literalArgumentBuilder("reload")
                                .executes(ctx -> {
                                    reloadCommand(ctx.getSource());
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .then(BrigadierCommand.literalArgumentBuilder("debug")
                                .executes(ctx -> {
                                    changeDebugLevelCommand(ctx.getSource(), 0);
                                    return Command.SINGLE_SUCCESS;
                                })
                                .then(BrigadierCommand.requiredArgumentBuilder("level", IntegerArgumentType.integer(0))
                                        .executes(ctx -> {
                                            changeDebugLevelCommand(ctx.getSource(), ctx.getArgument("level", Integer.class));
                                            return Command.SINGLE_SUCCESS;
                                        })
                                )
                        )

                        .build()
        );
    }

    private static void reloadCommand(CommandSource sender) {
        AsureVelocityLib.inst().loadData();
        sender.sendMessage(LangManager.INSTANCE.parseLangString(AsureVelocityLib.inst(),"reloadMessage"));
    }

    private static void changeDebugLevelCommand(CommandSource sender, int level) {
        AsureVelocityLib.inst().setDebugLevel(level);
        sender.sendMessage(VinUtils.parseString("<gray>Debug level set to <blue>"+level+"<gray>."));
    }
}
