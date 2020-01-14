
package io.qameta.allure.test

import io.qameta.allure.model.TestResult
import io.qameta.allure.model.TestResultContainer

/**
 * @author charlie (Dmitry Baev).
 */
interface AllureResults {
    val testResults: List<TestResult>
    val testResultContainers: List<TestResultContainer>
    val attachments: Map<String, ByteArray>
}