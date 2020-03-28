package io.qameta.allure.kotlin.junit4.samples

import org.hamcrest.core.Is
import org.junit.Assume
import org.junit.Test

/**
 * @author gladnik (Nikolai Gladkov)
 */
class AssumptionFailedTest {
    @Test
    fun assumptionFailedTest() {
        Assume.assumeThat(true, Is.`is`(false))
    }
}