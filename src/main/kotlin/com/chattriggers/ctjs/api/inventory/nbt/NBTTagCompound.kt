package com.chattriggers.ctjs.api.inventory.nbt

import com.chattriggers.ctjs.internal.mixins.CompoundTagAccessor
import com.chattriggers.ctjs.MCNbtBase
import com.chattriggers.ctjs.MCNbtCompound
import com.chattriggers.ctjs.internal.utils.asMixin
import net.minecraft.nbt.ByteArrayTag
import net.minecraft.nbt.IntArrayTag
import net.minecraft.nbt.LongArrayTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import org.mozilla.javascript.NativeObject

class NBTTagCompound(override val mcValue: MCNbtCompound) : NBTBase(mcValue) {
    val tagMap: Map<String, MCNbtBase>
        get() = mcValue.asMixin<CompoundTagAccessor>().tags

    val keySet: Set<String>
        get() = mcValue.keySet()

    enum class NBTDataType {
        BYTE,
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        STRING,
        BYTE_ARRAY,
        INT_ARRAY,
        LONG_ARRAY,
        BOOLEAN,
        COMPOUND_TAG,
        TAG_LIST,
    }

    fun getTag(key: String): NBTBase? = mcValue.get(key)?.let(::fromMC)

    fun getTagId(key: String): Byte = mcValue.get(key)?.id ?: 0

    fun getByte(key: String): Byte = mcValue.getByte(key).orElse(0)

    fun getShort(key: String): Short = mcValue.getShort(key).orElse(0)

    fun getInteger(key: String): Int = mcValue.getInt(key).orElse(0)

    fun getLong(key: String): Long = mcValue.getLong(key).orElse(0L)

    fun getFloat(key: String): Float = mcValue.getFloat(key).orElse(0f)

    fun getDouble(key: String): Double = mcValue.getDouble(key).orElse(0.0)

    fun getString(key: String): String = mcValue.getString(key).orElse("")

    fun getByteArray(key: String): ByteArray = mcValue.getByteArray(key).orElseGet { ByteArray(0) }

    fun getIntArray(key: String): IntArray = mcValue.getIntArray(key).orElseGet { IntArray(0) }

    fun getBoolean(key: String): Boolean = mcValue.getBoolean(key).orElse(false)

    fun getCompoundTag(key: String) = NBTTagCompound(mcValue.getCompound(key).orElseGet(::MCNbtCompound))

    fun getTagList(key: String) = NBTTagList(mcValue.getList(key).orElseGet(::ListTag))

    fun getTagList(key: String, type: Int): NBTTagList {
        val list = mcValue.getList(key).orElseGet(::ListTag)
        return if (list.isEmpty() || list[0].id.toInt() == type) NBTTagList(list) else NBTTagList(ListTag())
    }

    fun get(key: String, type: NBTDataType, tagType: Int?): Any? {
        return when (type) {
            NBTDataType.BYTE -> getByte(key)
            NBTDataType.SHORT -> getShort(key)
            NBTDataType.INTEGER -> getInteger(key)
            NBTDataType.LONG -> getLong(key)
            NBTDataType.FLOAT -> getFloat(key)
            NBTDataType.DOUBLE -> getDouble(key)
            NBTDataType.STRING -> (mcValue.get(key) as? StringTag)?.asString()
            NBTDataType.BYTE_ARRAY -> (mcValue.get(key) as? ByteArrayTag)?.asByteArray
            NBTDataType.INT_ARRAY -> (mcValue.get(key) as? IntArrayTag)?.asIntArray
            NBTDataType.LONG_ARRAY -> (mcValue.get(key) as? LongArrayTag)?.asLongArray
            NBTDataType.BOOLEAN -> getBoolean(key)
            NBTDataType.COMPOUND_TAG -> getCompoundTag(key)
            NBTDataType.TAG_LIST -> getTagList(
                key,
                tagType ?: throw IllegalArgumentException("For accessing a tag list you need to provide the tagType argument"),
            )
        }
    }

    operator fun get(key: String): NBTBase? = getTag(key)

    fun setNBTBase(key: String, value: NBTBase) = setNBTBase(key, value.toMC())

    fun setNBTBase(key: String, value: MCNbtBase) = apply {
        mcValue.put(key, value)
    }

    fun setBoolean(key: String, value: Boolean) = apply {
        mcValue.putBoolean(key, value)
    }

    fun setByte(key: String, value: Byte) = apply {
        mcValue.putByte(key, value)
    }

    fun setShort(key: String, value: Short) = apply {
        mcValue.putShort(key, value)
    }

    fun setInteger(key: String, value: Int) = apply {
        mcValue.putInt(key, value)
    }

    fun setLong(key: String, value: Long) = apply {
        mcValue.putLong(key, value)
    }

    fun setFloat(key: String, value: Float) = apply {
        mcValue.putFloat(key, value)
    }

    fun setDouble(key: String, value: Double) = apply {
        mcValue.putDouble(key, value)
    }

    fun setString(key: String, value: String) = apply {
        mcValue.putString(key, value)
    }

    fun setByteArray(key: String, value: ByteArray) = apply {
        mcValue.putByteArray(key, value)
    }

    fun setIntArray(key: String, value: IntArray) = apply {
        mcValue.putIntArray(key, value)
    }

    fun putLongArray(key: String, value: LongArray) = apply {
        mcValue.putLongArray(key, value)
    }

    fun setLongArray(key: String, value: LongArray) = putLongArray(key, value)

    operator fun set(key: String, value: Any) = apply {
        when (value) {
            is NBTBase -> setNBTBase(key, value)
            is MCNbtBase -> setNBTBase(key, value)
            is Byte -> setByte(key, value)
            is Short -> setShort(key, value)
            is Int -> setInteger(key, value)
            is Long -> setLong(key, value)
            is Float -> setFloat(key, value)
            is Double -> setDouble(key, value)
            is CharSequence -> setString(key, value.toString())
            is ByteArray -> setByteArray(key, value)
            is IntArray -> setIntArray(key, value)
            is LongArray -> setLongArray(key, value)
            is Boolean -> setBoolean(key, value)
            else -> throw IllegalArgumentException("Unsupported NBT type: ${value.javaClass.simpleName}")
        }
    }

    fun removeTag(key: String) = apply {
        mcValue.remove(key)
    }

    fun toObject(): NativeObject = mcValue.toObject()
}
