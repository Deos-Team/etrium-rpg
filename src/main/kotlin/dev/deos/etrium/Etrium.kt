package dev.deos.etrium

import com.mojang.logging.LogUtils
import dev.deos.etrium.config.ConfigManager
import dev.deos.etrium.event.AttackEntityEvent
import dev.deos.etrium.event.PlayerJoinEvent
import dev.deos.etrium.event.PlayerTickEvent
import dev.deos.etrium.network.DataPackets
import dev.deos.etrium.utils.EnergyData
import dev.deos.etrium.utils.EnergyData.getEnergy
import dev.deos.etrium.utils.EnergyData.getMaxEnergy
import dev.deos.etrium.utils.EnergyData.getRegen
import dev.deos.etrium.utils.EnergyRequired
import dev.deos.etrium.utils.EnergyTypes.ENERGY
import dev.deos.etrium.utils.EnergyTypes.HEALTH
import dev.deos.etrium.utils.EnergyTypes.MAX_ENERGY
import dev.deos.etrium.utils.EnergyTypes.MAX_HEALTH
import dev.deos.etrium.utils.EnergyTypes.REGEN
import dev.deos.etrium.utils.IEntityDataSaver
import dev.deos.etrium.utils.PlayerTickContainer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.GameRules
import net.minecraft.world.World
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
        PlayerBlockBreakEvents.BEFORE.register(::onBlockBreaking)
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

    private fun onBlockBreaking(
        world: World, playerEntity: PlayerEntity, blockPos: BlockPos,
        blockState: BlockState, blockEntity: BlockEntity?
    ): Boolean {
        if (!world.isClient() && !playerEntity.isSpectator) {
            return if (playerEntity.getEnergy() >= EnergyRequired.BlockBreaking.value) {
                if (!playerEntity.isCreative) {
                    EnergyData.removeEnergy(playerEntity, EnergyRequired.BlockBreaking.value, ENERGY)
                }
                true
            } else {
                playerEntity.sendMessage(
                    Text.literal("Don't enough energy"), true
                )
                false
            }
        }
        return false
    }

    private fun onPlayerTick(player: ServerPlayerEntity) {
        val tick = (player as PlayerTickContainer).getTick()
        if (tick % 4 == 0) {
            syncEnergy(player.getEnergy(), player)
            syncMaxEnergy(player.getMaxEnergy(), player)
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

    private fun syncMaxEnergy(energy: Float, player: ServerPlayerEntity) {
        val buf = PacketByteBufs.create()
        buf.writeFloat(energy)
        ServerPlayNetworking.send(player, DataPackets.MAX_ENERGY_ID, buf)
    }

}