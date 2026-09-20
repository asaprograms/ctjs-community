package com.chattriggers.ctjs.api.entity

import com.chattriggers.ctjs.api.CTWrapper
import com.chattriggers.ctjs.api.render.Renderer
import com.chattriggers.ctjs.internal.mixins.ParticleAccessor
import com.chattriggers.ctjs.internal.mixins.SingleQuadParticleAccessor
import com.chattriggers.ctjs.MCParticle
import com.chattriggers.ctjs.internal.utils.asMixin
import net.minecraft.client.particle.SingleQuadParticle
import java.awt.Color

class Particle(override val mcValue: MCParticle) : CTWrapper<MCParticle> {
    private val mixed: ParticleAccessor = mcValue.asMixin()
    private fun quad(): SingleQuadParticle = mcValue as? SingleQuadParticle
        ?: throw UnsupportedOperationException("Color is not available for ${mcValue.javaClass.simpleName} particles")

    private fun mixedQuad(): SingleQuadParticleAccessor = quad().asMixin()

    var x by mixed::x
    var y by mixed::y
    var z by mixed::z

    var lastX
        get() = mixed.xo
        set(value) = mixed.setXo(value)
    var lastY
        get() = mixed.yo
        set(value) = mixed.setYo(value)
    var lastZ
        get() = mixed.zo
        set(value) = mixed.setZo(value)

    val renderX get() = lastX + (x - lastX) * Renderer.partialTicks
    val renderY get() = lastY + (y - lastY) * Renderer.partialTicks
    val renderZ get() = lastZ + (z - lastZ) * Renderer.partialTicks

    var motionX
        get() = mixed.xd
        set(value) = mixed.setXd(value)
    var motionY
        get() = mixed.yd
        set(value) = mixed.setYd(value)
    var motionZ
        get() = mixed.zd
        set(value) = mixed.setZd(value)

    var age by mixed::age
    var dead
        get() = mixed.removed
        set(value) = mixed.setRemoved(value)

    fun scale(scale: Float) = apply {
        mcValue.scale(scale)
    }

    /** Multiplies all three particle velocity components. */
    fun multiplyVelocity(multiplier: Float) = apply {
        motionX *= multiplier
        motionY *= multiplier
        motionZ *= multiplier
    }

    /**
     * Sets the color of the particle.
     * @param red the red value between 0 and 1.
     * @param green the green value between 0 and 1.
     * @param blue the blue value between 0 and 1.
     */
    fun setColor(red: Float, green: Float, blue: Float) = apply {
        quad().setColor(red, green, blue)
    }

    /**
     * Sets the color of the particle.
     * @param red the red value between 0 and 1.
     * @param green the green value between 0 and 1.
     * @param blue the blue value between 0 and 1.
     * @param alpha the alpha value between 0 and 1.
     */
    fun setColor(red: Float, green: Float, blue: Float, alpha: Float) = apply {
        setColor(red, green, blue)
        setAlpha(alpha)
    }

    fun setColor(color: Long) = apply {
        val red = (color shr 16 and 255).toFloat() / 255.0f
        val green = (color shr 8 and 255).toFloat() / 255.0f
        val blue = (color and 255).toFloat() / 255.0f
        val alpha = (color shr 24 and 255).toFloat() / 255.0f

        setColor(red, green, blue, alpha)
    }

    /**
     * Sets the alpha of the particle.
     * @param alpha the alpha value between 0 and 1.
     */
    fun setAlpha(alpha: Float) = apply {
        mixedQuad().setAlpha(alpha)
    }

    /**
     * Returns the color of the Particle
     *
     * @return A [Color] with the R, G, B and A values
     */
    fun getColor(): Color = mixedQuad().let { Color(it.rCol, it.gCol, it.bCol, it.alpha) }

    fun setColor(color: Color) = setColor(color.rgb.toLong())

    /**
     * Sets the amount of ticks this particle will live for
     *
     * @param maxAge the particle's max age (in ticks)
     */
    fun setMaxAge(maxAge: Int) = apply {
        mcValue.setLifetime(maxAge)
    }

    fun remove() = apply {
        mcValue.remove()
    }

    override fun toString() =
        "Particle(type=${mcValue.javaClass.simpleName}, pos=($x, $y, $z), age=$age)"
}
