package dev.deos.etrium

import com.mojang.logging.LogUtils
import dev.deos.etrium.event.AttackBlockEvent
import dev.deos.etrium.event.AttackEntityEvent
import dev.deos.etrium.event.PlayerTickEvent
import dev.deos.etrium.utils.EnergyContainer
import dev.deos.etrium.utils.PlayerTickContainer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.text.Text
import org.slf4j.Logger

object Etrium : ModInitializer {
    const val MI = "etrium"
    val logger: Logger = LogUtils.getLogger()
    override fun onInitialize() {


        PlayerTickEvent.TICK.register {
            val player = it as EnergyContainer
            val testPlayer = it as PlayerTickContainer
            if (testPlayer.getTicks() % 20 == 0) {
                if (player.getEnergy() >= player.getMaxEnergy()) {
                    it.sendMessage(Text.literal("Max Energy: ${player.getMaxEnergy()}"))
                    it.sendMessage(Text.literal(player.getRegenEnergy().toString()))
                    player.setEnergy(player.getEnergy() + player.getRegenEnergy())
                    it.sendMessage(Text.literal("Energy ${player.getEnergy()}/${player.getMaxEnergy()}"), true)
                }
            }
        }

        AttackEntityCallback.EVENT.register(AttackEntityEvent())
        AttackBlockCallback.EVENT.register(AttackBlockEvent())
        ServerTickEvents.START_SERVER_TICK.register {
            /*if (it.ticks % 100 == 0) {
                it.playerManager.playerList.forEach { p ->
                    val player = p as EnergyContainer
                    if (player.getEnergy() < player.getMaxEnergy()) {
                        if (player.getEnergy() + 1.0f > player.getMaxEnergy()) {
                            player.setEnergy(player.getMaxEnergy())
                        } else player.setEnergy(player.getEnergy() + 1.0f)
                    }
                }
            }*/
        }
    }
}