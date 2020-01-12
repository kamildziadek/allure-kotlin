package io.qameta.allure.model

import kotlinx.serialization.Serializable

/**
 * POJO that stores attachment information.
 */
@Serializable
data class Attachment(
    var name: String? = null,
    var source: String? = null,
    var type: String? = null
)