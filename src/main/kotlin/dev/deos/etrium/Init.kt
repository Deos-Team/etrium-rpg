package dev.deos.etrium

import dev.deos.etrium.command.EnergyCommand
import dev.deos.etrium.config.ConfigManager

object Init {
    init {
        EnergyCommand
        ConfigManager
    }
}