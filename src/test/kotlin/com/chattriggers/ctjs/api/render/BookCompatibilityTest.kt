package com.chattriggers.ctjs.api.render

import com.chattriggers.ctjs.api.message.Message
import net.minecraft.SharedConstants
import net.minecraft.server.Bootstrap
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertSame

class BookCompatibilityTest {
    @BeforeTest
    fun bootstrapRegistries() {
        SharedConstants.tryDetectVersion()
        Bootstrap.bootStrap()
    }

    @Test
    fun `pages can be added and replaced with legacy Messages`() {
        val book = Book()

        assertSame(book, book.addPage(Message("first")))
        assertSame(book, book.addPage("second"))
        assertSame(book, book.setPage(0, Message("replacement")))
    }
}
