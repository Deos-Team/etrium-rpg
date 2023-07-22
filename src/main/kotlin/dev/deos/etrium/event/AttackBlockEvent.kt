package dev.deos.etrium.event

import dev.deos.etrium.utils.EnergyContainer
import dev.deos.etrium.utils.EnergyRequired
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World


class AttackBlockEvent: AttackBlockCallback {
    override fun interact(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        pos: BlockPos,
        direction: Direction
    ): ActionResult {
        if (!world.isClient() && !player.isSpectator) {
            val playerEssence: EnergyContainer = player as EnergyContainer
            return if (playerEssence.getEnergy() != 0F) {
                playerEssence.setEnergy(playerEssence.getEnergy() - EnergyRequired.blockBreaking.value)
                player.sendMessage(Text.literal("${playerEssence.getEnergy()} energy"), true)
                ActionResult.PASS
            } else {
                player.sendMessage(
                    Text.literal(
                        "Don't enough energy. " +
                                "Required's ${EnergyRequired.blockBreaking.value}. You have ${player.getEnergy()} "
                    ),
                    true
                )
                ActionResult.FAIL
            }
        }
        return ActionResult.PASS
    }
}