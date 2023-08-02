package dev.deos.etrium

import dev.deos.etrium.command.BiomeEditorCommand
import dev.deos.etrium.command.EnergyCommand
import dev.deos.etrium.config.ConfigManager
import dev.deos.etrium.network.DataPackets
import dev.deos.etrium.registry.ItemGroupsRegistry
import dev.deos.etrium.registry.ItemRegistry

object Init {
    init {
        ConfigManager
        DataPackets.registerS2CPackets()
        ItemRegistry
        ItemGroupsRegistry
        EnergyCommand
        BiomeEditorCommand
    }
}