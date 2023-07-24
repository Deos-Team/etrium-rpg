package dev.deos.etrium

import com.mojang.logging.LogUtils
import dev.deos.etrium.event.AttackBlockEvent
import dev.deos.etrium.event.AttackEntityEvent
import dev.deos.etrium.event.PlayerJoinEvent
import dev.deos.etrium.event.PlayerTickEvent
import dev.deos.etrium.utils.EnergyData
import dev.deos.etrium.utils.EnergyData.getEnergy
import dev.deos.etrium.utils.EnergyData.getMaxEnergy
import dev.deos.etrium.utils.EnergyData.getRegen
import dev.deos.etrium.utils.IEntityDataSaver
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
            val nbt = it as IEntityDataSaver
            val tick = (it as PlayerTickContainer).getTick()
            if (tick % 20 != 0) return@register
            if (nbt.getEnergy() >= nbt.getMaxEnergy()) return@register
            EnergyData.addEnergy(nbt, nbt.getRegen())
            it.sendMessage(Text.literal("Energy ${nbt.getEnergy()}/${nbt.getMaxEnergy()}"), true)

        }

        PlayerJoinEvent.JOIN.register {
            val nbt = it as IEntityDataSaver
            if (!nbt.getPersistentData().contains("energy")) nbt.getPersistentData().putFloat("energy", 50.0f)
            if (!nbt.getPersistentData().contains("maxEnergy")) nbt.getPersistentData().putFloat("maxEnergy", 100.0f)
            if (!nbt.getPersistentData().contains("regen")) nbt.getPersistentData().putFloat("regen", 0.5f)
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