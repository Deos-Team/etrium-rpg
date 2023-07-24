package dev.deos.etrium.event

import dev.deos.etrium.utils.EnergyData
import dev.deos.etrium.utils.IEntityDataSaver
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
            val nbt = player as IEntityDataSaver
            return if (EnergyData.getEnergy(nbt) != EnergyRequired.blockBreaking.value) {
                EnergyData.removeEnergy(nbt, EnergyRequired.blockBreaking.value)
                player.sendMessage(Text.literal("${EnergyData.getEnergy(nbt)} energy"), true)
                ActionResult.PASS
            } else {
                player.sendMessage(
                    Text.literal(
                        "Don't enough energy. " +
                                "Required's ${EnergyRequired.blockBreaking.value}. You have ${EnergyData.getEnergy(nbt)} "
                    ),
                    true
                )
                ActionResult.FAIL
            }
        }
        return ActionResult.PASS
    }
}