package io.qameta.allure.model

import kotlinx.serialization.Serializable

/**
 * POJO that stores status details.
 */
@Serializable
data class StatusDetails(
    var known: Boolean? = null,
    var muted: Boolean? = null,
    var flaky: Boolean? = null,
    var message: String? = null,
    var trace: String? = null
)