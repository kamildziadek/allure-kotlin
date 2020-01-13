package io.qameta.allure

import io.qameta.allure.util.ResultsUtils
import java.lang.annotation.Inherited

/**
 * Used to mark tests with feature label.
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
@Repeatable
@LabelAnnotation(name = ResultsUtils.FEATURE_LABEL_NAME)
annotation class Feature(val value: String)