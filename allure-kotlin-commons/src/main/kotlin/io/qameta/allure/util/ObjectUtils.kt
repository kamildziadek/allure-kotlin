package io.qameta.allure.util

import io.qameta.allure.listener.printError
import java.util.*

/**
 * @author charlie (Dmitry Baev).
 */
object ObjectUtils {
    /**
     * Returns string representation of given object. Pretty prints arrays.
     *
     * @param `object` the given object.
     * @return the string representation of given object.
     */
    @JvmStatic
    fun toString(data: Any?): String {
        return try {
            if (data != null && data.javaClass.isArray) {
                when (data) {
                    is LongArray -> return data.contentToString()
                    is ShortArray -> return data.contentToString()
                    is IntArray -> return data.contentToString()
                    is CharArray -> return data.contentToString()
                    is DoubleArray -> return data.contentToString()
                    is FloatArray -> return data.contentToString()
                    is BooleanArray -> return data.contentToString()
                    is ByteArray -> return "<BINARY>"
                    is Array<*> -> return data.contentToString()
                }
            }
            Objects.toString(data)
        } catch (e: Exception) {
            printError("Could not convert object to string", e)
            "<NPE>"
        }
    }
}