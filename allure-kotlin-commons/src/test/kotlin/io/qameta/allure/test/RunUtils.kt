/*
 *  Copyright 2019 Qameta Software OÜ
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.qameta.allure.test

import io.qameta.allure.Allure
import io.qameta.allure.AllureLifecycle
import io.qameta.allure.model.TestResult
import io.qameta.allure.util.ResultsUtils.getStatus
import io.qameta.allure.util.ResultsUtils.getStatusDetails
import java.util.*

/**
 * @author charlie (Dmitry Baev).
 */
object RunUtils {
    fun runWithinTestContext(runnable: Runnable): AllureResults {
        return runWithinTestContext(runnable, { Allure.lifecycle = it })
    }

    @JvmStatic
    @SafeVarargs
    fun runWithinTestContext(
        runnable: Runnable,
        vararg configurers: (AllureLifecycle) -> Unit
    ): AllureResults {
        val writer = AllureResultsWriterStub()
        val lifecycle = AllureLifecycle(writer)
        val uuid = UUID.randomUUID().toString()
        val result = TestResult(uuid)
        val cached = Allure.lifecycle
        try {
            configurers.forEach { it(lifecycle) }
            lifecycle.scheduleTestCase(result)
            lifecycle.startTestCase(uuid)
            runnable.run()
        } catch (e: Throwable) {
            lifecycle.updateTestCase(uuid) { testResult: TestResult ->
                getStatus(e)?.let { testResult.status = it }
                getStatusDetails(e)?.let { testResult.statusDetails = it }
            }
        } finally {
            lifecycle.stopTestCase(uuid)
            lifecycle.writeTestCase(uuid)
            configurers.forEach { it(cached) }
        }
        return writer
    }

}