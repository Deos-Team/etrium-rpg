package dev.deos.etrium.event

import dev.deos.etrium.utils.EnergyContainer
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
        player: PlayerEntity?,
        world: World?,
        hand: Hand?,
        pos: BlockPos?,
        direction: Direction?
    ): ActionResult {
        if (!world!!.isClient() && !player!!.isSpectator) {
            val playerEssence: EnergyContainer = player as EnergyContainer
            return if (playerEssence.getEnergy() != 0F) {
                playerEssence.setEnergy(playerEssence.getEnergy() - 1.0f)
                player.sendMessage(Text.literal("${playerEssence.getEnergy()} energy"))
                ActionResult.PASS
            } else ActionResult.FAIL
        }
        return ActionResult.PASS
    }
}