package dev.deos.etrium

import com.mojang.logging.LogUtils
import dev.deos.etrium.event.AttackBlockEvent
import dev.deos.etrium.event.AttackEntityEvent
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import org.slf4j.Logger

object Etrium : ModInitializer {
    const val MI = "etrium"
    val logger: Logger = LogUtils.getLogger()
    override fun onInitialize() {
        AttackEntityCallback.EVENT.register(AttackEntityEvent())
        AttackBlockCallback.EVENT.register(AttackBlockEvent())
    }
}