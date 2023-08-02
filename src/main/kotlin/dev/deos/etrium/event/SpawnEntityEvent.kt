package dev.deos.etrium.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.network.ServerPlayerEntity

object SpawnEntityEvent {
    val SPAWN: Event<SpawnEntityCallback> = EventFactory.createArrayBacked(SpawnEntityCallback::class.java)
    { callbacks: Array<SpawnEntityCallback> ->
        SpawnEntityCallback { entity ->
            callbacks.forEach {
                it.onSpawn(entity)
            }
        }
    }

    fun interface SpawnEntityCallback {
        fun onSpawn(entity: MobEntity)
    }
}