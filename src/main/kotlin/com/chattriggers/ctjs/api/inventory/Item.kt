package com.chattriggers.ctjs.api.inventory

import com.chattriggers.ctjs.api.CTWrapper
import com.chattriggers.ctjs.api.client.Client
import com.chattriggers.ctjs.api.client.Player
import com.chattriggers.ctjs.api.entity.Entity
import com.chattriggers.ctjs.api.message.TextComponent
import com.chattriggers.ctjs.api.inventory.nbt.NBTTagCompound
import com.chattriggers.ctjs.api.render.Renderer
import com.chattriggers.ctjs.api.world.World
import com.chattriggers.ctjs.api.world.block.Block
import com.chattriggers.ctjs.api.world.block.BlockPos
import com.chattriggers.ctjs.api.world.block.BlockType
import com.chattriggers.ctjs.internal.Skippable
import com.chattriggers.ctjs.internal.TooltipOverridable
import com.chattriggers.ctjs.internal.mixins.GameRendererAccessor
import com.chattriggers.ctjs.internal.utils.asMixin
import net.minecraft.world.level.block.state.pattern.BlockInWorld
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.core.component.DataComponents
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.Item.TooltipContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.ReportedException
import net.minecraft.CrashReport
import kotlin.jvm.optionals.getOrNull

class Item(override val mcValue: ItemStack) : CTWrapper<ItemStack> {
    val type: ItemType = ItemType(mcValue.item)

    init {
        require(!mcValue.isEmpty) {
            "Can not wrap empty ItemStack as an Item"
        }
    }

    constructor(type: ItemType) : this(type.toMC().defaultInstance)

    constructor(itemName: String) : this(ItemType(itemName))

    constructor(itemId: Int) : this(ItemType(itemId))

    constructor(blockType: BlockType) : this(ItemType(blockType))

    constructor(entity: Entity) : this(
        (entity.toMC() as? ItemEntity)?.item
            ?: throw IllegalArgumentException("Entity is not a dropped item"),
    )

//    fun getHolder(): Entity? = mcValue.entityRepresentation?.let(Entity::fromMC)

    fun getStackSize(): Int = mcValue.count

    fun setStackSize(size: Int) = apply {
        mcValue.count = size
    }

    fun getEnchantments() = EnchantmentHelper.getEnchantmentsForCrafting(mcValue).keySet().associate {
        it.unwrapKey().getOrNull() to EnchantmentHelper.getItemEnchantmentLevel(it, mcValue)
    }

    fun isEnchantable() = mcValue.isEnchantable

    fun isEnchanted() = mcValue.isEnchanted

    fun canPlaceOn(pos: BlockPos) =
        World.toMC()?.let { mcValue.canPlaceOnBlockInAdventureMode(BlockInWorld(it, pos.toMC(), false)) } ?: false

    fun canPlaceOn(block: Block) = canPlaceOn(block.pos)

    fun canHarvest(pos: BlockPos) =
        World.toMC()?.let { mcValue.canBreakBlockInAdventureMode(BlockInWorld(it, pos.toMC(), false)) } ?: false

    fun canHarvest(block: Block) = canHarvest(block.pos)

    /** Legacy adventure-mode block predicate name. */
    fun canDestroy(block: Block) = canHarvest(block)

    fun getDurability() = getMaxDamage() - getDamage()

    fun getMaxDamage() = mcValue.maxDamage

    fun getDamage() = mcValue.damageValue

    fun setDamage(damage: Int) = apply {
        mcValue.damageValue = damage
    }

    /** Legacy numeric item identifier. Prefer Item.type.getRegistryName(). */
    fun getID(): Int = type.getId()

    /** Legacy 1.8 metadata maps to item damage for damageable stacks. */
    fun getMetadata(): Int = getDamage()

    fun getRegistryName(): String = type.getRegistryName()

    fun getUnlocalizedName(): String = type.getTranslationKey()

    fun getTextComponent(): TextComponent = TextComponent(mcValue.hoverName)

    fun isDamageable() = mcValue.isDamageableItem

    /** Legacy misspelling retained for 1.8.9 modules. */
    fun isDamagable() = isDamageable()

    fun getName(): String = TextComponent(mcValue.hoverName).formattedText

    fun setName(name: TextComponent?) = apply {
        mcValue.set(DataComponents.CUSTOM_NAME, name)
    }

    fun setName(name: String) = setName(TextComponent(name))

    fun resetName() {
        setName(null)
    }

    @JvmOverloads
    fun getLore(advanced: Boolean = false): List<String> =
        getLoreComponents(advanced).map(TextComponent::formattedText)

    @JvmOverloads
    fun getLoreComponents(advanced: Boolean = false): List<TextComponent> {
        mcValue.asMixin<Skippable>().ctjs_setShouldSkip(true)
        val tooltip = mcValue.getTooltipLines(
            TooltipContext.EMPTY,
            Player.toMC(),
            if (advanced) TooltipFlag.ADVANCED else TooltipFlag.NORMAL,
        ).mapTo(mutableListOf()) { TextComponent(it) }

        mcValue.asMixin<Skippable>().ctjs_setShouldSkip(false)

        return tooltip
    }

    fun setLore(lore: List<TextComponent>) {
        mcValue.asMixin<TooltipOverridable>().apply {
            ctjs_setTooltip(lore)
            ctjs_setShouldOverrideTooltip(true)
        }
    }

    fun setLore(vararg loreLines: String) = setLore(loreLines.map { TextComponent(it) })

    fun resetLore() {
        mcValue.asMixin<TooltipOverridable>().ctjs_setShouldOverrideTooltip(false)
    }

    /** Returns the complete encoded item stack using the current data-component format. */
    fun getNBT(): NBTTagCompound = NBTTagCompound(serializeToNbt())

    /** Legacy alias retained for 1.8.9 modules. */
    fun getItemNBT(): NBTTagCompound = getNBT()

    /** Returns the encoded item stack as SNBT text. */
    fun getRawNBT(): String = serializeToNbt().toString()

    /** Direct access to the modern item data-component map. */
    fun getComponents() = mcValue.components

    private fun serializeToNbt(): CompoundTag {
        val registries = World.toMC()?.registryAccess()
            ?: RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)
        val ops = RegistryOps.create(NbtOps.INSTANCE, registries)
        return ItemStack.CODEC.encodeStart(ops, mcValue).getOrThrow() as CompoundTag
    }

    /**
     * Renders the item icon to the client's overlay, with customizable overlay information.
     *
     * @param x the x location
     * @param y the y location
     * @param scale the scale
     * @param z the z level to draw the item at
     */
    @JvmOverloads
    fun draw(x: Float = 0f, y: Float = 0f, scale: Float = 1f, z: Float = 200f) {
        val itemRenderer = Client.getMinecraft().gameRenderer.itemInHandRenderer
        val itemRenderState = ItemStackRenderState()

        Renderer.pushMatrix()
            .scale(scale, scale, 1f)
            .translate(x / scale, y / scale, z)

        // The item draw method moved to DrawContext in 1.20, which we don't have access
        // to here, so its drawItem method has been copy-pasted here instead
        if (mcValue.isEmpty)
            return
        Client.getMinecraft().itemModelResolver.updateForTopItem(itemRenderState, mcValue, ItemDisplayContext.GUI, World.toMC(), null, 0)
        Renderer.pushMatrix()
        Renderer.translate(x + 8, y + 8, 150 + z)
        try {
            val orderedRender = (Client.getMinecraft().gameRenderer as GameRendererAccessor).submitNodeStorage
            // TODO: surely this wont be needed anymore
//            val vertexConsumers = Client.getMinecraft().gameRenderer.renderBuffers()
            Renderer.scale(16.0f, -16.0f, 16.0f)
            if (!itemRenderState.usesBlockLight())
//                vertexConsumers.endBatch()
                // TODO: find out a way to get Diffuse instance and call setType
                // DiffuseLighting.disableGuiDepthLighting()

            itemRenderState.submit(Renderer.matrixStack.toMC(), orderedRender, 15728880, OverlayTexture.NO_OVERLAY, 0)

            Renderer.disableDepth()
//            vertexConsumers.endBatch()
            Renderer.enableDepth()

            if (!itemRenderState.usesBlockLight()) {
                // TODO: find out a way to get Diffuse instance and call setType
                // DiffuseLighting.enableGuiDepthLighting()
            }
        } catch (e: Throwable) {
            val crashReport = CrashReport.forThrowable(e, "Rendering item")
            val crashReportSection = crashReport.addCategory("Item being rendered")
            crashReportSection.setDetail("Item Type") { mcValue.item.toString() }
            crashReportSection.setDetail("Item Damage") { mcValue.damageValue.toString() }
            crashReportSection.setDetail("Item Components") { mcValue.components.toString() }
            crashReportSection.setDetail("Item Foil") { mcValue.hasFoil().toString() }
            throw ReportedException(crashReport)
        } finally {
            Renderer.popMatrix()
            Renderer.popMatrix()
        }
    }

    override fun toString(): String = "Item{name=${getName()}, type=${type.getRegistryName()}, size=${getStackSize()}}"

    companion object {
        @JvmStatic
        fun fromMC(mcValue: ItemStack): Item? {
            return if (mcValue.isEmpty) {
                null
            } else {
                Item(mcValue)
            }
        }
    }
}
