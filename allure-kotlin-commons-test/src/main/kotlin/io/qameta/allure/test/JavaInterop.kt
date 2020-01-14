package io.qameta.allure.test

import java.util.function.Function

fun <T> function(extraction: (T) -> Any?): Function<T, Any?> = Function { extraction(it) }