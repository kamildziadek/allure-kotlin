package io.qameta.allure.model

/**
 * Test statuses.
 */
enum class Status(val value: String) {
    FAILED("failed"),
    BROKEN("broken"),
    PASSED("passed"),
    SKIPPED("skipped");

    companion object {
        fun fromValue(value: String): Status =
            values().firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }

}