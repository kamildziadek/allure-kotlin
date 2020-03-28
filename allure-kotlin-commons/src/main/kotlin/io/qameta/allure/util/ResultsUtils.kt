package io.qameta.allure.util

import io.qameta.allure.*
import io.qameta.allure.kotlin.model.*
import io.qameta.allure.kotlin.model.Link
import io.qameta.allure.util.ObjectUtils.toString
import io.qameta.allure.util.PropertiesUtils.loadAllureProperties
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.management.ManagementFactory
import java.lang.reflect.Method
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.logging.Logger

/**
 * The collection of Allure utils methods.
 */
object ResultsUtils {

    private val LOGGER: Logger = loggerFor<ResultsUtils>()

    const val ALLURE_HOST_NAME_SYSPROP = "allure.hostName"
    const val ALLURE_HOST_NAME_ENV = "ALLURE_HOST_NAME"
    const val ALLURE_THREAD_NAME_SYSPROP = "allure.threadName"
    const val ALLURE_THREAD_NAME_ENV = "ALLURE_THREAD_NAME"
    const val ALLURE_SEPARATE_LINES_SYSPROP = "allure.description.javadoc.separateLines"

    const val ISSUE_LINK_TYPE = "issue"
    const val TMS_LINK_TYPE = "tms"
    const val CUSTOM_LINK_TYPE = "custom"

    const val ALLURE_ID_LABEL_NAME = "AS_ID"
    const val SUITE_LABEL_NAME = "suite"
    const val PARENT_SUITE_LABEL_NAME = "parentSuite"
    const val SUB_SUITE_LABEL_NAME = "subSuite"
    const val EPIC_LABEL_NAME = "epic"
    const val FEATURE_LABEL_NAME = "feature"
    const val STORY_LABEL_NAME = "story"
    const val SEVERITY_LABEL_NAME = "severity"
    const val TAG_LABEL_NAME = "tag"
    const val OWNER_LABEL_NAME = "owner"
    const val LEAD_LABEL_NAME = "lead"
    const val HOST_LABEL_NAME = "host"
    const val THREAD_LABEL_NAME = "thread"
    const val TEST_METHOD_LABEL_NAME = "testMethod"
    const val TEST_CLASS_LABEL_NAME = "testClass"
    const val PACKAGE_LABEL_NAME = "package"
    const val FRAMEWORK_LABEL_NAME = "framework"
    const val LANGUAGE_LABEL_NAME = "language"

    private const val ALLURE_DESCRIPTIONS_PACKAGE = "allureDescriptions/"
    private const val MD_5 = "MD5"
    private var cachedHost: String? = null
    fun createParameter(name: String, value: Any?): Parameter {
        return Parameter(name = name, value = toString(value))
    }

    fun createSuiteLabel(suite: String?): Label {
        return createLabel(SUITE_LABEL_NAME, suite)
    }

    fun createParentSuiteLabel(suite: String?): Label {
        return createLabel(PARENT_SUITE_LABEL_NAME, suite)
    }

    fun createSubSuiteLabel(suite: String?): Label {
        return createLabel(SUB_SUITE_LABEL_NAME, suite)
    }

    fun createTestMethodLabel(testMethod: String?): Label {
        return createLabel(TEST_METHOD_LABEL_NAME, testMethod)
    }

    fun createTestClassLabel(testClass: String?): Label {
        return createLabel(TEST_CLASS_LABEL_NAME, testClass)
    }

    fun createPackageLabel(packageName: String?): Label {
        return createLabel(PACKAGE_LABEL_NAME, packageName)
    }

    fun createEpicLabel(epic: String?): Label {
        return createLabel(EPIC_LABEL_NAME, epic)
    }

    fun createFeatureLabel(feature: String?): Label {
        return createLabel(FEATURE_LABEL_NAME, feature)
    }

    fun createStoryLabel(story: String?): Label {
        return createLabel(STORY_LABEL_NAME, story)
    }

    fun createTagLabel(tag: String?): Label {
        return createLabel(TAG_LABEL_NAME, tag)
    }

    fun createOwnerLabel(owner: String?): Label {
        return createLabel(OWNER_LABEL_NAME, owner)
    }

    fun createSeverityLabel(severity: SeverityLevel): Label {
        return createSeverityLabel(severity.value)
    }

    fun createSeverityLabel(severity: String?): Label {
        return createLabel(SEVERITY_LABEL_NAME, severity)
    }

    fun createHostLabel(): Label {
        return createLabel(
            HOST_LABEL_NAME,
            hostName
        )
    }

    fun createThreadLabel(): Label {
        return createLabel(
            THREAD_LABEL_NAME,
            threadName
        )
    }

    fun createFrameworkLabel(framework: String?): Label {
        return createLabel(FRAMEWORK_LABEL_NAME, framework)
    }

    fun createLanguageLabel(language: String?): Label {
        return createLabel(LANGUAGE_LABEL_NAME, language)
    }

    fun createLabel(name: String, value: String?): Label {
        return Label(name = name, value = value)
    }

    fun createLabel(owner: Owner): Label {
        return createOwnerLabel(owner.value)
    }

    fun createLabel(severity: Severity): Label {
        return createSeverityLabel(severity.value)
    }

    fun createLabel(story: Story): Label {
        return createStoryLabel(story.value)
    }

    fun createLabel(feature: Feature): Label {
        return createFeatureLabel(feature.value)
    }

    fun createLabel(epic: Epic): Label {
        return createEpicLabel(epic.value)
    }

    @JvmStatic
    fun createIssueLink(value: String?): Link {
        return createLink(value, null, null, ISSUE_LINK_TYPE)
    }

    @JvmStatic
    fun createTmsLink(value: String?): Link {
        return createLink(value, null, null, TMS_LINK_TYPE)
    }

    fun createLink(link: io.qameta.allure.Link): Link {
        return createLink(link.value, link.name, link.url, link.type)
    }

    fun createLink(link: Issue): Link {
        return createIssueLink(link.value)
    }

    fun createLink(link: TmsLink): Link {
        return createTmsLink(link.value)
    }

    @JvmStatic
    fun createLink(
        value: String?, name: String?,
        url: String?, type: String?
    ): Link {
        val resolvedName = firstNonEmpty(value) ?: name
        val resolvedUrl = firstNonEmpty(url) ?: getLinkUrl(name = resolvedName, type = type)
        return Link(name = resolvedName, url = resolvedUrl, type = type)
    }

    fun getProvidedLabels(): Set<Label> {
        val properties = loadAllureProperties()
        val propertyNames = properties.stringPropertyNames()
        return propertyNames.asSequence()
            .filter { name: String -> name.startsWith("allure.label.") }
            .map { name: String ->
                val labelName = name.substring(13)
                val labelValue = properties.getProperty(name)
                Label(name = labelName, value = labelValue)
            }
            .filter { it.value != null }
            .toSet()
    }

    val hostName: String?
        get() {
            val fromProperty = System.getProperty(ALLURE_HOST_NAME_SYSPROP)
            val fromEnv = System.getenv(ALLURE_HOST_NAME_ENV)
            return listOfNotNull(fromProperty, fromEnv).firstOrNull() ?: realHostName
        }

    val threadName: String
        get() {
            val fromProperty = System.getProperty(ALLURE_THREAD_NAME_SYSPROP)
            val fromEnv = System.getenv(ALLURE_THREAD_NAME_ENV)
            return listOfNotNull(fromProperty, fromEnv).firstOrNull() ?: realThreadName
        }


    @JvmStatic
    fun getStatus(throwable: Throwable?): Status? {
        return throwable?.let { if (it is AssertionError) Status.FAILED else Status.BROKEN }
    }

    @JvmStatic
    fun getStatusDetails(throwable: Throwable?): StatusDetails? {
        return throwable?.let {
            StatusDetails(
                message = it.message ?: it.javaClass.name,
                trace = getStackTraceAsString(it)
            )
        }

    }

    fun getJavadocDescription(classLoader: ClassLoader, method: Method): String? {
        val name = method.name
        val parameterTypes: List<String> = method.parameterTypes.map { obj -> obj.typeName }

        val signatureHash = generateMethodSignatureHash(
            className = method.declaringClass.name,
            methodName = name,
            parameterTypes = parameterTypes
        )
        return readResource(
            classLoader,
            ALLURE_DESCRIPTIONS_PACKAGE + signatureHash
        )?.let { desc -> if (separateLines()) desc.replace("\n", "<br />") else desc }
    }

    fun firstNonEmpty(vararg items: String?): String? {
        return items.asSequence()
            .filterNotNull()
            .filter { it.isNotEmpty() }
            .firstOrNull()
    }

    @JvmStatic
    fun getLinkTypePatternPropertyName(type: String?): String {
        return String.format("allure.link.%s.pattern", type)
    }

    fun generateMethodSignatureHash(
        className: String,
        methodName: String,
        parameterTypes: List<String>
    ): String {
        val md = md5Digest.apply {
            update(className.toByteArray(StandardCharsets.UTF_8))
            update(methodName.toByteArray(StandardCharsets.UTF_8))
        }
        parameterTypes.asSequence()
            .map { string -> string.toByteArray(StandardCharsets.UTF_8) }
            .forEach(md::update)
        val bytes = md.digest()
        return bytesToHex(bytes)
    }

    fun md5(source: String): String {
        return bytesToHex(md5Digest.digest(source.toByteArray(StandardCharsets.UTF_8)))
    }

    fun bytesToHex(bytes: ByteArray?): String {
        return BigInteger(1, bytes).toString(16)
    }

    val md5Digest: MessageDigest
        get() = try {
            MessageDigest.getInstance(MD_5)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalStateException("Can not find hashing algorithm", e)
        }

    private fun getLinkUrl(name: String?, type: String?): String? {
        val properties = loadAllureProperties()
        val pattern = properties.getProperty(getLinkTypePatternPropertyName(type))
        return pattern?.replace("{}", name ?: "")
    }

    private val realHostName: String?
        get() = cachedHost?.let {
            try {
                InetAddress.getLocalHost().hostName
            } catch (e: UnknownHostException) {
                LOGGER.debug("Could not get host name $e")
                "default"
            }.also {
                cachedHost = it
            }
        }

    private val realThreadName: String
        get() {
            //resolving of bean fails on Android due to NoClassDefFoundError, hence adding additional safety check
            val bean = runCatching { ManagementFactory.getRuntimeMXBean().name }.getOrNull()?.let { "$it." } ?: ""
            val currentThread = Thread.currentThread()
            return "$bean${currentThread.name}(${currentThread.id})"
        }

    private fun getStackTraceAsString(throwable: Throwable): String {
        val stringWriter = StringWriter()
        throwable.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }

    @Deprecated("use {@link #getJavadocDescription(ClassLoader, Method)} instead.")
    fun processDescription(
        classLoader: ClassLoader,
        method: Method,
        item: ExecutableItem
    ) {
        if (method.isAnnotationPresent(Description::class.java)) {
            if (method.getAnnotation(Description::class.java).useJavaDoc) {
                getJavadocDescription(classLoader, method)?.let {
                    item.descriptionHtml = it
                }
            } else {
                val description = method.getAnnotation(Description::class.java).value
                item.description = description
            }
        }
    }

    private fun readResource(
        classLoader: ClassLoader,
        resourceName: String
    ): String? {
        return try {
            classLoader.getResourceAsStream(resourceName)?.toByteArray()?.let { String(it, StandardCharsets.UTF_8) }
        } catch (e: IOException) {
            LOGGER.error("Unable to process description resource file", e)
            null
        }
    }

    private fun separateLines(): Boolean {
        return "true".equals(loadAllureProperties().getProperty(ALLURE_SEPARATE_LINES_SYSPROP), ignoreCase = true)
    }
}