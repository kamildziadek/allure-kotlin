package io.qameta.allure

import io.qameta.allure.util.ResultsUtils
import java.lang.annotation.Inherited

/**
 * Used to link tests with issues.
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
@LinkAnnotation(type = ResultsUtils.ISSUE_LINK_TYPE)
@Repeatable
annotation class Issue(val value: String)