package dev.deos.etrium.item

import com.mojang.brigadier.exceptions.CommandSyntaxException
import dev.deos.etrium.Etrium
import net.minecraft.block.Blocks
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.server.command.FillBiomeCommand
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.util.math.*
import net.minecraft.world.GameRules
import net.minecraft.world.Heightmap
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.source.BiomeCoords
import net.minecraft.world.biome.source.BiomeSupplier
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkStatus
import org.apache.commons.lang3.mutable.MutableInt
import java.util.function.Predicate

class BiomeEditor(settings: Settings?) : Item(settings) {

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        if (stack.hasNbt()) return
        val nbt = NbtCompound()
        nbt.putInt("radius", 1)
        nbt.putString("biome", "empty")
        stack.nbt = nbt
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val stack = context.stack
        val world = context.world
        val radius = stack.getRadius()
        val biomeKey = stack.getBiome()
        if (biomeKey != "empty") {
            if (!world.isClient) {
                val biome = world.registryManager.get(RegistryKeys.BIOME)
                    .getEntry(RegistryKey.of(RegistryKeys.BIOME, Identifier(biomeKey))).get()
                fillBiome(world, context.blockPos, radius, biome)
            }
        }
        return ActionResult.SUCCESS
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val radius = stack.getRadius()
        val biomeKey = stack.getBiome()
        var pos = user.pos
        val vector = user.rotationVector
        (0..72).forEach {
            pos = pos.add(vector)
            val blockPos = BlockPos(pos.x.toInt(), pos.y.toInt() + 1, pos.z.toInt())
            if (!world.getBlockState(blockPos).isAir) {
//            world.setBlocks(blockPos, stack)
                if (biomeKey != "empty") {
                    if (!world.isClient) {
                        val biome = world.registryManager.get(RegistryKeys.BIOME)
                            .getEntry(RegistryKey.of(RegistryKeys.BIOME, Identifier(biomeKey))).get()
                        fillBiome(world, blockPos, radius, biome)
                    }
                }
                return TypedActionResult.success(stack, true)
            }
        }
//        chunk.populateBiomes()
        return TypedActionResult.fail(stack)
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        if (stack.hasNbt()) {
            val radius = stack.getRadius()
            val biome = stack.getBiome()
            tooltip.add(Text.literal("Biome: ").append(Text.literal(biome).formatted(Formatting.AQUA)))
            tooltip.add(Text.literal("Radius: ").append(Text.literal(radius.toString()).formatted(Formatting.GREEN)))
        } else {
            tooltip.add(Text.literal("Biome: ").append(Text.literal("empty").formatted(Formatting.AQUA)))
            tooltip.add(Text.literal("Radius: ").append(Text.literal("1").formatted(Formatting.GREEN)))
        }

    }

    /*private fun World.setBlocks(origin: BlockPos, stack: ItemStack){
        if (!stack.hasNbt()) return
        val mode = stack.getMode()
        when (mode) {
            SINGLE_MODE -> {
                this.setBlockState(origin, Blocks.STONE.defaultState)
            }
            FIVE_MODE -> {
                (-5..5).forEach { addX ->
                    (-5..5).forEach { addZ ->
                        val x = origin.x + addX
                        val z = origin.z + addZ
                        val y = this.getTopY(Heightmap.Type.WORLD_SURFACE, x, z)
                        this.setBlockState(BlockPos(x, y, z), Blocks.STONE.defaultState)
                    }
                }
            }
            FORTY -> {
                (-40..40).forEach { addX ->
                    (-40..40).forEach { addZ ->
                        val x = origin.x + addX
                        val z = origin.z + addZ
                        val y = this.getTopY(Heightmap.Type.WORLD_SURFACE, x, z)
                        this.setBlockState(BlockPos(x, y, z), Blocks.STONE.defaultState)
                    }
                }
            }
        }
    }*/

    private fun ItemStack.getRadius(): Int {
        val nbt = this.nbt
        return nbt!!.getInt("radius")
    }

    private fun ItemStack.getBiome(): String {
        val nbt = this.nbt
        return nbt?.getString("biome") ?: "empty"
    }

    @Throws(CommandSyntaxException::class)
    private fun fillBiome(
        world: World,
        origin: BlockPos,
        radius: Int,
        biome: RegistryEntry.Reference<Biome>
    ): Int {
        var j: Int
        val blockPos1 = BlockPos(origin.x - radius, world.bottomY, origin.z - radius)
        val blockPos2 = BlockPos(origin.x + radius, world.topY, origin.z + radius)
        val blockBox = BlockBox.create(blockPos1, blockPos2)
        val i = blockBox.blockCountX * blockBox.blockCountY * blockBox.blockCountZ
        val serverWorld = world as ServerWorld
        val list = ArrayList<Chunk>()
        for (k in ChunkSectionPos.getSectionCoord(blockBox.minZ)..ChunkSectionPos.getSectionCoord(blockBox.maxZ)) {
            for (l in ChunkSectionPos.getSectionCoord(blockBox.minX)..ChunkSectionPos.getSectionCoord(blockBox.maxX)) {
                val chunk = serverWorld.getChunk(l, k, ChunkStatus.FULL, false)
                    ?: throw FillBiomeCommand.UNLOADED_EXCEPTION.create()
                list.add(chunk)
            }
        }
        val mutableInt = MutableInt(0)
        for (chunk in list) {
            chunk.populateBiomes(
                createBiomeSupplier(mutableInt, chunk, blockBox, biome),
                serverWorld.chunkManager.noiseConfig.multiNoiseSampler
            )
            chunk.setNeedsSaving(true)
        }
        serverWorld.chunkManager.threadedAnvilChunkStorage.sendChunkBiomePackets(list)
        return mutableInt.value
    }

    private fun createBiomeSupplier(
        counter: MutableInt,
        chunk: Chunk,
        box: BlockBox,
        biome: RegistryEntry<Biome>,
    ): BiomeSupplier {
        return BiomeSupplier { x: Int, y: Int, z: Int, noise: MultiNoiseSampler? ->
            val i = BiomeCoords.toBlock(x)
            val j = BiomeCoords.toBlock(y)
            val k = BiomeCoords.toBlock(z)
            val registryEntry2 = chunk.getBiomeForNoiseGen(x, y, z)
            if (box.contains(i, j, k)) {
                counter.increment()
                return@BiomeSupplier biome
            }
            registryEntry2
        }
    }

}