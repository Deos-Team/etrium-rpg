package dev.deos.etrium.client

import com.mojang.blaze3d.systems.RenderSystem
import dev.deos.etrium.Etrium
import dev.deos.etrium.network.DataPackets
import dev.deos.etrium.utils.EnergyTypes.LEVEL
import dev.deos.etrium.utils.IEntityDataSaver
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.GameRenderer
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.fluid.FluidState
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.*
import net.minecraft.util.math.ColorHelper.Argb
import net.minecraft.util.shape.VoxelShape
import net.minecraft.world.BlockView
import net.minecraft.world.RaycastContext
import net.minecraft.world.RaycastContext.FluidHandling
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate


class EtriumHud : HudRenderCallback, BlockView {

    private val HUD = Identifier(Etrium.MI, "textures/hud/hud.png")
    private val client = MinecraftClient.getInstance()
    private var height = 0f
    private var width = 0f
    val yellow = takeColor(245, 203, 66)
    val black = takeColor(0, 0, 0)
    val white = takeColor(255, 255, 255)

    override fun onHudRender(drawContext: DrawContext, tickDelta: Float) {
        if (client != null) {
            height = drawContext.scaledWindowHeight.toFloat()
            width = drawContext.scaledWindowWidth.toFloat()
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram)
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
        RenderSystem.setShaderTexture(0, HUD)

        if (!(client.player as PlayerEntity).isCreative && !(client.player as PlayerEntity).isSpectator) {
            drawContext.apply {
                renderHealthBar()
                renderHungerBar()
                renderOxygenBar()
                renderEnergyBar()
                renderArmor()
            }
        }

        drawContext.apply {
            renderEntityLevel(tickDelta)
        }

    }

    private fun syncEntityLevel(id: Int) {
        val buf = PacketByteBufs.create()
        buf.writeInt(id)
        ClientPlayNetworking.send(DataPackets.CLIENT_ENTITY_ID, buf)
    }

    private fun DrawContext.renderEntityLevel(tickDelta: Float) {
        val entity = getEntity(tickDelta) ?: return
        syncEntityLevel(entity.id)
        val level = (client.player as IEntityDataSaver).getPersistentData().getInt("entityLevel")
        val playerLevel = (client.player as IEntityDataSaver).getPersistentData().getInt(LEVEL)
        val color =
            if (level <= playerLevel + 1) Formatting.GREEN else if (level - playerLevel <= 6) Formatting.YELLOW else Formatting.RED
        RenderSystem.enableBlend()
        this.drawTextWithShadow(
            client.textRenderer, Text.literal(entity.name.string).formatted(Formatting.BOLD),
            (width / 100).toInt(), (height / 100).toInt(), white
        )
        this.drawTextWithShadow(
            client.textRenderer, Text.literal("Lvl: $level").formatted(Formatting.BOLD, color),
            (width / 100).toInt(), (height / 100 + 30).toInt(), white
        )
        renderEntityHealthBar(entity)
        RenderSystem.disableBlend()
    }

    private fun DrawContext.renderEntityHealthBar(entity: LivingEntity) {
        this.drawTexture(HUD, (width / 100 - 1).toInt(), (height / 100 + 11f).toInt(), 0, 0, 182, 14)
        val health = entity.health
        val maxHealth = entity.maxHealth
        if (health > 0) {
            val wth = if (maxHealth >= health) (182f * (health / maxHealth)).toInt() else 182
            this.drawTexture(
                HUD,
                (width / 100 - 1).toInt(),
                (height / 100 + 11f).toInt(),
                0,
                14,
                wth,
                14
            )
//          37f
            val wdth = health.toInt().toString().length * 6

            this.drawText(
                client.textRenderer, Text.literal("${health.toInt()}/${maxHealth.toInt()}"),
                (width / 100 + 92 - wdth).toInt(), (height / 100 + 14).toInt(), white, true
            )
        }
    }

    private fun DrawContext.renderHealthBar() {
        this.drawTexture(HUD, (width / 2 - 91).toInt(), (height - 40f).toInt(), 0, 0, 182, 14)
        val health = (client.player as PlayerEntity).health
        val maxHealth = (client.player as PlayerEntity).maxHealth
        if (health > 0) {
            val wth = if (maxHealth >= health) (182f * (health / maxHealth)).toInt() else 182
            this.drawTexture(
                HUD,
                (width / 2 - 91).toInt(),
                (height - 40f).toInt(),
                0,
                14,
                wth,
                14
            )
//          37f
            val wdth = health.toInt().toString().length * 6

            this.drawText(
                client.textRenderer, Text.literal("${health.toInt()}/${maxHealth.toInt()}"),
                (width / 2 - 3 - wdth).toInt(), (height - 37f).toInt(), white, true
            )
        }
    }

    private fun DrawContext.renderHungerBar() {
        this.drawTexture(HUD, (width / 2 - 91).toInt(), (height - 52).toInt(), 0, 28, 81, 8)
        val hunger = (client.player as PlayerEntity).hungerManager.foodLevel
        if (hunger > 0) {
            val wth = (81f * (hunger.toFloat() / 20f)).toInt()
            this.drawTexture(
                HUD,
                (width / 2 - 91).toInt(),
                (height - 52f).toInt(),
                0,
                36,
                wth,
                8
            )
        }
        this.drawTexture(HUD, (width / 2 - 96).toInt(), (height - 54).toInt(), 182, 0, 14, 14)
    }

    private fun DrawContext.renderOxygenBar() {
        val oxygen = (client.player as PlayerEntity).air
        val maxOxygen = (client.player as PlayerEntity).maxAir
        if (oxygen >= maxOxygen) return
        this.drawTexture(HUD, (width / 2 + 10).toInt(), (height - 52).toInt(), 81, 28, 81, 8)
        val wth = (81f * (oxygen.toFloat() / maxOxygen)).toInt()
        this.drawTexture(
            HUD,
            (width / 2 + 10).toInt(),
            (height - 52f).toInt(),
            81,
            36,
            wth,
            8
        )
        this.drawTexture(HUD, (width / 2 + 3).toInt(), (height - 55).toInt(), 196, 0, 14, 15)
    }

    private fun DrawContext.renderArmor() {
        this.drawTexture(HUD, (width / 2 - 120f).toInt(), (height - 36f).toInt(), 182, 15, 22, 31)
        val armor = (client.player as PlayerEntity).armor
        if (armor > 0) {
            val wdth = armor.toString().length * 6
            this.drawText(
                client.textRenderer, Text.literal(armor.toString()),
                (width / 2 - 125f - wdth).toInt(), (height - 25f).toInt(), white, true
            )
        }
    }

    private fun DrawContext.renderEnergyBar() {
        this.drawTexture(HUD, (width / 2 + 100).toInt(), (height - 36f).toInt(), 210, 0, 17, 31)
        val energy = (client.player as IEntityDataSaver).getPersistentData().getFloat("energy")
        val maxEnergy = (client.player as IEntityDataSaver).getPersistentData().getFloat("maxEnergy")
        val hgt = if (maxEnergy >= energy) (31f * (1f - (energy / maxEnergy))).toInt() else 0
        this.drawTexture(
            HUD,
            (width / 2 + 100).toInt(),
            (height - 36f).toInt(),
            227,
            0,
            20,
            hgt
        )
        this.drawText(
            client.textRenderer, Text.literal("/"),
            (width / 2 + 124f).toInt(), (height - 25f).toInt(), white, true
        )
        this.drawText(
            client.textRenderer, Text.literal(energy.toInt().toString()),
            (width / 2 + 124f).toInt(), (height - 36f).toInt(), white, true
        )
        this.drawText(
            client.textRenderer, Text.literal(maxEnergy.toInt().toString()),
            (width / 2 + 124f).toInt(), (height - 15f).toInt(), white, true
        )
    }

    private fun takeColor(r: Int, g: Int, b: Int, invis: Int = 255): Int {
        return Argb.getArgb(invis, r, g, b)
    }

    override fun raycast(context: RaycastContext): BlockHitResult? {
        return BlockView.raycast(
            context.start, context.end, context,
            BiFunction { c, pos: BlockPos? ->
                val block = getBlockState(pos)
                if (!block.isOpaque) {
                    return@BiFunction null
                }
                val blockShape: VoxelShape = c.getBlockShape(block, this, pos)
                raycastBlock(c.start, c.end, pos, blockShape, block)
            },
            Function { c ->
                val v: Vec3d = c.start.subtract(c.end)
                return@Function BlockHitResult.createMissed(
                    c.end,
                    Direction.getFacing(v.x, v.y, v.z),
                    BlockPos(Vec3i(c.end.x.toInt(), c.end.y.toInt(), c.end.z.toInt()))
                )
            })
    }

    private fun setupRayTraceContext(
        player: PlayerEntity,
        distance: Double,
        fluidHandling: FluidHandling
    ): RaycastContext {
        val pitch = player.pitch
        val yaw = player.yaw
        val fromPos = player.getCameraPosVec(1.0f)
        val float_3 = MathHelper.cos(-yaw * 0.017453292f - 3.1415927f)
        val float_4 = MathHelper.sin(-yaw * 0.017453292f - 3.1415927f)
        val float_5 = -MathHelper.cos(-pitch * 0.017453292f)
        val xComponent = float_4 * float_5
        val yComponent = MathHelper.sin(-pitch * 0.017453292f)
        val zComponent = float_3 * float_5
        val toPos = fromPos.add(
            xComponent.toDouble() * distance,
            yComponent.toDouble() * distance,
            zComponent.toDouble() * distance
        )
        return RaycastContext(fromPos, toPos, RaycastContext.ShapeType.OUTLINE, fluidHandling, player)
    }

    private fun getEntity(tickDelta: Float): LivingEntity? {
        val isVisible = Predicate<net.minecraft.entity.Entity> { entity -> !entity.isSpectator }
        val viewer = client.cameraEntity ?: return null
        val reachDistance: Double = 14.0

        val position = viewer.getCameraPosVec(1.0f)
        val look = viewer.getRotationVec(tickDelta)
        val max = position.add(look.x * reachDistance, look.y * reachDistance, look.z * reachDistance)
        val searchBox: Box = viewer.boundingBox.stretch(look.multiply(reachDistance)).expand(1.0, 1.0, 1.0)

        val result = ProjectileUtil.raycast(viewer, position, max, searchBox, isVisible, reachDistance * reachDistance)

        if (result != null && result.entity is LivingEntity) {
            val target = result.entity as LivingEntity
            val blockHit: BlockHitResult? =
                raycast(setupRayTraceContext(client.player as PlayerEntity, reachDistance, FluidHandling.NONE))
            if (blockHit!!.type != HitResult.Type.MISS) {
                val blockDistance = blockHit.pos.distanceTo(position)
                if (blockDistance > target.distanceTo(client.player)) {
                    return target
                }
            } else {
                return target
            }
        }

        return null

    }

    override fun getHeight(): Int {
        return 0
    }

    override fun getBottomY(): Int {
        return 0
    }

    override fun getBlockEntity(pos: BlockPos): BlockEntity? {
        return client.world!!.getBlockEntity(pos)
    }

    override fun getBlockState(pos: BlockPos?): BlockState {
        return client.world!!.getBlockState(pos)
    }

    override fun getFluidState(pos: BlockPos?): FluidState {
        return client.world!!.getFluidState(pos)
    }

}