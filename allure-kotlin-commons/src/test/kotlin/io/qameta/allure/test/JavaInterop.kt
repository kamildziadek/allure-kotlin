package io.qameta.allure.test

import org.assertj.core.api.iterable.Extractor
import java.util.function.Function

fun <T> function(extraction: (T) -> Any?): Function<T, Any?> = Function { extraction(it) }
fun <T> extractor(extraction: (T) -> Any?): Extractor<T, Any?> = Extractor { extraction(it) }