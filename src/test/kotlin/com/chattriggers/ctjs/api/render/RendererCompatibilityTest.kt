package com.chattriggers.ctjs.api.render

import org.mozilla.javascript.Context
import org.mozilla.javascript.ImporterTopLevel
import org.mozilla.javascript.NativeJavaClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RendererCompatibilityTest {
    @Test
    fun `legacy integer color call creates packed color outside a manual draw`() {
        val color = Renderer.color(12, 34, 56, 78)

        assertIs<Long>(color)
        assertEquals(Renderer.getColor(12, 34, 56, 78), color)
    }

    @Test
    fun `rhino dispatches JavaScript numbers to packed color compatibility method`() {
        val context = Context.enter()
        try {
            val scope = ImporterTopLevel(context)
            scope.put("Renderer", scope, NativeJavaClass(scope, Renderer::class.java))
            val result = context.evaluateString(scope, "Renderer.color(12, 34, 56, 78)", "test", 1, null)

            assertEquals(Renderer.getColor(12, 34, 56, 78).toDouble(), Context.toNumber(result))
        } finally {
            Context.exit()
        }
    }
}
