package io.qameta.allure.model

/**
 * POJO that stores status details.
 */
data class StatusDetails(
    var known: Boolean? = null,
    var muted: Boolean? = null,
    var flaky: Boolean? = null,
    var message: String? = null,
    var trace: String? = null
)