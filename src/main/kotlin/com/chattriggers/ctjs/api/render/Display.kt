package com.chattriggers.ctjs.api.render

import com.chattriggers.ctjs.internal.utils.getOption
import net.minecraft.client.gui.GuiGraphicsExtractor
import org.mozilla.javascript.NativeObject
import java.util.concurrent.CopyOnWriteArrayList

class Display() {
    private var lines = CopyOnWriteArrayList<Text>()

    private var x = 0
    private var y = 0
    private var renderX = 0f
    private var renderY = 0f
    private var shouldRender = true
    private var registerType = DisplayHandler.RegisterType.RENDER_OVERLAY
    private var order = Order.NORMAL

    private var backgroundColor: Long = 0x50000000
    private var textColor: Long = 0xffffffff
    private var background = Background.NONE
    private var align = Text.Align.LEFT

    private var minWidth = 0
    private var width = 0
    private var height = 0

    init {
        DisplayHandler.registerDisplay(this)
    }

    constructor(config: NativeObject?) : this() {
        setBackgroundColor(config.getOption("backgroundColor", 0x50000000))
        setTextColor(config.getOption("textColor", 0xffffffff))
        setBackground(config.getOption("background", Background.NONE))
        setAlign(config.getOption("align", Text.Align.LEFT))
        setOrder(config.getOption("order", Order.NORMAL))
        setX(config.getOption("x", 0))
        setY(config.getOption("y", 0))
        setRenderX(config.getOption<Number>("renderX", x).toFloat())
        setRenderY(config.getOption<Number>("renderY", y).toFloat())
        setShouldRender(config.getOption("shouldRender", true))
        setRegisterType(config.getOption("registerType", DisplayHandler.RegisterType.RENDER_OVERLAY))
        setMinWidth(config.getOption("minWidth", 0))
    }

    fun getTextColor(): Long = textColor

    /**
     * Sets the color of the texts
     *
     * Overrides the color of the individual texts
     */
    fun setTextColor(textColor: Long) = apply {
        this.textColor = textColor
    }

    fun getAlign(): Text.Align = align

    /**
     * Set the alignment of the texts in the display
     *
     * Overrides alignment of the individual texts
     */
    fun setAlign(align: Any) = apply {
        this.align = when (align) {
            is CharSequence -> Text.Align.valueOf(align.toString().uppercase())
            is Text.Align -> align
            is DisplayHandler.Align -> Text.Align.valueOf(align.name)
            else -> Text.Align.LEFT
        }
    }

    fun getOrder(): Order = order

    fun setOrder(order: Any) = apply {
        this.order = when (order) {
            is CharSequence -> when (order.toString().uppercase()) {
                "DOWN", "NORMAL" -> Order.NORMAL
                "UP", "REVERSED" -> Order.REVERSED
                else -> Order.NORMAL
            }
            is Order -> order
            is DisplayHandler.Order -> if (order == DisplayHandler.Order.DOWN) Order.NORMAL else Order.REVERSED
            else -> Order.NORMAL
        }
    }

    fun getBackground(): Background = background

    fun setBackground(background: Any) = apply {
        this.background = when (background) {
            is CharSequence -> Background.valueOf(background.toString().uppercase().replace(" ", "_"))
            is Background -> background
            is DisplayHandler.Background -> Background.valueOf(background.name)
            else -> Background.NONE
        }
    }

    fun getBackgroundColor(): Long = backgroundColor

    fun setBackgroundColor(backgroundColor: Long) = apply {
        this.backgroundColor = backgroundColor
    }

    fun setLine(index: Int, line: Any) = apply {
        while (lines.size - 1 < index)
            lines.add(Text(""))

        when (line) {
            is CharSequence -> lines[index].setString(line.toString())
            is Text -> lines[index] = line
            else -> lines[index] = Text("")
        }
    }

    fun getLine(index: Int): Text = lines[index]

    fun getLines(): List<Text> = lines

    fun setLines(lines: MutableList<Text>) = apply {
        this.lines = CopyOnWriteArrayList(lines)
    }

    fun addLine(line: Any) = apply {
        setLine(this.lines.size, line)
    }

    fun addLine(index: Int, line: Any) = apply {
        val text = when (line) {
            is CharSequence -> Text(line.toString())
            is Text -> line
            else -> Text("")
        }
        if (index < 0 || index >= lines.size) lines.add(text) else lines.add(index, text)
    }

    fun addLines(vararg lines: Any) = apply {
        lines.forEach { addLine(it) }
    }

    fun removeLine(index: Int) = apply {
        lines.removeAt(index)
    }

    fun clearLines() = apply {
        lines.clear()
    }

    fun getX(): Int = x

    fun setX(x: Int) = apply {
        this.x = x
        this.renderX = x.toFloat()
    }

    fun getY(): Int = y

    fun setY(y: Int) = apply {
        this.y = y
        this.renderY = y.toFloat()
    }

    fun getRenderX(): Float = renderX

    fun setRenderX(renderX: Float) = apply {
        this.renderX = renderX
        x = renderX.toInt()
    }

    fun getRenderY(): Float = renderY

    fun setRenderY(renderY: Float) = apply {
        this.renderY = renderY
        y = renderY.toInt()
    }

    fun setRenderLoc(renderX: Float, renderY: Float) = apply {
        setRenderX(renderX)
        setRenderY(renderY)
    }

    fun getShouldRender() = shouldRender

    fun setShouldRender(shouldRender: Boolean) = apply {
        this.shouldRender = shouldRender
    }

    fun show() = setShouldRender(true)

    fun hide() = setShouldRender(false)

    fun getRegisterType() = registerType

    fun setRegisterType(registerType: Any) = apply {
        this.registerType = when (registerType) {
            is CharSequence -> DisplayHandler.RegisterType.valueOf(registerType.toString().uppercase().replace(" ", "_"))
            is DisplayHandler.RegisterType -> registerType
            else -> DisplayHandler.RegisterType.RENDER_OVERLAY
        }
    }

    fun getWidth(): Int = width

    fun getHeight(): Int = height

    fun getMinWidth(): Int = minWidth

    fun setMinWidth(minWidth: Int) = apply {
        this.minWidth = minWidth
    }

    fun draw(ctx: GuiGraphicsExtractor) {
        if (!shouldRender) return

        width = lines.maxOfOrNull { it.getWidth() }?.coerceAtLeast(minWidth) ?: minWidth

        val textBackgroundWidth = when (background) {
            Background.FULL -> width
            Background.PER_LINE -> width
            Background.NONE -> null
        }

        var currentHeight = 0

        val linesX = when (align) {
            Text.Align.CENTER -> x + width / 2
            Text.Align.RIGHT -> x + width
            else -> x
        }

        val linesToDraw = when (order) {
            Order.NORMAL -> lines
            Order.REVERSED -> lines.asReversed()
        }

        linesToDraw.forEach {
            if (background === Background.FULL)
                it
                    .setBackground(true)
                    .setBackgroundColor(backgroundColor)

            it
                .setColor(textColor)
                .setAlign(align)
                .draw(ctx, linesX, y + currentHeight, x, textBackgroundWidth)

            it.updateInteractionBounds(
                x - 1.0,
                y + currentHeight - 2.0,
                width + 1.0,
                it.getHeight() + 1.0,
            )

            currentHeight += it.getHeight().toInt()
        }

        height = currentHeight
    }

    internal fun handleClick(x: Double, y: Double, button: Int, pressed: Boolean) {
        if (shouldRender) lines.forEach { it.handleClick(x, y, button, pressed) }
    }

    internal fun handleDrag(deltaX: Double, deltaY: Double, x: Double, y: Double, button: Int) {
        if (shouldRender) lines.forEach { it.handleDrag(deltaX, deltaY, x, y, button) }
    }

    fun render(ctx: GuiGraphicsExtractor) = draw(ctx)

    fun render() {
        val context = DisplayHandler.context()
            ?: throw IllegalStateException("Display.render() must be called from a GUI or overlay render callback")
        draw(context)
    }

    override fun toString() =
        "Display{" +
            "renderX=$x, renderY=$y, " +
            "background=$background, backgroundColor=$backgroundColor, " +
            "textColor=$textColor, align=$align, order=$order, " +
            "minWidth=$minWidth, width=$width, height=$height, " +
            "lines=$lines" +
            "}"

    enum class Background {
        NONE, FULL, PER_LINE
    }

    enum class Order {
        REVERSED, NORMAL
    }
}
