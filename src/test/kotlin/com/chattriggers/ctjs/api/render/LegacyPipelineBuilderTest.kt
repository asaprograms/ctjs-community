package com.chattriggers.ctjs.api.render

import com.mojang.blaze3d.platform.CompareOp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LegacyPipelineBuilderTest {
    @Test
    fun `legacy render state changes are applied to custom pipelines`() {
        val pipeline = LegacyPipelineBuilder
            .enabledBlend()
            .disableCull()
            .disableDepth()
            .build()

        val depthState = requireNotNull(pipeline.depthStencilState)
        val colorState = requireNotNull(pipeline.colorTargetState)

        assertFalse(pipeline.isCull())
        assertEquals(CompareOp.ALWAYS_PASS, depthState.depthTest())
        assertFalse(depthState.writeDepth())
        assertTrue(colorState.blendFunction().isPresent)

        LegacyPipelineBuilder.enableDepth().enableCull().disableBlend()
    }
}
