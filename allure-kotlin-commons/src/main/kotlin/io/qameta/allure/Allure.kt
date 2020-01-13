package io.qameta.allure

import io.qameta.allure.model.*
import io.qameta.allure.model.Link
import io.qameta.allure.util.ExceptionUtils
import io.qameta.allure.util.ResultsUtils
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * The class contains some useful methods to work with [AllureLifecycle].
 */
object Allure {

    private const val TXT_EXTENSION = ".txt"
    private const val TEXT_PLAIN = "text/plain"
    var lifecycle: AllureLifecycle = AllureLifecycle()

    /**
     * Adds step with provided name and status in current test or step (or test fixture). Takes no effect
     * if no test run at the moment.
     *
     * @param name   the name of step.
     * @param status the step status.
     */
    /**
     * Adds passed step with provided name in current test or step (or test fixture). Takes no effect
     * if no test run at the moment. Shortcut for [.step].
     *
     * @param name the name of step.
     */
    @JvmOverloads
    fun step(
        name: String,
        status: Status = Status.PASSED
    ) {
        val uuid = UUID.randomUUID().toString()
        val step = StepResult().apply {
            this.name = name
            this.status = status
        }
        lifecycle.startStep(uuid, step)
        lifecycle.stopStep(uuid)
    }

    /**
     * Syntax sugar for [.step].
     *
     * @param name     the name of step.
     * @param runnable the step's body.
     */
    fun <T> step(
        name: String? = null,
        runnable: (StepContext) -> T
    ): T? {
        return step {
            it.name(name)
            runnable(it)
        }
    }

    /**
     * Run provided [ThrowableRunnable] as step with given name. Takes no effect
     * if no test run at the moment.
     *
     * @param runnable the step's body.
     */
    fun <T> step(runnable: (StepContext) -> T): T? {
        val uuid = UUID.randomUUID().toString()
        lifecycle.startStep(uuid, StepResult().apply {
            name = "step"
        })

        return try {
            runnable(DefaultStepContext(uuid)).also {
                lifecycle.updateStep(uuid) { it.status = Status.PASSED }
            }
        } catch (throwable: Throwable) {
            lifecycle.updateStep {
                with(it) {
                    status = ResultsUtils.getStatus(throwable) ?: Status.BROKEN
                    statusDetails = ResultsUtils.getStatusDetails(throwable)
                }
            }
            ExceptionUtils.sneakyThrow<RuntimeException>(throwable)
            null
        } finally {
            lifecycle.stopStep(uuid)
        }
    }

    /**
     * Adds epic label to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Shortcut for [.label].
     *
     * @param value the value of label.
     */
    fun epic(value: String?) {
        label(ResultsUtils.EPIC_LABEL_NAME, value)
    }

    /**
     * Adds feature label to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Shortcut for [.label].
     *
     * @param value the value of label.
     */
    fun feature(value: String?) {
        label(ResultsUtils.FEATURE_LABEL_NAME, value)
    }

    /**
     * Adds story label to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Shortcut for [.label].
     *
     * @param value the value of label.
     */
    fun story(value: String?) {
        label(ResultsUtils.STORY_LABEL_NAME, value)
    }

    /**
     * Adds suite label to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Shortcut for [.label].
     *
     * @param value the value of label.
     */
    fun suite(value: String?) {
        label(ResultsUtils.SUITE_LABEL_NAME, value)
    }

    /**
     * Adds label to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment.
     *
     * @param name  the name of label.
     * @param value the value of label.
     */
    fun label(name: String, value: String?) {
        val label = Label(name = name, value = value)
        lifecycle.updateTestCase { it.labels.add(label) }
    }

    /**
     * Adds parameter to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment.
     *
     * @param name  the name of parameter.
     * @param value the value of parameter.
     */
    fun <T> parameter(name: String, value: T): T {
        val parameter = ResultsUtils.createParameter(name, value)
        lifecycle.updateTestCase { it.parameters.add(parameter) }
        return value
    }

    /**
     * Adds issue link to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Shortcut for [.link].
     *
     * @param name the name of link.
     * @param url  the link's url.
     */
    fun issue(name: String?, url: String?) {
        link(name, ResultsUtils.ISSUE_LINK_TYPE, url)
    }

    /**
     * Adds tms link to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Shortcut for [.link].
     *
     * @param name the name of link.
     * @param url  the link's url.
     */
    fun tms(name: String?, url: String?) {
        link(name, ResultsUtils.TMS_LINK_TYPE, url)
    }

    /**
     * Adds link to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Shortcut for [.link]
     *
     * @param url the link's url.
     */
    fun link(url: String?) {
        link(null, url)
    }

    /**
     * Adds link to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Shortcut for [.link]
     *
     * @param name the name of link.
     * @param url  the link's url.
     */
    fun link(name: String?, url: String?) {
        link(name, null, url)
    }

    /**
     * Adds link to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment.
     *
     * @param name the name of link.
     * @param type the type of link, used to display link icon in the report.
     * @param url  the link's url.
     */
    fun link(name: String?, type: String?, url: String?) {
        val link = Link(name = name, url = url, type = type)
        lifecycle.updateTestCase { it.links.add(link) }
    }

    /**
     * Adds description to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Expecting description provided in Markdown format.
     *
     * @param description the description in markdown format.
     * @see .descriptionHtml
     */
    fun description(description: String?) {
        lifecycle.updateTestCase { executable: TestResult ->
            executable.description = description
        }
    }

    /**
     * Adds descriptionHtml to current test or step (or fixture) if any. Takes no effect
     * if no test run at the moment. Note that description will take no effect if descriptionHtml is
     * specified.
     *
     * @param descriptionHtml the description in html format.
     * @see .description
     */
    fun descriptionHtml(descriptionHtml: String?) {
        lifecycle.updateTestCase { executable: TestResult ->
            executable.descriptionHtml = descriptionHtml
        }
    }

    /**
     * Adds attachment.
     *
     * @param name    the name of attachment.
     * @param content the attachment content.
     */
    fun attachment(name: String?, content: String) {
        addAttachment(name, content)
    }

    /**
     * Adds attachment.
     *
     * @param name    the name of attachment.
     * @param content the stream that contains attachment content.
     */
    fun attachment(name: String?, content: InputStream) {
        addAttachment(name, content)
    }

    @Deprecated("use {@link #label(String, String)} instead.")
    fun addLabels(vararg labels: Label) {
        lifecycle.updateTestCase {
            it.labels.addAll(labels.toList())
        }
    }

    @Deprecated("use {@link #link(String, String, String)} instead.")
    fun addLinks(vararg links: Link) {
        lifecycle.updateTestCase {
            it.links.addAll(links.toList())
        }
    }

    @Deprecated("use {@link #description(String)} instead.")
    fun addDescription(description: String?) {
        description(description)
    }

    @Deprecated("use {@link #descriptionHtml(String)} instead.")
    fun addDescriptionHtml(descriptionHtml: String?) {
        descriptionHtml(descriptionHtml)
    }

    fun addAttachment(name: String?, content: String) {
        lifecycle.addAttachment(
            name = name,
            type = TEXT_PLAIN,
            fileExtension = TXT_EXTENSION,
            body = content.toByteArray(StandardCharsets.UTF_8)
        )
    }

    fun addAttachment(name: String?, type: String?, content: String) {
        lifecycle.addAttachment(
            name = name,
            type = type,
            fileExtension = TXT_EXTENSION,
            body = content.toByteArray(StandardCharsets.UTF_8)
        )
    }

    fun addAttachment(
        name: String?,
        type: String?,
        content: String,
        fileExtension: String?
    ) {
        lifecycle.addAttachment(
            name = name,
            type = type,
            fileExtension = fileExtension,
            body = content.toByteArray(StandardCharsets.UTF_8)
        )
    }

    fun addAttachment(name: String?, content: InputStream) {
        lifecycle.addAttachment(name = name, type = null, fileExtension = null, stream = content)
    }

    fun addAttachment(
        name: String?,
        type: String?,
        content: InputStream,
        fileExtension: String?
    ) {
        lifecycle.addAttachment(name = name, type = type, fileExtension = fileExtension, stream = content)
    }

    /**
     * Step context.
     */
    interface StepContext {
        fun name(name: String?)
        fun <T> parameter(name: String, value: T): T
    }

    /**
     * Basic implementation of step context.
     */
    private class DefaultStepContext(private val uuid: String) : StepContext {
        override fun name(name: String?) {
            lifecycle.updateStep(uuid) { it.name = name }
        }

        override fun <T> parameter(name: String, value: T): T {
            val param = ResultsUtils.createParameter(name, value)
            lifecycle.updateStep(uuid) { it.parameters.add(param) }
            return value
        }

    }

}