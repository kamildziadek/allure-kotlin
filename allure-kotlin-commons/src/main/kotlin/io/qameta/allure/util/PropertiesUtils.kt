package io.qameta.allure.util

import io.qameta.allure.listener.printError
import java.io.IOException
import java.util.*

/**
 * The collection of properties utils methods.
 */
object PropertiesUtils {
    private const val ALLURE_PROPERTIES_FILE = "allure.properties"
    @JvmStatic
    fun loadAllureProperties(): Properties = Properties().apply {
        loadPropertiesFrom(ClassLoader.getSystemClassLoader())
        loadPropertiesFrom(Thread.currentThread().contextClassLoader)
        putAll(System.getProperties())
    }

    private fun Properties.loadPropertiesFrom(classLoader: ClassLoader) {
        if (classLoader.getResource(ALLURE_PROPERTIES_FILE) != null) {
            try {
                classLoader.getResourceAsStream(ALLURE_PROPERTIES_FILE)
                    .use { stream -> load(stream) }
            } catch (e: IOException) {
                printError("Error while reading allure.properties file from classpath: ${e.message}", e)
            }
        }
    }
}