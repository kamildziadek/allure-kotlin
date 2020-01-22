package io.qameta.allure.android

import androidx.test.filters.LargeTest
import io.qameta.allure.Allure.step
import io.qameta.allure.junit4.DisplayName
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.*

@RunWith(AllureAndroidJUnit4::class)
@LargeTest
class AllureAndroidJUnit4Sample {


    @Test
    @DisplayName("Broken test")
    fun brokenTest() {
        step("Green step") {
            //empty green step
        }
        step("Broken step") {
            throw IllegalStateException()
        }
    }

    @Test
    @DisplayName("Failing test")
    fun failingTest() {
        step("Green Step") {
            //empty green step
        }
        step("Failing Step") {
            fail()
        }
    }

    @Test
    @DisplayName("Green test")
    fun greenTest() {
        step("Green Step #1") {
            //empty green step
        }
        step("Green Step #2") {
        }
    }

}