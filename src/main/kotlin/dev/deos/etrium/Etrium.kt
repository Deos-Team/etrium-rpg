package dev.deos.etrium

import com.mojang.logging.LogUtils
import dev.deos.etrium.config.ConfigManager
import dev.deos.etrium.event.AttackBlockEvent
import dev.deos.etrium.event.AttackEntityEvent
import dev.deos.etrium.event.PlayerJoinEvent
import dev.deos.etrium.event.PlayerTickEvent
import dev.deos.etrium.init.Init
import dev.deos.etrium.network.DataPackets
import dev.deos.etrium.utils.EnergyData
import dev.deos.etrium.utils.EnergyData.getEnergy
import dev.deos.etrium.utils.EnergyData.getMaxEnergy
import dev.deos.etrium.utils.EnergyData.getRegen
import dev.deos.etrium.utils.EnergyTypes.ENERGY
import dev.deos.etrium.utils.EnergyTypes.HEALTH
import dev.deos.etrium.utils.EnergyTypes.MAX_ENERGY
import dev.deos.etrium.utils.EnergyTypes.MAX_HEALTH
import dev.deos.etrium.utils.EnergyTypes.REGEN
import dev.deos.etrium.utils.IEntityDataSaver
import dev.deos.etrium.utils.PlayerTickContainer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.world.GameRules
import org.slf4j.Logger

object Etrium : ModInitializer {
    const val MI = "etrium"
    val logger: Logger = LogUtils.getLogger()
    override fun onInitialize() {
        Init
        PlayerTickEvent.TICK.register(::onPlayerTick)
        PlayerJoinEvent.JOIN.register(::onPlayerFirstJoin)
        ServerLifecycleEvents.SERVER_STARTED.register(::onServerStart)
        AttackEntityCallback.EVENT.register(AttackEntityEvent())
        AttackBlockCallback.EVENT.register(AttackBlockEvent())

    }

    private fun onPlayerFirstJoin(player: ServerPlayerEntity) {
        val nbt = player as IEntityDataSaver
        if (!nbt.getPersistentData().contains(HEALTH)) nbt.getPersistentData()
            .putInt(HEALTH, player.maxHealth.toInt())
        if (!nbt.getPersistentData().contains(MAX_HEALTH)) nbt.getPersistentData()
            .putInt(MAX_HEALTH, player.maxHealth.toInt())
        if (!nbt.getPersistentData().contains(ENERGY)) nbt.getPersistentData()
            .putFloat(ENERGY, ConfigManager.readCfg().default.energy)
        if (!nbt.getPersistentData().contains(MAX_ENERGY)) nbt.getPersistentData()
            .putFloat(MAX_ENERGY, ConfigManager.readCfg().default.maxEnergy)
        if (!nbt.getPersistentData().contains(REGEN)) nbt.getPersistentData()
            .putFloat(REGEN, ConfigManager.readCfg().default.regen)
    }

    private fun onPlayerTick(player: ServerPlayerEntity) {
        val tick = (player as PlayerTickContainer).getTick()
        if (tick % 4 == 0) {
            syncEnergy(player.getEnergy(), player)
            syncHealth(player.health.toInt(), player)
            syncMaxHealth(player.maxHealth.toInt(), player)
        }
        if (tick % 20 != 0) return
        if (player.getEnergy() >= player.getMaxEnergy()) return
        EnergyData.addEnergy(player, player.getRegen(), ENERGY)
    }

    private fun onServerStart(server: MinecraftServer) {
        if (server.gameRules.get(GameRules.KEEP_INVENTORY).get()) return
        server.gameRules.get(GameRules.KEEP_INVENTORY).set(true, server)
    }

    private fun syncEnergy(energy: Float, player: ServerPlayerEntity) {
        val buf = PacketByteBufs.create()
        buf.writeFloat(energy)
        ServerPlayNetworking.send(player, DataPackets.ENERGY_ID, buf)
    }

    private fun syncHealth(health: Int, player: ServerPlayerEntity) {
        val buf = PacketByteBufs.create()
        buf.writeInt(health)
        ServerPlayNetworking.send(player, DataPackets.HEALTH_ID, buf)
    }

    private fun syncMaxHealth(health: Int, player: ServerPlayerEntity) {
        val buf = PacketByteBufs.create()
        buf.writeInt(health)
        ServerPlayNetworking.send(player, DataPackets.MAX_HEALTH_ID, buf)
    }

}