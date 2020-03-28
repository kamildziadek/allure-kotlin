package io.qameta.allure.kotlin.util

/**
 * @author charlie (Dmitry Baev).
 */
object ExceptionUtils {
    fun <T : Throwable> sneakyThrow(throwable: Throwable): Nothing {
        throw throwable as T
    }
}