package dev.deos.etrium.event

import dev.deos.etrium.utils.EnergyData
import dev.deos.etrium.utils.EnergyData.getEnergy
import dev.deos.etrium.utils.EnergyData.getMaxEnergy
import dev.deos.etrium.utils.EnergyRequired
import dev.deos.etrium.utils.IEntityDataSaver
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
            val nbt = player as IEntityDataSaver
            return if (nbt.getEnergy() >= EnergyRequired.attackEntity.value) {
                EnergyData.removeEnergy(nbt, EnergyRequired.attackEntity.value)
                player.sendMessage(Text.literal("Energy ${nbt.getEnergy()}/${nbt.getMaxEnergy()}"), true)
                ActionResult.PASS
            } else {
                player.sendMessage(
                    Text.literal(
                        "Don't enough energy. " +
                                "Required's ${EnergyRequired.attackEntity.value}. You have ${nbt.getEnergy()} "
                    ),
                    true
                )
                ActionResult.FAIL
            }
        }
        return ActionResult.PASS
    }
}