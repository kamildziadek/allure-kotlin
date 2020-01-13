package io.qameta.allure

import io.qameta.allure.model.TestResult
import io.qameta.allure.model.TestResultContainer
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class FileSystemResultsWriter(private val outputDirectory: Path) : AllureResultsWriter {
    private val mapper: Json = Json.indented

    @ImplicitReflectionSerializer
    override fun write(testResult: TestResult) {
        val testResultName = testResult.uuid ?: generateTestResultName()
        createDirectories(outputDirectory)
        val file = outputDirectory.resolve(testResultName)
        try {
            val json = mapper.stringify(testResult)
            file.toFile().writeText(json)
        } catch (e: IOException) {
            throw AllureResultsWriteException("Could not write Allure test result", e)
        }
    }

    @ImplicitReflectionSerializer
    override fun write(testResultContainer: TestResultContainer) {
        val testResultContainerName = testResultContainer.uuid ?: generateTestResultContainerName()
        createDirectories(outputDirectory)
        val filePath = outputDirectory.resolve(testResultContainerName)
        try {
            val json = mapper.stringify(testResultContainer)
            filePath.toFile().writeText(json)
        } catch (e: IOException) {
            throw AllureResultsWriteException("Could not write Allure test result container", e)
        }
    }

    override fun write(source: String, attachment: InputStream) {
        createDirectories(outputDirectory)
        val filePath = outputDirectory.resolve(source)
        try {
            attachment.use { Files.copy(it, filePath) }
        } catch (e: IOException) {
            throw AllureResultsWriteException("Could not write Allure attachment", e)
        }
    }

    private fun createDirectories(directory: Path) {
        try {
            Files.createDirectories(directory)
        } catch (e: IOException) {
            throw AllureResultsWriteException("Could not create Allure results directory", e)
        }
    }

    private fun generateTestResultContainerName(uuid: String? = UUID.randomUUID().toString()): String =
        uuid + AllureConstants.TEST_RESULT_CONTAINER_FILE_SUFFIX

    companion object {
        @JvmStatic
        @JvmOverloads
        fun generateTestResultName(uuid: String? = UUID.randomUUID().toString()): String {
            return uuid + AllureConstants.TEST_RESULT_FILE_SUFFIX
        }

    }
}