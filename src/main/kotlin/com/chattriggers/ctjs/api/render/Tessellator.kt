package com.chattriggers.ctjs.api.render

import com.chattriggers.ctjs.api.vec.Vec3f

/**
 * Compatibility facade for the 1.8.9 global Tessellator API.
 *
 * Modern CTJS exposes the underlying world renderer as [Renderer3d]. This
 * facade retains the historical name and delegates supported state operations
 * to the current rendering pipeline.
 */
object Tessellator {
    @JvmStatic
    var partialTicks: Float
        get() = Renderer.partialTicks
        set(value) {
            Renderer.partialTicks = value
        }

    /** Alpha testing was removed from Minecraft's modern render pipeline. */
    @JvmStatic fun disableAlpha() = apply {}
    @JvmStatic fun enableAlpha() = apply {}
    @JvmStatic fun alphaFunc(func: Int, ref: Float) = apply {}

    @JvmStatic fun enableLighting() = apply { Renderer.enableLighting() }
    @JvmStatic fun disableLighting() = apply { Renderer.disableLighting() }
    @JvmStatic fun disableDepth() = apply { Renderer.disableDepth() }
    @JvmStatic fun enableDepth() = apply { Renderer.enableDepth() }
    @JvmStatic fun depthFunc(depthFunc: Int) = apply { Renderer.depthFunc(depthFunc) }
    @JvmStatic fun depthMask(flagIn: Boolean) = apply { Renderer.depthMask(flagIn) }
    @JvmStatic fun disableBlend() = apply { Renderer.disableBlend() }
    @JvmStatic fun enableBlend() = apply { Renderer.enableBlend() }
    @JvmStatic fun blendFunc(sourceFactor: Int, destFactor: Int) = apply {
        Renderer.tryBlendFuncSeparate(sourceFactor, destFactor, 1, 0)
    }
    @JvmStatic fun tryBlendFuncSeparate(sourceFactor: Int, destFactor: Int, sourceFactorAlpha: Int, destFactorAlpha: Int) = apply {
        Renderer.tryBlendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha)
    }

    /** Fixed-function texture enablement no longer exists on modern Minecraft. */
    @JvmStatic fun enableTexture2D() = apply {}
    @JvmStatic fun disableTexture2D() = apply {}
    @JvmStatic fun bindTexture(texture: Image) = apply { Renderer.bindTexture(texture) }
    @JvmStatic fun deleteTexture(texture: Image) = apply { Renderer.deleteTexture(texture) }
    @JvmStatic fun pushMatrix() = apply { Renderer.pushMatrix() }
    @JvmStatic fun popMatrix() = apply { Renderer.popMatrix() }

    @JvmStatic
    @JvmOverloads
    fun begin(drawMode: Int = 7, textured: Boolean = true) = apply {
        Renderer3d.begin(
            Renderer.DrawMode.fromLegacy(drawMode),
            if (textured) Renderer.VertexFormat.POSITION_TEXTURE else Renderer.VertexFormat.POSITION,
        )
    }

    @JvmStatic
    @JvmOverloads
    fun colorize(red: Float, green: Float, blue: Float, alpha: Float = 1f) = apply {
        Renderer.colorize(red, green, blue, alpha)
    }

    @JvmStatic fun rotate(angle: Float, x: Float, y: Float, z: Float) = apply { Renderer.rotate(angle, x, y, z) }
    @JvmStatic fun translate(x: Float, y: Float, z: Float) = apply { Renderer.translate(x, y, z) }

    @JvmStatic
    @JvmOverloads
    fun scale(x: Float, y: Float = x, z: Float = x) = apply { Renderer.scale(x, y, z) }

    @JvmStatic fun pos(x: Float, y: Float, z: Float) = apply { Renderer3d.pos(x, y, z) }
    @JvmStatic fun tex(u: Float, v: Float) = apply { Renderer3d.tex(u, v) }
    @JvmStatic fun draw() = Renderer3d.draw()
    @JvmStatic fun getRenderPos(x: Float, y: Float, z: Float): Vec3f = Renderer.getRenderPos(x, y, z)

    @JvmStatic
    @JvmOverloads
    fun drawString(
        text: String,
        x: Float,
        y: Float,
        z: Float,
        color: Int = -1,
        renderBlackBox: Boolean = true,
        scale: Float = 1f,
        increase: Boolean = true,
    ) = Renderer3d.drawString(
        text,
        x,
        y,
        z,
        color.toLong() and 0xffffffffL,
        renderBlackBox,
        scale,
        increase,
        centered = true,
        renderThroughBlocks = true,
    )
}
