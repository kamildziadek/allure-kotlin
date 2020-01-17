package io.qameta.allure.util

/**
 * @author charlie (Dmitry Baev).
 */
object ExceptionUtils {
    fun <T : Throwable> sneakyThrow(throwable: Throwable): Nothing {
        throw throwable as T
    }
}