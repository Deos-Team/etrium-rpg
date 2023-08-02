package dev.deos.etrium.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.FloatArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import dev.deos.etrium.item.BiomeEditor
import dev.deos.etrium.registry.ItemRegistry
import dev.deos.etrium.utils.EnergyTypes
import dev.deos.etrium.utils.EtriumData
import dev.deos.etrium.utils.EtriumData.getEnergy
import dev.deos.etrium.utils.EtriumData.getMaxEnergy
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.RegistryEntryArgumentType
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.world.biome.Biome

object BiomeEditorCommand {
    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource?>,
                                                                                 registryAccess: CommandRegistryAccess?, environment: CommandManager.RegistrationEnvironment? ->
            dispatcher.register(
                CommandManager.literal("editor").requires { it.hasPermissionLevel(4) }
                    .then(CommandManager.literal("biome")
                        .then(RequiredArgumentBuilder.argument<ServerCommandSource, RegistryEntry.Reference<Biome>>(
                            "value",
                            RegistryEntryArgumentType.registryEntry(registryAccess, RegistryKeys.BIOME)
                        ).executes {
                            if (it.source.isExecutedByPlayer) {
                                val player = it.source.player!!
                                val stack = player.mainHandStack
                                if (stack.isOf(ItemRegistry.ModItems.BIOME_EDITOR.item)) {
                                    val biome = RegistryEntryArgumentType.getRegistryEntry(
                                        it,
                                        "value",
                                        RegistryKeys.BIOME
                                    )
                                        .registryKey().value.toString()
                                    val nbt = NbtCompound()
                                    nbt.putString("biome", biome)
                                    stack.nbt?.putString("biome", biome)
                                }
                            }
                            0
                        }
                        )
                    )
                    .then(CommandManager.literal("radius")
                        .then(RequiredArgumentBuilder.argument<ServerCommandSource, Int>(
                            "value", IntegerArgumentType.integer()
                        ).executes {
                            if (it.source.isExecutedByPlayer) {
                                val player = it.source.player!!
                                val stack = player.mainHandStack
                                if (stack.isOf(ItemRegistry.ModItems.BIOME_EDITOR.item)) {
                                    val radius = IntegerArgumentType.getInteger(it, "value")
                                    stack.nbt?.putInt("radius", radius)
                                }
                            }
                            0
                        }
                        )
                    )
            )
        }
        )
    }
}