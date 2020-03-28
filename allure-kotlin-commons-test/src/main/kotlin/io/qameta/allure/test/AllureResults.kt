
package io.qameta.allure.test

import io.qameta.allure.kotlin.model.TestResult
import io.qameta.allure.kotlin.model.TestResultContainer

/**
 * @author charlie (Dmitry Baev).
 */
interface AllureResults {
    val testResults: List<TestResult>
    val testResultContainers: List<TestResultContainer>
    val attachments: Map<String, ByteArray>
}