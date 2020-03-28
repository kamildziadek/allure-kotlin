package io.qameta.allure.android.extensions

import android.app.UiAutomation
import android.graphics.Bitmap
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import io.qameta.allure.kotlin.Allure
import java.io.ByteArrayOutputStream
import java.io.InputStream

private const val TAG = "AllureExtensions"

/**
 * Takes a screenshot of a device in a given [quality] and attaches it to current step or test run.
 * It uses [android.app.UiAutomation] under the hood to take the screenshot (just like the UiAutomator).
 *
 * @return true if screen shot is created and attached successfully, false otherwise
 */
@Suppress("unused")
fun Allure.screenshot(name: String = "screenshot", quality: Int = 90): Boolean {
    val uiAutomation = InstrumentationRegistry.getInstrumentation().uiAutomation ?: return false.also {
        Log.e(TAG, "UiAutomation is unavailable. Can't take the screenshot")
    }
    val inputStream = uiAutomation.takeScreenshot(quality) ?: return false.also {
        Log.e(TAG, "Failed to take the screenshot")
    }
    attachment(name = name, content = inputStream, type = "image/png", fileExtension = ".png")
    return true
}

private fun UiAutomation.takeScreenshot(quality: Int): InputStream? {
    val screenshot = takeScreenshot() ?: return null
    return ByteArrayOutputStream()
        .apply { screenshot.compress(Bitmap.CompressFormat.PNG, quality, this) }
        .toByteArray()
        .inputStream()
        .also { screenshot.recycle() }
}