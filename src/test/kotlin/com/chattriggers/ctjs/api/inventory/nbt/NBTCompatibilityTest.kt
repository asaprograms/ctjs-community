package com.chattriggers.ctjs.api.inventory.nbt

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class NBTCompatibilityTest {
    @Test
    fun `compound getters expose legacy values instead of optionals`() {
        val nbt = NBTTagCompound(CompoundTag())
            .setByte("byte", 2)
            .setShort("short", 3)
            .setInteger("int", 4)
            .setLong("long", 5)
            .setFloat("float", 6.5f)
            .setDouble("double", 7.5)
            .setString("string", "value")
            .setByteArray("bytes", byteArrayOf(8, 9))
            .setIntArray("ints", intArrayOf(10, 11))
            .setLongArray("longs", longArrayOf(12, 13))
            .setBoolean("boolean", true)

        assertEquals(2.toByte(), nbt.getByte("byte"))
        assertEquals(3.toShort(), nbt.getShort("short"))
        assertEquals(4, nbt.getInteger("int"))
        assertEquals(5L, nbt.getLong("long"))
        assertEquals(6.5f, nbt.getFloat("float"))
        assertEquals(7.5, nbt.getDouble("double"))
        assertEquals("value", nbt.getString("string"))
        assertContentEquals(byteArrayOf(8, 9), nbt.getByteArray("bytes"))
        assertContentEquals(intArrayOf(10, 11), nbt.getIntArray("ints"))
        assertContentEquals(longArrayOf(12, 13), nbt.get("longs", NBTTagCompound.NBTDataType.LONG_ARRAY, null) as LongArray)
        assertEquals(true, nbt.getBoolean("boolean"))
        assertEquals(0.toByte(), nbt.getTagId("missing"))
        assertEquals("", nbt.getString("missing"))
    }

    @Test
    fun `typed legacy list access preserves element validation`() {
        val strings = ListTag().apply {
            add(StringTag.valueOf("first"))
        }
        val nbt = NBTTagCompound(CompoundTag()).setNBTBase("values", strings)

        assertEquals("first", nbt.getTagList("values", Tag.TAG_STRING.toInt()).getStringTagAt(0))
        assertEquals(0, nbt.getTagList("values", Tag.TAG_INT.toInt()).tagCount)
        assertFailsWith<IllegalArgumentException> {
            nbt.get("values", NBTTagCompound.NBTDataType.TAG_LIST, null)
        }

        val list = NBTTagList(ListTag()).appendTag(IntTag.valueOf(14))
        assertEquals(14, list.get(0, NBTTagCompound.NBTDataType.INTEGER).let { (it as IntTag).intValue() })
    }
}
