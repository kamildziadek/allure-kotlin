package io.qameta.allure.model

/**
 * POJO that stores test result information.
 */
data class TestResult(
    var uuid: String? = null,
    var historyId: String? = null,
    var testCaseId: String? = null,
    var rerunOf: String? = null,
    var fullName: String? = null,
    val labels: MutableList<Label> = mutableListOf(),
    override val links: MutableList<Link> = mutableListOf()
) : ExecutableItem(), WithLinks