package dev.deos.etrium.item

import dev.deos.etrium.utils.EnergyTypes.LEVEL
import dev.deos.etrium.utils.IEntityDataSaver
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand

class LevelGetter(settings: Settings?) : Item(settings) {
    override fun useOnEntity(stack: ItemStack, user: PlayerEntity, entity: LivingEntity, hand: Hand): ActionResult {
        return if (!entity.isPlayer && !user.world.isClient) {
            val nbt = (entity as IEntityDataSaver).getPersistentData()
            val level = nbt.getInt(LEVEL)
            user.sendMessage(Text.literal("Level: $level"))
            ActionResult.SUCCESS
        } else ActionResult.FAIL
    }
}