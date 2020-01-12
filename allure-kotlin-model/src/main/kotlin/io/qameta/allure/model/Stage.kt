package io.qameta.allure.model


/**
 * Test stages.
 */
enum class Stage(val value: String) {
    SCHEDULED("scheduled"),
    RUNNING("running"),
    FINISHED("finished"),
    PENDING("pending"),
    INTERRUPTED("interrupted");

    companion object {
        fun fromValue(value: String): Stage =
            values().firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
}