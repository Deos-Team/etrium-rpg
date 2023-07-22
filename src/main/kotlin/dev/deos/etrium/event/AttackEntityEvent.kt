package dev.deos.etrium.event

import dev.deos.etrium.utils.EnergyContainer
import dev.deos.etrium.utils.EnergyRequired
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
        player: PlayerEntity,
        world: World,
        hand: Hand,
        entity: Entity,
        hitResult: EntityHitResult?
    ): ActionResult {
        if (!world.isClient() && !player.isSpectator) {
            val playerEssence: EnergyContainer = player as EnergyContainer
            return if (playerEssence.getEnergy() != 0F) {
                playerEssence.setEnergy(playerEssence.getEnergy() - EnergyRequired.attackEntity.value)
                player.sendMessage(Text.literal("${playerEssence.getEnergy()} energy"), true)
                ActionResult.PASS
            } else {
                player.sendMessage(
                    Text.literal(
                        "Don't enough energy. " +
                                "Required's ${EnergyRequired.attackEntity.value}. You have ${player.getEnergy()} "
                    ),
                    true
                )
                ActionResult.FAIL
            }
        }
        return ActionResult.PASS
    }
}