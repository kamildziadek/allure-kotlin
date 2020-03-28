package io.qameta.allure.kotlin.test

import java.util.function.Function

fun <T> function(extraction: (T) -> Any?): Function<T, Any?> = Function { extraction(it) }