package dev.deos.etrium.event

import dev.deos.etrium.utils.EnergyContainer
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World


class AttackEntityEvent: AttackEntityCallback {
    override fun interact(
        player: PlayerEntity?,
        world: World?,
        hand: Hand?,
        entity: Entity?,
        hitResult: EntityHitResult?
    ): ActionResult {
        val playerEssence: EnergyContainer = player as EnergyContainer
        if (!world!!.isClient() && !player.isSpectator) {
            playerEssence.setEnergy(playerEssence.getEnergy() + 1.0f)
            player.sendMessage(Text.literal("${playerEssence.getEnergy()} energy got"))
        }

        return ActionResult.PASS
    }
}