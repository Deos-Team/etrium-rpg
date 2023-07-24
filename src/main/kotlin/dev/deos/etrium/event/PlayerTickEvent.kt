package dev.deos.etrium.event

import dev.deos.etrium.event.PlayerTickEvent.PlayerTickCallback
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.network.ServerPlayerEntity

object PlayerTickEvent {
    val TICK: Event<PlayerTickCallback> = EventFactory.createArrayBacked(PlayerTickCallback::class.java)
    { callbacks: Array<PlayerTickCallback> ->
        PlayerTickCallback { player ->
            callbacks.forEach {
                it.onTick(player)
            }
        }
    }

    fun interface PlayerTickCallback {
        fun onTick(player: ServerPlayerEntity)
    }

}
