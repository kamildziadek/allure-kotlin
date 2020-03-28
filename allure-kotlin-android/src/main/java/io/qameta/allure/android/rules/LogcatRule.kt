package io.qameta.allure.android.rules

import android.app.UiAutomation
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import io.qameta.allure.kotlin.Allure
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

    private val uiAutomation: UiAutomation?
        get() = InstrumentationRegistry.getInstrumentation().uiAutomation

    private fun dump() {
        val uiAutomation = uiAutomation ?: return Unit.also {
            Log.e(TAG, "UiAutomation is unavailable. Dumping logs failed.")
        }
        val pfd = uiAutomation.executeShellCommand("logcat -d")
        val inputStream = ParcelFileDescriptor.AutoCloseInputStream(pfd).buffered()
        Allure.addAttachment(name = fileName, type = "text/plain", fileExtension = ".txt", content = inputStream)
    }

    private fun clear() {
        val uiAutomation = uiAutomation ?: return Unit.also {
            Log.e(TAG, "UiAutomation is unavailable. Clearing logs failed.")
        }
        uiAutomation.executeShellCommand("logcat -c")
    }

    companion object {
        private val TAG = LogcatRule::class.java.simpleName
    }
}