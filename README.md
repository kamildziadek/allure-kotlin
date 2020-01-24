# Allure Kotlin Integrations 

The repository contains Allure2 adaptors for JVM-based test frameworks targeting Kotlin and Java 1.6 source compatibility. The core of the `allure-java` library was ported to Kotlin. On top of the core library support for Kotlin and Android test frameworks were added.

## Supported frameworks
* JUnit4 
* Android Robolectric (via AndroidX Test)
* Android Instrumentation (via AndroidX Test)

## Why allure-kotlin?

The main reason for the creation of `allure-kotlin` was Android support for unit tests, instrumentation tests, and end-to-end tests. 

Why not `allure-java`? Allure Java targets Java 1.8 which can't be used on Android. Due to having to be backward compatible with older devices, Android developers are limited to Java 1.6/1.7 with some support for Java 1.8 features. Unfortunately, it doesn't include things like `Stream`, `Optional` or `MethodHandle.invoke`. Because of that `allure-java` can't be used there.


Why not `allure-android`? The official Allure Android adapter implementation is limited and it doesn't support all of the features of Allure. Its drawbacks are following:
* no support for all annotations e.g. `@Description`, `@LinkAnnotations`
* limited API when comparing to `allure-java`
* poor test coverage
* no maintenance
* tight coupling with Android Instrumentation tests so it can't be easily reused for unit tests or Robolectric tests

Due to the mentioned limitations `allure-kotlin` was born as a core for different Kotlin and Android test frameworks.

## Connection with allure-java

The core of this library was ported from `allure-java` in order to achieve compatibility with Java 1.6 and Android API 21. Thanks to that `allure-kotlin` has the same features, test coverage and solutions as `allure-java`. Following modules have been migrated:

* `allure-model` -> `allure-kotlin-model`
* `allure-java-commons` -> `allure-kotlin-commons`
* `allure-java-commons-test` -> `allure-kotlin-commons-test`

Following changes have to be made in order to keep the compatibility with Java 1.6: 
* `java.util.Optional` (Java 1.8+) -> Kotlin null type & safe call operators
* `java.util.stream.*` (Java 1.8+) -> Kotlin collection operators
* `java.nio.file.*` (Java 1.7+) -> migrating form `Path` to `String`
* repeatable annotations (Java 1.8+) -> no alternatives, feature not supported by JVM 1.6 

*The only part that was not migrated is aspects support.*



