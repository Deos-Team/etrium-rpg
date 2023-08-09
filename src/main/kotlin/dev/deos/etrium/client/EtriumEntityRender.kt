package dev.deos.etrium.client

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity

class EtriumEntityRender : LivingEntityFeatureRendererRegistrationCallback {
    override fun registerRenderers(
        entityType: EntityType<out LivingEntity>?,
        entityRenderer: LivingEntityRenderer<*, *>?,
        registrationHelper: LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper?,
        context: EntityRendererFactory.Context?
    ) {
        TODO("Not yet implemented")
    }

}