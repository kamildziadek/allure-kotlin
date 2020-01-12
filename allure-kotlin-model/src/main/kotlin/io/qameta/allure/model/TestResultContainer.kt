package io.qameta.allure.model

/**
 * POJO that stores information about test fixtures.
 */
class TestResultContainer(
    var uuid: String? = null,
    var name: String? = null,
    var description: String? = null,
    var descriptionHtml: String? = null,
    var start: Long? = null,
    var stop: Long? = null,
    val children: MutableList<String> = mutableListOf(),
    val befores: MutableList<FixtureResult> = mutableListOf(),
    val afters: MutableList<FixtureResult> = mutableListOf(),
    override val links: MutableList<Link> = mutableListOf()
) : WithLinks