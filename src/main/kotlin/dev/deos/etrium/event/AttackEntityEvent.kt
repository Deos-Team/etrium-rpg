package dev.deos.etrium.event

import dev.deos.etrium.utils.EtriumData
import dev.deos.etrium.utils.EtriumData.getEnergy
import dev.deos.etrium.utils.EnergyRequired
import dev.deos.etrium.utils.EnergyTypes.ENERGY
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
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
            return if (player.getEnergy() >= EnergyRequired.AttackEntity.value) {
                if (!player.isCreative) {
                    EtriumData.removeEnergy(player, EnergyRequired.AttackEntity.value, ENERGY)
                }
                ActionResult.PASS
            } else {
                player.sendMessage(
                    Text.literal("Don't enough energy"), true
                )
                ActionResult.FAIL
            }
        }
        return ActionResult.PASS
    }
}