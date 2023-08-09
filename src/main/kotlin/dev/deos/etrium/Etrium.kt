package dev.deos.etrium

import com.mojang.logging.LogUtils
import dev.deos.etrium.command.BiomeEditorCommand
import dev.deos.etrium.command.EnergyCommand
import dev.deos.etrium.config.ConfigManager
import dev.deos.etrium.event.*
import dev.deos.etrium.network.DataPackets
import dev.deos.etrium.registry.ItemGroupsRegistry
import dev.deos.etrium.registry.ItemRegistry
import dev.deos.etrium.utils.*
import dev.deos.etrium.utils.EnergyTypes.ENERGY
import dev.deos.etrium.utils.EnergyTypes.LEVEL
import dev.deos.etrium.utils.EnergyTypes.MAX_ENERGY
import dev.deos.etrium.utils.EnergyTypes.REGEN
import dev.deos.etrium.utils.EnergyTypes.XP
import dev.deos.etrium.utils.EtriumData.getEnergy
import dev.deos.etrium.utils.EtriumData.getLevel
import dev.deos.etrium.utils.EtriumData.getMaxEnergy
import dev.deos.etrium.utils.EtriumData.getRegen
import dev.deos.etrium.world.biome.BiomeRegistry
import dev.deos.etrium.world.biome.BiomeRegistry.key
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.BlockPos
import net.minecraft.world.GameRules
import net.minecraft.world.World
import org.slf4j.Logger
import kotlin.random.Random

object Etrium : ModInitializer {
    const val MI = "etrium"
    val logger: Logger = LogUtils.getLogger()

    override fun onInitialize() {
        onInit()
        PlayerTickEvent.TICK.register(::onPlayerTick)
        PlayerJoinEvent.JOIN.register(::onPlayerFirstJoin)
        ServerLifecycleEvents.SERVER_STARTED.register(::onServerStart)
        AttackEntityCallback.EVENT.register(AttackEntityEvent())
        PlayerBlockBreakEvents.BEFORE.register(::onBlockBreaking)
        EntityKillEvent.Kill.register(::onEntityKill)
//        ServerLivingEntityEvents.ALLOW_DEATH.register(::beforeDeath)
        SpawnEntityEvent.SPAWN.register(::onSpawnEntity)
//        ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register(::onKilledByOther)
    }

    private fun onPlayerFirstJoin(player: ServerPlayerEntity) {
        val nbt = player as IEntityDataSaver
        if (!nbt.getPersistentData().contains(ENERGY)) nbt.getPersistentData()
            .putFloat(ENERGY, ConfigManager.readCfg().default.energy)
        if (!nbt.getPersistentData().contains(MAX_ENERGY)) nbt.getPersistentData()
            .putFloat(MAX_ENERGY, ConfigManager.readCfg().default.maxEnergy)
        if (!nbt.getPersistentData().contains(REGEN)) nbt.getPersistentData()
            .putFloat(REGEN, ConfigManager.readCfg().default.regen)
        if (!nbt.getPersistentData().contains(LEVEL)) nbt.getPersistentData().putInt(LEVEL, 1)
        if (!nbt.getPersistentData().contains(XP)) nbt.getPersistentData().putInt(XP, 0)
        if (!nbt.getPersistentData().contains("entityLevel")) nbt.getPersistentData().putInt("entityLevel", 0)
    }

    private fun beforeDeath(entity: LivingEntity, damageSource: DamageSource, damage: Float): Boolean {
        if (damageSource.attacker?.isPlayer == true && !entity.world.isClient) {
            val player = damageSource.attacker as PlayerEntity
            if (!player.world.isClient) {
                val entityNbt = (entity as IEntityDataSaver).getPersistentData()
                val playerNbt = (player as IEntityDataSaver).getPersistentData()
                val level = entityNbt.getInt(LEVEL)
                val playerLevel = playerNbt.getInt(LEVEL)
                if (playerLevel > level) player.sendMessage(Text.literal("You didn't got any XP. Entity level's $level"))
                if (playerLevel == level) {
                    val random = Random.nextInt(1, 100)
                    EtriumData.addLevel(player, random, XP)
                    player.sendMessage(Text.literal("You got $random XP. Entity level's $level"))
                }
                if (playerLevel < level) {
                    val random = Random.nextInt(1, 100) * (2 * level - playerLevel)
                    EtriumData.addLevel(entity, random, XP)
                    entity.sendMessage(Text.literal("You got $random XP. Entity level's $level"))
                }
            }
        }
        return false
    }

    private fun onKilledByOther(world: ServerWorld, entity: Entity, killedEntity: LivingEntity) {
        if (entity is PlayerEntity && !world.isClient) {
            val entityNbt = (killedEntity as IEntityDataSaver).getPersistentData()
            val playerNbt = (entity as IEntityDataSaver).getPersistentData()
            val level = entityNbt.getInt(LEVEL)
            val playerLevel = playerNbt.getInt(LEVEL)
            if (playerLevel > level) entity.sendMessage(Text.literal("You didn't got any XP. Entity level's $level"))
            if (playerLevel == level) {
                val random = Random.nextInt(1, 100)
                EtriumData.addLevel(entity, random, XP)
                entity.sendMessage(Text.literal("You got $random XP. Entity level's $level"))
            }
            if (playerLevel < level) {
                val random = Random.nextInt(1, 100) * (2 * level - playerLevel)
                EtriumData.addLevel(entity, random, XP)
                entity.sendMessage(Text.literal("You got $random XP. Entity level's $level"))
            }
        }
    }

    private fun onSpawnEntity(entity: LivingEntity) {
        val nbt = (entity as IEntityDataSaver).getPersistentData()
        val world = entity.world
        val blockPos = entity.blockPos
        val biome = world.getBiome(blockPos)
        var level = Random.nextInt(1, 12)
        val name = entity.name.string.lowercase()
        if (LevelAmount.entityLevels.contains(name)) {
            level = LevelAmount.entityLevels[name]!!.random()
        }
        // DONT FORGOT
        nbt.putInt(LEVEL, level)
        when (biome.key.get()) {
            BiomeRegistry.ModBiomes.DEAD_FOREST.key() -> {
//                nbt.putInt(LEVEL, level)
                entity.customName = Text.literal("Lvl: $level").formatted(Formatting.BOLD, Formatting.GREEN)
            }
        }
    }

    private fun onBlockBreaking(
        world: World, playerEntity: PlayerEntity, blockPos: BlockPos,
        blockState: BlockState, blockEntity: BlockEntity?
    ): Boolean {
        if (!world.isClient() && !playerEntity.isSpectator) {
            return if (playerEntity.getEnergy() >= EnergyRequired.BlockBreaking.value) {
                if (!playerEntity.isCreative) {
                    EtriumData.removeEnergy(playerEntity, EnergyRequired.BlockBreaking.value, ENERGY)
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

    private fun onEntityKill(playerEntity: ServerPlayerEntity, entity: Entity) {
        if (!playerEntity.world.isClient) {
            val entityNbt = (entity as IEntityDataSaver).getPersistentData()
            val playerNbt = (playerEntity as IEntityDataSaver).getPersistentData()
            val level = entityNbt.getInt(LEVEL)
            if (level == 0) return
            val playerLevel = playerNbt.getInt(LEVEL)
            if (playerLevel > level) playerEntity.sendMessage(Text.literal("You didn't got any XP. Entity level's $level"))
            if (playerLevel == level) {
                val random = Random.nextInt(1, 100)
                EtriumData.addLevel(playerEntity, random, XP)
                playerEntity.sendMessage(Text.literal("You got $random XP. Entity level's $level"))
            }
            if (playerLevel < level) {
                val random = Random.nextInt(1, 100) * (2 * level - playerLevel)
                EtriumData.addLevel(playerEntity, random, XP)
                playerEntity.sendMessage(Text.literal("You got $random XP. Entity level's $level"))
            }
        }
    }

    private fun onPlayerTick(player: ServerPlayerEntity) {
        val tick = (player as PlayerTickContainer).getTick()
        if (tick % 4 == 0) {
            syncEnergy(player.getEnergy(), player)
            syncMaxEnergy(player.getMaxEnergy(), player)
            syncLevel(player.getLevel(), player)
            val nbt = (player as IEntityDataSaver).getPersistentData()
            val entityLevel = nbt.getInt("entityLevel")
            syncEntityLevel(entityLevel, player)
        }
        if (tick % 20 != 0) return
        if (player.getEnergy() >= player.getMaxEnergy()) return
        EtriumData.addEnergy(player, player.getRegen(), ENERGY)
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

    private fun syncLevel(level: Int, player: ServerPlayerEntity) {
        val buf = PacketByteBufs.create()
        buf.writeInt(level)
        ServerPlayNetworking.send(player, DataPackets.LEVEL_ID, buf)
    }

    private fun syncMaxEnergy(energy: Float, player: ServerPlayerEntity) {
        val buf = PacketByteBufs.create()
        buf.writeFloat(energy)
        ServerPlayNetworking.send(player, DataPackets.MAX_ENERGY_ID, buf)
    }

    private fun syncEntityLevel(level: Int, player: ServerPlayerEntity) {
        val buf = PacketByteBufs.create()
        buf.writeInt(level)
        ServerPlayNetworking.send(player, DataPackets.SERVER_ENTITY_ID, buf)
    }

    private fun onInit() {
        DataPackets.registerC2SPackets()
        ConfigManager
        ItemRegistry
        ItemGroupsRegistry
        EnergyCommand
        BiomeEditorCommand
    }

}