package io.qameta.allure.util

import org.assertj.core.api.iterable.Extractor

fun <T> extractor(extraction: (T) -> Any?): Extractor<T, Any?> = Extractor { extraction(it) }