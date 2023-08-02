package dev.deos.etrium.item

import dev.deos.etrium.Etrium
import net.minecraft.block.Blocks
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.damage.DamageTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.item.SwordItem
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.world.World

class EtriumSwordItem(
    toolMaterial: WeaponToolMaterials, attackDamage: Int, attackSpeed: Float, settings: Settings?
) :
    SwordItem(toolMaterial, attackDamage, attackSpeed, settings) {

    override fun useOnEntity(stack: ItemStack, user: PlayerEntity, entity: LivingEntity, hand: Hand): ActionResult {
        /*val fire = stack.nbt?.getInt("etrium.fire_damage")
        if (fire != null) {
            if (fire > 0) {
                entity.setOnFireFor(fire * 4)
            }
        }*/
        val damageSources = entity.world.damageSources
        entity.damage(damageSources.create(DamageTypes.ON_FIRE, user), 2f)
//        entity.damage(damageSources.playerAttack(user), 1f)
        return super.useOnEntity(stack, user, entity, hand)
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val pos = context.blockPos
        if (world.getBlockState(pos).isOf(Blocks.NETHERITE_BLOCK)) {
            onUse(context.stack)
        }
        return super.useOnBlock(context)
    }

    private fun onUse(stack: ItemStack) {
        val nbtData = NbtCompound()
        val fireDamage = stack.nbt?.getInt("etrium.fire_damage") ?: 0
        nbtData.putInt("etrium.fire_damage", fireDamage + 4)
        if (stack.hasNbt()) {
            stack.nbt!!.putInt("etrium.fire_damage", fireDamage + 4)
        } else stack.nbt = nbtData
    }

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        if (stack.hasNbt()) {
            val fireDamage = stack.nbt?.getInt("etrium.fire_damage").toString()
            tooltip.add(Text.literal(fireDamage))
        }
        super.appendTooltip(stack, world, tooltip, context)
    }

}