package dev.deos.etrium.item

import net.minecraft.item.Items
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient

enum class WeaponToolMaterials(
    val dur: Int,
    val at: Float,
    val ench: Int,
    val rep: Ingredient
) : ToolMaterial {
    OBTANIUM(900, 1f, 30, Ingredient.ofItems(Items.DIAMOND)),
    UNOBTANIUM(1400, 1f, 30, Ingredient.ofItems(Items.NETHERITE_INGOT));

    override fun getAttackDamage(): Float {
        return this.at
    }

    override fun getDurability(): Int {
        return this.dur
    }

    override fun getEnchantability(): Int {
        return this.ench
    }

    override fun getMiningLevel(): Int {
        return 0
    }

    override fun getMiningSpeedMultiplier(): Float {
        return 0f
    }

    override fun getRepairIngredient(): Ingredient {
        return this.rep
    }

}