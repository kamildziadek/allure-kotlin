package io.qameta.allure.junit4.samples

import org.junit.Test

/**
 * @author charlie (Dmitry Baev).
 */
class BrokenWithoutMessageTest {
    @Test
    @Throws(Exception::class)
    fun brokenWithoutMessageTest() {
        throw RuntimeException()
    }
}