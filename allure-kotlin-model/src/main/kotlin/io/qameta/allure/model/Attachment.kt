package io.qameta.allure.model

/**
 * POJO that stores attachment information.
 */
data class Attachment(
    var name: String? = null,
    var source: String? = null,
    var type: String? = null
)