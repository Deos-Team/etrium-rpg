package dev.deos.etrium.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

object SetEnergyCommand {
    init {
        if (true) {
            CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource?>,
                                                                                     registryAccess: CommandRegistryAccess?, environment: CommandManager.RegistrationEnvironment? ->
                dispatcher.register(
                    CommandManager.literal("energy").then(CommandManager.literal("set"))
                        .then(
                            RequiredArgumentBuilder.argument<ServerCommandSource?, String?>(
                                "current", StringArgumentType.string()
                            )
                        )
                )
            })
        }
    }
}