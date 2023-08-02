package dev.deos.etrium.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.core.jmx.Server

object EntityKillEvent {
    val Kill: Event<EntityDeathCallback> = EventFactory.createArrayBacked(EntityDeathCallback::class.java)
    { callbacks: Array<EntityDeathCallback> ->
        EntityDeathCallback { entity, player ->
            callbacks.forEach {
                it.onKill(entity, player)
            }
        }
    }

    fun interface EntityDeathCallback {
        fun onKill(attacker: ServerPlayerEntity, entity: Entity)
    }
}