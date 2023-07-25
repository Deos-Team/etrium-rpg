package dev.deos.etrium.event

import dev.deos.etrium.utils.EnergyData
import dev.deos.etrium.utils.EnergyData.getEnergy
import dev.deos.etrium.utils.EnergyData.getMaxEnergy
import dev.deos.etrium.utils.EnergyRequired
import dev.deos.etrium.utils.EnergyTypes.ENERGY
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
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
            player as ServerPlayerEntity
            return if (player.getEnergy() != EnergyRequired.BlockBreaking.value) {
                EnergyData.removeEnergy(player, EnergyRequired.BlockBreaking.value, ENERGY)
                player.sendMessage(Text.literal("Energy ${player.getEnergy()}/${player.getMaxEnergy()}"), true)
                ActionResult.PASS
            } else {
                player.sendMessage(
                    Text.literal(
                        "Don't enough energy. " +
                                "Required's ${EnergyRequired.BlockBreaking.value}. You have ${player.getEnergy()} "
                    ),
                    true
                )
                ActionResult.FAIL
            }
        }
        return ActionResult.PASS
    }
}