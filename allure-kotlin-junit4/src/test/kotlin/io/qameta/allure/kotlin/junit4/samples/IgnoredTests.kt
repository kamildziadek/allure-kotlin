package io.qameta.allure.kotlin.junit4.samples

import org.junit.Ignore
import org.junit.Test

/**
 * @author gladnik (Nikolai Gladkov)
 */
class IgnoredTests {
    @Test
    @Ignore
    fun ignoredTest() {
    }

    @Test
    @Ignore("Ignored for some reason")
    fun ignoredWithDescriptionTest() {
    }
}