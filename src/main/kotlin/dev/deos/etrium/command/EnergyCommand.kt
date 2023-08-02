package dev.deos.etrium.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.deos.etrium.utils.EtriumData
import dev.deos.etrium.utils.EtriumData.getEnergy
import dev.deos.etrium.utils.EtriumData.getLevel
import dev.deos.etrium.utils.EtriumData.getMaxEnergy
import dev.deos.etrium.utils.EtriumData.getRegen
import dev.deos.etrium.utils.EtriumData.getXp
import dev.deos.etrium.utils.EnergyTypes.ENERGY
import dev.deos.etrium.utils.EnergyTypes.LEVEL
import dev.deos.etrium.utils.EnergyTypes.MAX_ENERGY
import dev.deos.etrium.utils.EnergyTypes.REGEN
import dev.deos.etrium.utils.EnergyTypes.XP
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.EntitySelector
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

object EnergyCommand {
    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource?>,
                                                                                 registryAccess: CommandRegistryAccess?, environment: CommandManager.RegistrationEnvironment? ->
            dispatcher.register(
                CommandManager.literal("etrium").requires { it.hasPermissionLevel(4) }
                    .then(RequiredArgumentBuilder.argument<ServerCommandSource, EntitySelector>(
                        "player", EntityArgumentType.player()
                    )
                        .then(CommandManager.literal("add")
                            .then(CommandManager.literal(ENERGY)
                                .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = FloatArgumentType.getFloat(it, "amount")
                                        player.sendMessage(
                                            Text.literal(
                                                "Has been added a $amount energy." +
                                                        " Now your energy is ${player.getEnergy()}/${player.getMaxEnergy()}"
                                            ), true
                                        )
                                        EtriumData.addEnergy(player, amount, ENERGY)
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("add")
                            .then(CommandManager.literal(MAX_ENERGY)
                                .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = FloatArgumentType.getFloat(it, "amount")
                                        EtriumData.addEnergy(player, amount, MAX_ENERGY)
                                        player.sendMessage(
                                            Text.literal(
                                                "Has been added a $amount max energy." +
                                                        " Now your max  energy is ${player.getMaxEnergy()}"
                                            ), true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("add")
                            .then(CommandManager.literal(REGEN)
                                .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = FloatArgumentType.getFloat(it, "amount")
                                        EtriumData.addEnergy(player, amount, REGEN)
                                        player.sendMessage(
                                            Text.literal(
                                                "Has been added a $amount regen." +
                                                        " Now your regen is ${player.getRegen()}"
                                            ), true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("add")
                            .then(CommandManager.literal(LEVEL)
                                .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = IntegerArgumentType.getInteger(it, "amount")
                                        EtriumData.addLevel(player, amount, LEVEL)
                                        player.sendMessage(
                                            Text.literal(
                                                "Has been added a $amount level." +
                                                        " Now your level is ${player.getLevel()}"
                                            ), true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(
                            CommandManager.literal("add")
                                .then(
                                    CommandManager.literal(XP)
                                        .then(
                                            CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes {
                                                    val player = EntityArgumentType.getPlayer(it, "player")
                                                    val amount = IntegerArgumentType.getInteger(it, "amount")
                                                    EtriumData.addLevel(player, amount, XP)
                                                    player.sendMessage(
                                                        Text.literal(
                                                            "Has been added a $amount xp." +
                                                                    " Now your xp is ${player.getXp()}"
                                                        ), true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("remove")
                            .then(CommandManager.literal(ENERGY)
                                .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = FloatArgumentType.getFloat(it, "amount")
                                        EtriumData.removeEnergy(player, amount, ENERGY)
                                        player.sendMessage(
                                            Text.literal(
                                                "Has been removed a $amount energy." +
                                                        " Now your energy is ${player.getEnergy()}/${player.getMaxEnergy()}"
                                            ), true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("remove")
                            .then(CommandManager.literal(MAX_ENERGY)
                                .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = FloatArgumentType.getFloat(it, "amount")
                                        EtriumData.removeEnergy(player, amount, MAX_ENERGY)
                                        player.sendMessage(
                                            Text.literal(
                                                "Has been removed a $amount max energy." +
                                                        " Now your max energy is ${player.getMaxEnergy()}"
                                            ), true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("remove")
                            .then(CommandManager.literal(REGEN)
                                .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = FloatArgumentType.getFloat(it, "amount")
                                        EtriumData.removeEnergy(player, amount, REGEN)
                                        player.sendMessage(
                                            Text.literal(
                                                "Has been removed a $amount regen." +
                                                        " Now your regen is ${player.getRegen()}"
                                            ), true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("remove")
                            .then(CommandManager.literal(LEVEL)
                                .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = IntegerArgumentType.getInteger(it, "amount")
                                        EtriumData.removeLevel(player, amount, LEVEL)
                                        player.sendMessage(
                                            Text.literal(
                                                "Has been removed a $amount level." +
                                                        " Now your level is ${player.getLevel()}"
                                            ), true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(
                            CommandManager.literal("remove")
                                .then(
                                    CommandManager.literal(XP)
                                        .then(
                                            CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes {
                                                    val player = EntityArgumentType.getPlayer(it, "player")
                                                    val amount = IntegerArgumentType.getInteger(it, "amount")
                                                    EtriumData.removeLevel(player, amount, XP)
                                                    player.sendMessage(
                                                        Text.literal(
                                                            "Has been removed a $amount xp." +
                                                                    " Now your xp is ${player.getXp()}"
                                                        ), true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("set")
                            .then(CommandManager.literal(ENERGY)
                                .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = FloatArgumentType.getFloat(it, "amount")
                                        EtriumData.setEnergy(player, amount, ENERGY)
                                        player.sendMessage(
                                            Text.literal("Now your energy is ${player.getEnergy()}/${player.getMaxEnergy()}"),
                                            true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("set")
                            .then(CommandManager.literal(MAX_ENERGY)
                                .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = FloatArgumentType.getFloat(it, "amount")
                                        EtriumData.setEnergy(player, amount, MAX_ENERGY)
                                        player.sendMessage(
                                            Text.literal("Now your max energy is ${player.getMaxEnergy()}"),
                                            true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("set")
                            .then(CommandManager.literal(REGEN)
                                .then(CommandManager.argument("amount", FloatArgumentType.floatArg())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = FloatArgumentType.getFloat(it, "amount")
                                        EtriumData.setEnergy(player, amount, REGEN)
                                        player.sendMessage(
                                            Text.literal("Now your regen is ${player.getRegen()}"),
                                            true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("set")
                            .then(CommandManager.literal(LEVEL)
                                .then(CommandManager.argument("amount", IntegerArgumentType.integer())
                                    .executes {
                                        val player = EntityArgumentType.getPlayer(it, "player")
                                        val amount = IntegerArgumentType.getInteger(it, "amount")
                                        EtriumData.setLevel(player, amount, LEVEL)
                                        player.sendMessage(
                                            Text.literal("Now your level is ${player.getLevel()}"),
                                            true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(
                            CommandManager.literal("set")
                                .then(
                                    CommandManager.literal(XP)
                                        .then(
                                            CommandManager.argument("amount", IntegerArgumentType.integer())
                                                .executes {
                                                    val player = EntityArgumentType.getPlayer(it, "player")
                                                    val amount = IntegerArgumentType.getInteger(it, "amount")
                                                    EtriumData.setLevel(player, amount, XP)
                                                    player.sendMessage(
                                                        Text.literal("Now your xp is ${player.getXp()}"),
                                                        true
                                        )
                                        0
                                    }
                                )
                            )
                        )
                        .then(CommandManager.literal("get")
                            .then(CommandManager.literal(ENERGY)
                                .executes {
                                    val player = EntityArgumentType.getPlayer(it, "player")
                                    player.sendMessage(
                                        Text.literal("${player.name.string} energy: ${player.getEnergy()}"),
                                        true
                                    )
                                    0
                                }
                            )
                        )
                        .then(CommandManager.literal("get")
                            .then(CommandManager.literal(MAX_ENERGY)
                                .executes {
                                    val player = EntityArgumentType.getPlayer(it, "player")
                                    player.sendMessage(
                                        Text.literal("${player.name.string} max energy: ${player.getMaxEnergy()}"),
                                        true
                                    )
                                    0
                                }
                            )
                        )
                        .then(CommandManager.literal("get")
                            .then(CommandManager.literal(REGEN)
                                .executes {
                                    val player = EntityArgumentType.getPlayer(it, "player")
                                    player.sendMessage(
                                        Text.literal("${player.name.string} regen: ${player.getRegen()}"),
                                        true
                                    )
                                    0
                                }
                            )
                        )
                        .then(CommandManager.literal("get")
                            .then(CommandManager.literal(LEVEL)
                                .executes {
                                    val player = EntityArgumentType.getPlayer(it, "player")
                                    player.sendMessage(
                                        Text.literal("${player.name.string} level: ${player.getLevel()}"),
                                        true
                                    )
                                    0
                                }
                            )
                        )
                        .then(CommandManager.literal("get")
                            .then(CommandManager.literal(XP)
                                .executes {
                                    val player = EntityArgumentType.getPlayer(it, "player")
                                    player.sendMessage(
                                        Text.literal("${player.name.string} xp: ${player.getXp()}"),
                                        true
                                    )
                                    0
                                }
                            )
                        )
                    )

            )
        })
    }
}