package com.chattriggers.ctjs.api.client

import kotlin.test.Test
import kotlin.test.assertEquals

class ClientCompatibilityTest {
    @Test
    fun `mouse coordinates use GUI scaling`() {
        assertEquals(160.0, Client.scaleMouseCoordinate(960.0, 320, 1920))
        assertEquals(90.0, Client.scaleMouseCoordinate(540.0, 180, 1080))
    }

    @Test
    fun `mouse coordinate scaling avoids division by zero during window setup`() {
        assertEquals(1280.0, Client.scaleMouseCoordinate(640.0, 2, 0))
    }
}
