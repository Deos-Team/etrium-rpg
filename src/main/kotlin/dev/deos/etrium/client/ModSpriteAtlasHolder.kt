package dev.deos.etrium.client

import net.minecraft.client.texture.Sprite
import net.minecraft.client.texture.SpriteAtlasHolder
import net.minecraft.client.texture.TextureManager
import net.minecraft.util.Identifier

class ModSpriteAtlasHolder(textureManager: TextureManager?, atlasId: Identifier?, sourcePath: Identifier?) :
    SpriteAtlasHolder(
        textureManager,
        atlasId, sourcePath
    ) {

    override fun getSprite(objectId: Identifier?): Sprite {
        return super.getSprite(objectId)
    }

}