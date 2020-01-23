package io.qameta.allure.android.rules

import android.app.UiAutomation
import android.os.ParcelFileDescriptor
import androidx.test.platform.app.InstrumentationRegistry
import io.qameta.allure.Allure
import org.junit.rules.*
import org.junit.runner.*
import org.junit.runners.model.*

/**
 * Clears logcat before each test and dumps the logcat as an attachment after test failure.
 */
class LogcatRule(private val fileName: String = "logcat-dump") : TestRule {

    override fun apply(base: Statement?, description: Description?): Statement = object : Statement() {
        override fun evaluate() {
            try {
                clear()
                base?.evaluate()
            } catch (throwable: Throwable) {
                dump()
                throw throwable
            }
        }
    }

    private val uiAutomation: UiAutomation
        get() = InstrumentationRegistry.getInstrumentation().uiAutomation

    private fun dump() {
        val pfd = uiAutomation.executeShellCommand("logcat -d")
        val inputStream = ParcelFileDescriptor.AutoCloseInputStream(pfd).buffered()
        Allure.addAttachment(name = fileName, type = "text/plain", fileExtension = ".txt", content = inputStream)
    }

    private fun clear() {
        uiAutomation.executeShellCommand("logcat -c")
    }

}