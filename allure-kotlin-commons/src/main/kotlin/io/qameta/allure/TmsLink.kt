package io.qameta.allure

import io.qameta.allure.util.ResultsUtils
import java.lang.annotation.Inherited

/**
 * Used to link tests with test cases in external test management system.
 */
@MustBeDocumented
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@LinkAnnotation(type = ResultsUtils.TMS_LINK_TYPE)
@Repeatable
annotation class TmsLink(val value: String)