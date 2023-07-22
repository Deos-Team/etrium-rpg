package dev.deos.etrium

import com.mojang.logging.LogUtils
import dev.deos.etrium.event.AttackBlockEvent
import dev.deos.etrium.event.AttackEntityEvent
import dev.deos.etrium.utils.EnergyContainer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import org.slf4j.Logger

object Etrium : ModInitializer {
    const val MI = "etrium"
    val logger: Logger = LogUtils.getLogger()
    override fun onInitialize() {
        AttackEntityCallback.EVENT.register(AttackEntityEvent())
        AttackBlockCallback.EVENT.register(AttackBlockEvent())
        ServerTickEvents.START_SERVER_TICK.register {
            if (it.ticks % 100 == 0) {
                it.playerManager.playerList.forEach { p ->
                    val player = p as EnergyContainer
                    if (player.getEnergy() < player.getMaxEnergy()) {
                        if (player.getEnergy() + 1.0f > player.getMaxEnergy()) {
                            player.setEnergy(player.getMaxEnergy())
                        } else player.setEnergy(player.getEnergy() + 1.0f)
                    }
                }
            }
        }
    }
}