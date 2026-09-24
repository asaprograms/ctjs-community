package com.chattriggers.ctjs.engine

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WrappedThreadCompatibilityTest {
    @Test
    fun `legacy thread surface remains available`() {
        val thread = WrappedThread(Runnable {})

        thread.run()
        thread.stop()
        thread.interrupt()
        thread.destroy()
        thread.suspend()
        thread.resume()

        assertFalse(thread.isInterrupted())
        assertTrue(thread.isAlive())
        assertEquals(0L, thread.getId())
        assertNotNull(WrappedThread.currentThread())
        WrappedThread.sleep(0)
    }
}
