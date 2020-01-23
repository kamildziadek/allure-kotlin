package io.qameta.allure.android.extensions

import android.graphics.Bitmap
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import io.qameta.allure.Allure
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
fun Allure.screenshot(name: String? = null, quality: Int = 90): Boolean {
    val inputStream = takeScreenshot(quality) ?: return false.also {
        Log.e(TAG, "Failed to take the screenshot")
    }
    addAttachment(name = name, type = "image/png", content = inputStream, fileExtension = ".png")
    return true
}

private fun takeScreenshot(quality: Int): InputStream? {
    val screenshot = runCatching { InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot() }
        .onFailure { Log.e(TAG, "Failed to take a screenshot", it) }
        .getOrNull() ?: return null
    return ByteArrayOutputStream()
        .apply { screenshot.compress(Bitmap.CompressFormat.PNG, quality, this) }
        .toByteArray()
        .inputStream()
        .also { screenshot.recycle() }
}