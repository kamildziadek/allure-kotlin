package io.qameta.allure.util

import io.qameta.allure.LabelAnnotation
import io.qameta.allure.LinkAnnotation
import io.qameta.allure.listener.printError
import io.qameta.allure.model.Label
import io.qameta.allure.model.Link
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.Arrays
import kotlin.collections.HashSet

/**
 * Collection of utils used by Allure integration to extract meta information from
 * test cases via reflection.
 *
 */
//todo USE SEQUENCE ?
object AnnotationUtils {
    private const val VALUE_METHOD_NAME = "value"
    /**
     * Returns links created from Allure meta annotations specified on annotated element.
     *
     * @param annotatedElement the element to search annotations on.
     * @return discovered links.
     */
    fun getLinks(annotatedElement: AnnotatedElement): Set<Link> {
        return getLinks(*annotatedElement.declaredAnnotations)
    }

    /**
     * Shortcut for [.getLinks].
     *
     * @param annotations annotations to analyse.
     * @return discovered links.
     */
    fun getLinks(vararg annotations: Annotation): Set<Link> {
        return getLinks(annotations.toList())
    }

    /**
     * Returns links from given annotations.
     *
     * @param annotations annotations to analyse.
     * @return discovered links.
     */
    @JvmStatic
    fun getLinks(annotations: Collection<Annotation>): Set<Link> {
        return extractMetaAnnotations(LinkAnnotation::class.java, ::extractLinks, annotations).toSet()
    }

    /**
     * Returns labels created from Allure meta annotations specified on annotated element.
     * Shortcut for [.getLinks]
     *
     * @param annotatedElement the element to search annotations on.
     * @return discovered labels.
     */
    fun getLabels(annotatedElement: AnnotatedElement): Set<Label> {
        return getLabels(*annotatedElement.declaredAnnotations)
    }

    /**
     * Shortcut for [.getLabels].
     *
     * @param annotations annotations to analyse.
     * @return discovered labels.
     */
    fun getLabels(vararg annotations: Annotation?): Set<Label> {
        return getLabels(
            Arrays.asList<Annotation>(
                *annotations
            )
        )
    }

    /**
     * Returns labels from given annotations.
     *
     * @param annotations annotations to analyse.
     * @return discovered labels.
     */
    @JvmStatic
    fun getLabels(annotations: Collection<Annotation>): Set<Label> {
        return extractMetaAnnotations(LabelAnnotation::class.java, ::extractLabels, annotations).toSet()
    }

    private fun <Result, AnnotationClazz : Annotation> extractMetaAnnotations(
        annotationType: Class<AnnotationClazz>,
        mapper: (AnnotationClazz, Annotation) -> List<Result>,
        candidates: Collection<Annotation>
    ): List<Result> {
        val visited: MutableSet<Annotation> = HashSet()
        return candidates
            .flatMap(this::extractRepeatable)
            .flatMap { candidate ->
                extractMetaAnnotations(annotationType, mapper, candidate, visited)
            }
    }

    private fun <Result, AnnotationClazz : Annotation> extractMetaAnnotations(
        annotationType: Class<AnnotationClazz>,
        mapper: (AnnotationClazz, Annotation) -> List<Result>,
        candidate: Annotation,
        visited: MutableSet<Annotation>
    ): List<Result> {
        if (!isInJavaLangAnnotationPackage(candidate.annotationType()) && visited.add(candidate)) {
            val children = candidate.annotationType().annotations
                .flatMap { extractRepeatable(it) }
                .flatMap {
                    extractMetaAnnotations(annotationType, mapper, it, visited)
                }

            val current: List<Result> =
                candidate.annotationType().getAnnotationsByType(annotationType)
                    .flatMap { marker -> mapper(marker, candidate) }
            return current + children
        }
        return emptyList()
    }

    private fun extractLabels(
        m: LabelAnnotation,
        annotation: Annotation
    ): List<Label> {
        return if (m.value == LabelAnnotation.DEFAULT_VALUE) {
            callValueMethod(annotation)
                .map { ResultsUtils.createLabel(m.name, it) }
        } else listOf(ResultsUtils.createLabel(m.name, m.value))
    }

    private fun extractLinks(
        m: LinkAnnotation,
        annotation: Annotation
    ): List<Link> { // this is required as Link annotation uses name attribute as value alias.
        if (annotation is io.qameta.allure.Link) {
            return listOf(ResultsUtils.createLink(annotation))
        }
        return if (m.value == LinkAnnotation.DEFAULT_VALUE) {
            callValueMethod(annotation)
                .map { value ->
                    ResultsUtils.createLink(
                        value,
                        null,
                        m.url,
                        m.type
                    )
                }
        } else listOf(ResultsUtils.createLink(m.value, null, m.url, m.type))
    }

    private fun callValueMethod(annotation: Annotation): List<String> {
        return try {
            val method: Method = annotation.annotationType().getMethod(VALUE_METHOD_NAME)
            val `object` = method.invoke(annotation)
            listOf(ObjectUtils.toString(`object`))
        } catch (e: NoSuchMethodException) {
            printError(
                "Invalid annotation $annotation: marker annotations without value should contains value() method",
                e
            )
            emptyList()
        } catch (e: IllegalAccessException) {
            printError(
                "Invalid annotation $annotation: marker annotations without value should contains value() method",
                e
            )
            emptyList()
        } catch (e: InvocationTargetException) {
            printError(
                "Invalid annotation $annotation: marker annotations without value should contains value() method",
                e
            )
            emptyList()
        }
    }


    private fun extractRepeatable(annotation: Annotation): List<Annotation> {
        return if (isRepeatableWrapper(annotation)) {
            try {
                val method: Method = annotation.annotationType().getMethod(VALUE_METHOD_NAME)
                (method.invoke(annotation) as Array<Annotation>).toList()
            } catch (e: NoSuchMethodException) {
                printError("Could not extract repeatable annotation $annotation")
                emptyList<Annotation>()
            } catch (e: IllegalAccessException) {
                printError("Could not extract repeatable annotation $annotation")
                emptyList<Annotation>()
            } catch (e: InvocationTargetException) {
                printError("Could not extract repeatable annotation $annotation")
                emptyList<Annotation>()
            }
        } else listOf(annotation)
    }

    private fun isRepeatableWrapper(annotation: Annotation): Boolean {
        return annotation.annotationType().declaredMethods.asSequence()
            .filter { method: Method ->
                VALUE_METHOD_NAME.equals(method.name, ignoreCase = true)
            }
            .filter { method: Method -> method.returnType.isArray }
            .any { method: Method -> isRepeatable(method.returnType.componentType) }
    }

    private fun isRepeatable(annotationType: Class<*>): Boolean {
        return annotationType.isAnnotationPresent(Repeatable::class.java)
    }

    private fun isInJavaLangAnnotationPackage(annotationType: Class<out Annotation>?): Boolean {
        return annotationType != null && annotationType.name.startsWith("java.lang.annotation")
    }
}

//todo use Kotlin annotationClass?
fun <T : Annotation> T.annotationType(): Class<out T> =
    (this as java.lang.annotation.Annotation).annotationType() as Class<out T>