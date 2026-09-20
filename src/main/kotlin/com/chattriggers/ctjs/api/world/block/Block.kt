package com.chattriggers.ctjs.api.world.block

import com.chattriggers.ctjs.api.client.Player
import com.chattriggers.ctjs.api.inventory.Item
import com.chattriggers.ctjs.api.world.World

/** A reference to a block type and its optional world position and face. */
open class Block(
    val type: BlockType,
    pos: BlockPos,
    face: BlockFace? = null,
) {
    var pos: BlockPos = pos
        private set
    private var currentFace: BlockFace? = face
    val face: BlockFace? get() = currentFace

    /** Legacy names for the wrapped block and its position. */
    val block get() = type.mcValue
    val blockPos get() = pos

    constructor(type: BlockType) : this(type, BlockPos(0, 0, 0))
    constructor(block: Block) : this(block.type, block.pos, block.face)
    constructor(blockName: String) : this(BlockType(blockName))
    constructor(blockID: Int) : this(BlockType(blockID))
    constructor(item: Item) : this(BlockType(item))

    val x: Int get() = pos.x
    val y: Int get() = pos.y
    val z: Int get() = pos.z

    fun withType(type: BlockType) = Block(type, pos, face)

    fun withPos(pos: BlockPos) = Block(type, pos, face)

    /** Legacy mutating position setter. */
    fun setBlockPos(blockPos: BlockPos) = apply {
        pos = blockPos
    }

    /**
     * Narrows this block to reference a certain face. Used by
     * [Player.lookingAt] to specify the block face
     * being looked at.
     */
    fun withFace(face: BlockFace) = Block(type, pos, face)

    /** Legacy mutating face setter. */
    fun setFace(face: BlockFace) = apply {
        currentFace = face
    }

    fun getID() = type.getID()

    fun getRegistryName() = type.getRegistryName()

    fun getUnlocalizedName() = type.getUnlocalizedName()

    fun getName() = type.getName()

    fun getLightValue() = getState()?.lightEmission ?: type.getLightValue()

    fun getDefaultState() = type.getDefaultState()

    fun getDefaultMetadata() = type.getDefaultMetadata()

    fun canProvidePower() = getState()?.isSignalSource ?: type.canProvidePower()

    fun isTranslucent() = getState()?.useShapeForLightOcclusion() ?: type.isTranslucent()

    fun getState() = World.toMC()?.getBlockState(pos.toMC())

    /**
     * Returns the placed state's stable index within this block type's state definition.
     * This is the modern equivalent of the 1.8.9 metadata value.
     */
    fun getMetadata(): Int {
        val state = getState() ?: return 0
        return type.mcValue.stateDefinition.possibleStates.indexOf(state).coerceAtLeast(0)
    }

    /** Legacy name for whether this position receives redstone power. */
    fun isPowered(): Boolean = isReceivingPower()

    /** Legacy name for the strongest neighboring redstone signal. */
    fun getRedstoneStrength(): Int = getReceivingPower()

    @JvmOverloads
    fun isEmittingPower(face: BlockFace? = null): Boolean {
        if (face != null)
            return World.toMC()!!.hasSignal(pos.toMC(), face.toMC())
        return BlockFace.entries.any { isEmittingPower(it) }
    }

    @JvmOverloads
    fun getEmittingPower(face: BlockFace? = null): Int {
        if (face != null)
            return World.toMC()!!.getSignal(pos.toMC(), face.toMC())
        return BlockFace.entries.asSequence().map(::getEmittingPower).firstOrNull { it != 0 } ?: 0
    }

    fun isReceivingPower() = World.toMC()!!.hasNeighborSignal(pos.toMC())

    fun getReceivingPower() = World.toMC()!!.getBestNeighborSignal(pos.toMC())

    /**
     * Checks whether the block can be mined with the tool in the player's hand
     *
     * @return whether the block can be mined
     */
    fun canBeHarvested(): Boolean = Player.getHeldItem()?.let(::canBeHarvestedWith) ?: false

    fun canBeHarvestedWith(item: Item): Boolean = item.canHarvest(this)

    override fun toString() = "Block{type=$type, pos=($x, $y, $z), face=$face}"
}
