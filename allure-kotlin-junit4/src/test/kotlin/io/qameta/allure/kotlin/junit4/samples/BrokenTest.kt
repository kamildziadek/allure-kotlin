package io.qameta.allure.kotlin.junit4.samples

import org.junit.Test

/**
 * @author charlie (Dmitry Baev).
 */
class BrokenTest {
    @Test
    @Throws(Exception::class)
    fun brokenTest() {
        throw RuntimeException("Hello, everybody")
    }
}