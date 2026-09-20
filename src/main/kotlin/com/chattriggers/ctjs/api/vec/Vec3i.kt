package com.chattriggers.ctjs.api.vec

import java.util.*
import kotlin.math.acos
import kotlin.math.sqrt

open class Vec3i @JvmOverloads constructor(
    val x: Int = 0, val y: Int = 0, val z: Int = 0,
) : Comparable<Vec3i> {
    constructor(x: Number, y: Number, z: Number) : this(x.toInt(), y.toInt(), z.toInt())

    fun magnitudeSquared() = x * x + y * y + z * z

    fun magnitude() = sqrt(magnitudeSquared().toFloat())

    open fun translated(dx: Int, dy: Int, dz: Int) = Vec3i(x + dx, y + dy, z + dz)

    open fun scaled(scale: Int) = Vec3i(x * scale, y * scale, z * scale)

    open fun scaled(xScale: Int, yScale: Int, zScale: Int) = Vec3i(x * xScale, y * yScale, z * zScale)

    open fun crossProduct(other: Vec3i) = Vec3i(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x,
    )

    fun distanceSq(other: Vec3i): Double {
        val dx = (x - other.x).toDouble()
        val dy = (y - other.y).toDouble()
        val dz = (z - other.z).toDouble()
        return dx * dx + dy * dy + dz * dz
    }

    fun distance(other: Vec3i) = sqrt(distanceSq(other))

    fun distanceSqToCenter(x: Double, y: Double, z: Double): Double {
        val dx = this.x + 0.5 - x
        val dy = this.y + 0.5 - y
        val dz = this.z + 0.5 - z
        return dx * dx + dy * dy + dz * dz
    }

    fun dotProduct(other: Vec3i) = x * other.x + y * other.y + z * other.z

    fun angleTo(other: Vec3i): Float {
        return acos(dotProduct(other) / (magnitude() * other.magnitude()).coerceIn(-1f, 1f))
    }

    fun normalized() = magnitude().let {
        Vec3f(x / it, y / it, z / it)
    }

    open operator fun unaryMinus() = Vec3i(-x, -y, -z)

    open operator fun plus(other: Vec3i) = Vec3i(x + other.x, y + other.y, z + other.z)

    open operator fun minus(other: Vec3i) = this + (-other)

    override fun compareTo(other: Vec3i): Int = compareValuesBy(this, other, Vec3i::y, Vec3i::z, Vec3i::x)

    override fun hashCode() = Objects.hash(x, y, z)

    override fun equals(other: Any?) = other is Vec3i && x == other.x && y == other.y && z == other.z

    override fun toString() = "Vec3i($x, $y, $z)"
}
