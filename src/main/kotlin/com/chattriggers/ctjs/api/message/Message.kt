package com.chattriggers.ctjs.api.message

/**
 * The mutable message container exposed by ChatTriggers 1.8.9.
 *
 * Modern CTJS uses [TextComponent] directly. This wrapper keeps the older
 * constructor and mutating methods available to existing modules.
 */
class Message(vararg initialParts: Any) {
    private val parts = initialParts.toMutableList()
    private var chatLineId = -1
    private var recursive = false
    private var formatted = true

    fun getChatMessage(): TextComponent = toTextComponent()
    fun getFormattedText(): String = toTextComponent().formattedText.removePrefix("§r")
    fun getUnformattedText(): String = toTextComponent().unformattedText
    fun getMessageParts(): Array<TextComponent> = parts.map { TextComponent(it) }.toTypedArray()
    fun getChatLineId(): Int = chatLineId
    fun setChatLineId(id: Int) = apply { chatLineId = id }
    fun isRecursive(): Boolean = recursive
    fun setRecursive(value: Boolean) = apply { recursive = value }
    fun isFormatted(): Boolean = formatted
    fun setFormatted(value: Boolean) = apply { formatted = value }

    fun setTextComponent(index: Int, component: Any) = apply { parts[index] = component }
    fun addTextComponent(component: Any) = apply { parts.add(component) }
    fun addTextComponent(index: Int, component: Any) = apply { parts.add(index, component) }
    fun clone(): Message = copy()
    fun copy(): Message = Message(*parts.toTypedArray()).also {
        it.chatLineId = chatLineId
        it.recursive = recursive
        it.formatted = formatted
    }

    fun edit(vararg replacements: Message) = ChatLib.editChat(this, *replacements)
    fun chat() = apply { toTextComponent().chat() }
    fun actionBar() = apply { toTextComponent().actionBar() }

    internal fun toTextComponent(): TextComponent {
        val text = TextComponent(*parts.toTypedArray())
        text.setChatLineId(chatLineId)
        text.setRecursive(recursive)
        text.setFormatted(formatted)
        return text
    }
}
