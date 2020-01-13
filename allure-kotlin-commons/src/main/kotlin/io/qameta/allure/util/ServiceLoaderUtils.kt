package io.qameta.allure.util

import io.qameta.allure.listener.printDebug
import io.qameta.allure.listener.printError
import java.util.*

/**
 * Internal service loader util.
 *
 * @see ServiceLoader
 *
 * @since 2.0
 */
object ServiceLoaderUtils {
    /**
     * Load implementation by given type.
     *
     * @param <T>         type of implementation.
     * @param type        the type of implementation to load.
     * @param classLoader the class loader to search for implementations.
     * @return loaded implementations.
    </T> */
    fun <T> load(type: Class<T>, classLoader: ClassLoader?): List<T> {
        val loaded: MutableList<T> = ArrayList()
        val iterator: Iterator<T> = ServiceLoader.load(type, classLoader).iterator()
        while (iterator.hasNextSafely()) {
            try {
                val next = iterator.next()
                loaded.add(next)
                printDebug("Found $type")
            } catch (e: Exception) {
                printError("Could not load $type: $e", e)
            }
        }
        return loaded
    }

    /**
     * Safely check for <pre>iterator.hasNext()</pre>.
     *
     * @param this@hasNextSafely specified iterator to check he presence of next element
     * @return `true` if the iteration has more elements, false otherwise
     */
    private fun Iterator<*>.hasNextSafely(): Boolean {
        return try { /* Throw a ServiceConfigurationError if a provider-configuration file violates the specified format,
            or if it names a provider class that cannot be found and instantiated, or if the result of
            instantiating the class is not assignable to the service type, or if any other kind of exception
            or error is thrown as the next provider is located and instantiated.
            @see http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html#iterator()
            */
            hasNext()
        } catch (e: Exception) {
            printError("iterator.hasNext() failed", e)
            false
        }
    }

}