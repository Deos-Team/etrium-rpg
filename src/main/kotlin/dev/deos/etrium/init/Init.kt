package dev.deos.etrium.init

import dev.deos.etrium.command.EnergyCommand
import dev.deos.etrium.config.ConfigManager
import dev.deos.etrium.network.DataPackets

object Init {
    init {
        EnergyCommand
        ConfigManager
        DataPackets.registerS2CPackets()
    }
}