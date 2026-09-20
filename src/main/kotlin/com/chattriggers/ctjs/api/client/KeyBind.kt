package com.chattriggers.ctjs.api.client

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.api.triggers.RegularTrigger
import com.chattriggers.ctjs.api.triggers.TriggerType
import com.chattriggers.ctjs.api.world.World
import com.chattriggers.ctjs.internal.BoundKeyUpdater
import com.chattriggers.ctjs.internal.mixins.OptionsAccessor
import com.chattriggers.ctjs.internal.mixins.KeyMappingAccessor
import com.chattriggers.ctjs.internal.mixins.KeyMappingCategoryAccessor
import com.chattriggers.ctjs.internal.utils.Initializer
import com.chattriggers.ctjs.internal.utils.asMixin
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.KeyMapping
import net.minecraft.client.resources.language.I18n
import net.minecraft.resources.Identifier
import org.apache.commons.lang3.ArrayUtils
import java.util.concurrent.CopyOnWriteArrayList

class KeyBind {
    private val keyBinding: KeyMapping
    private val categoryName: String
    private var onKeyPress: RegularTrigger? = null
    private var onKeyRelease: RegularTrigger? = null
    private var onKeyDown: RegularTrigger? = null

    private var down: Boolean = false

    /**
     * Creates a new keybind, editable in the user's controls.
     *
     * @param description what the keybind does
     * @param keyCode the keycode which the keybind will respond to, see Keyboard below. Ex. Keyboard.KEY_A
     * @param category the keybind category the keybind will be in
     * @see [org.lwjgl.input.Keyboard](http://legacy.lwjgl.org/javadoc/org/lwjgl/input/Keyboard.html)
     */
    @JvmOverloads
    constructor(description: String, keyCode: Int, category: String = "ChatTriggers") {
        val categoryId = categoryIdentifier(category)
        val possibleDuplicate = Client.getMinecraft().options.keyMappings.find {
            I18n.get(it.saveString()) == I18n.get(description) &&
                it.category.id == categoryId
        }

        if (possibleDuplicate != null) {
            require(possibleDuplicate in customKeyBindings) {
                "KeyBind already exists! To get a KeyBind from an existing Minecraft KeyBinding, " +
                    "use the other KeyBind constructor or Client.getKeyBindFromKey."
            }
            keyBinding = possibleDuplicate
            categoryName = category
        } else {
            val categoryList = KeyMappingCategoryAccessor.getCategoryList()

            if (categoryList.none { it.id == categoryId }) {
                uniqueCategories[category] = 0
            }
            val keyCategory = KeyMapping.Category.register(categoryId)
            uniqueCategories[category] = uniqueCategories.getOrDefault(category, 0) + 1
            keyBinding = KeyMapping(description, keyCode, keyCategory)
            categoryName = category

            // We need to update the bound key for the KeyBind we just made to the previous binding,
            // just in case it existed last time the game was opened. This will only matter for the first
            // time launching the game, as subsequent CT loads will cause possibleDuplicate to be found.
            Client.getMinecraft().options.asMixin<BoundKeyUpdater>().ctjs_updateBoundKey(keyBinding)
            KeyMapping.resetMapping()

            addKeyBinding(keyBinding)
            customKeyBindings.add(keyBinding)
        }

        keyBinds.add(this)
    }

    constructor(keyBinding: KeyMapping) {
        this.keyBinding = keyBinding
        categoryName = getCategoryName(keyBinding.category)
        keyBinds.add(this)
    }

    fun registerKeyPress(method: Any): RegularTrigger {
        unregisterKeyPress()
        return RegularTrigger(method, TriggerType.OTHER).also { onKeyPress = it }
    }

    fun registerKeyRelease(method: Any): RegularTrigger {
        unregisterKeyRelease()
        return RegularTrigger(method, TriggerType.OTHER).also { onKeyRelease = it }
    }

    fun registerKeyDown(method: Any): RegularTrigger {
        unregisterKeyDown()
        return RegularTrigger(method, TriggerType.OTHER).also { onKeyDown = it }
    }

    fun unregisterKeyPress() = apply {
        onKeyPress?.unregister()
        onKeyPress = null
    }

    fun unregisterKeyRelease() = apply {
        onKeyRelease?.unregister()
        onKeyRelease = null
    }

    fun unregisterKeyDown() = apply {
        onKeyDown?.unregister()
        onKeyDown = null
    }

    internal fun onTick() {
        if (isPressed() && !down) {
            if (keyBinding in customKeyBindings) {
                while (keyBinding.consumeClick()) {
                    // consume the key press if not built-in keybinding
                }
            }

            onKeyPress?.trigger(arrayOf())
            down = true
        }

        if (isKeyDown()) {
            onKeyDown?.trigger(arrayOf())
            down = true
        }

        if (down && !isKeyDown()) {
            while (keyBinding.consumeClick()) {
                // consume the rest of the key presses
            }

            onKeyRelease?.trigger(arrayOf())
            down = false
        }
    }

    /**
     * Returns true if the key is pressed (used for continuous querying).
     *
     * @return whether the key is pressed
     */
    fun isKeyDown(): Boolean = keyBinding.isDown

    /**
     * Returns true on the initial key press. For continuous querying use [isKeyDown].
     *
     * @return whether the key has just been pressed
     */
    fun isPressed(): Boolean = keyBinding.asMixin<KeyMappingAccessor>().clickCount > 0

    /**
     * Gets the description of the key.
     *
     * @return the description
     */
    fun getDescription(): String = keyBinding.saveString()

    /**
     * Gets the key code of the key.
     *
     * @return the integer key code
     */
    fun getKeyCode(): Int = keyBinding.asMixin<KeyMappingAccessor>().key.value

    /**
     * Gets the category of the key.
     *
     * @return the category
     */
    fun getCategory(): String = categoryName

    /**
     * Sets the state of the key.
     *
     * @param pressed True to press, False to release
     */
    fun setState(pressed: Boolean) =
        KeyMapping.set(keyBinding.asMixin<KeyMappingAccessor>().key, pressed)

    override fun toString() = "KeyBind{" +
        "description=${getDescription()}, " +
        "keyCode=${getKeyCode()}, " +
        "category=${getCategory()}" +
        "}"

    companion object : Initializer {
        private val customKeyBindings = mutableSetOf<KeyMapping>()
        private val uniqueCategories = mutableMapOf<String, Int>()
        private val keyBinds = CopyOnWriteArrayList<KeyBind>()

        internal fun getKeyBinds() = keyBinds

        override fun init() {
            ClientTickEvents.START_CLIENT_TICK.register {
                if (!World.isLoaded())
                    return@register

                keyBinds.forEach {
                    // This used to cause crashes on legacy sometimes. If it starts crashing again,
                    // we'll add the empty try-catch block back
                    it.onTick()
                }
            }
        }

        @JvmStatic
        fun clearKeyBinds() {
            keyBinds.toList().forEach(::removeKeyBind)
            customKeyBindings.clear()
            keyBinds.clear()
        }

        internal fun getCategoryName(category: KeyMapping.Category): String {
            return category.id.path
        }

        private fun removeKeyBinding(keyBinding: KeyMapping) {
            Client.getMinecraft().options.asMixin<OptionsAccessor>().setKeyMappings(
                ArrayUtils.removeElement(
                    Client.getMinecraft().options.keyMappings,
                    keyBinding
                )
            )
            val category = keyBinding.category
            val categoryName = getCategoryName(category)

            if (categoryName in uniqueCategories) {
                uniqueCategories[categoryName] = uniqueCategories[categoryName]!! - 1

                if (uniqueCategories[categoryName] == 0) {
                    uniqueCategories.remove(categoryName)
                    KeyMappingCategoryAccessor.getCategoryList().removeIf { it.id.equals(category.id) }
                }
            }
        }

        @JvmStatic
        fun removeKeyBind(keyBind: KeyBind) {
            val keyBinding = keyBind.keyBinding
            if (keyBinding !in customKeyBindings) return

            removeKeyBinding(keyBinding)
            customKeyBindings.remove(keyBinding)
            keyBinds.remove(keyBind)
        }

        private fun addKeyBinding(keyBinding: KeyMapping): KeyMapping {
            Client.getMinecraft().options.asMixin<OptionsAccessor>().setKeyMappings(
                ArrayUtils.add(
                    Client.getMinecraft().options.keyMappings,
                    keyBinding
                )
            )

            if (KeyMappingCategoryAccessor.getCategoryList().none { it.id == keyBinding.category.id }) {
                KeyMappingCategoryAccessor.getCategoryList().add(keyBinding.category)
            }

            return keyBinding
        }

        private fun categoryIdentifier(category: String): Identifier {
            val path = category.lowercase().map { character ->
                if (character in 'a'..'z' || character in '0'..'9' || character in "._-/") character else '_'
            }.joinToString("").trim('/').ifEmpty { "chattriggers" }
            return Identifier.fromNamespaceAndPath(CTJS.MOD_ID, path)
        }
    }
}
