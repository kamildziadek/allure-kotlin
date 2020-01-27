# Allure Kotlin Integrations

The repository contains Allure2 adaptors for JVM-based test frameworks targeting Kotlin and Java with 1.6 source compatibility. The core of the `allure-java` library was ported to Kotlin. On top of the core library support for Kotlin and Android test frameworks were added. 

## How does it work?

Allure libraries generate a set of JSON/XML files that can then be used to generate the report via Allure CLI (see the [Allure Documentation][allure-cli]).

Allure libraries are implemented as a **test listener** for different test engines. All you need to do is attach the Allure test listener, which is appropriate to your test engine. This can be done in numerous ways and differs based on the test framework. Listener will hook into the test framework, analyze annotations and then generate JSON/XML files in a specified directory.

Check out the [Allure Documentation][allure-docs] to get to know all of the Allure features.

## Supported frameworks
* JUnit4 
* Android Robolectric (via AndroidX Test)
* Android Instrumentation (via AndroidX Test)

## How to?

There are many examples of Allure integration across the Internet ([the official one][allure-examples]), where you can check out examples of different integrations. As `allure-kotlin` has the exact same API as `allure-java`, you just have to use different dependency and it will work correctly (although you can't use Allure Gradle Plugin). Take a look at the [docs][allure-cli].

Moreover, in `samples` directory you can find different examples of `allure-kotlin` usage. This includes:
- `junit4-android` - complete Android sample with unit tests, robolectric tests & on device instrumentation tests

### JUnit4 Android Integration

#### Dependencies

```gradle
testImplementation "io.qameta.allure:allure-kotlin-model:$LATEST_VERSION"
testImplementation "io.qameta.allure:allure-kotlin-commons:$LATEST_VERSION"
testImplementation "io.qameta.allure:allure-kotlin-junit4:$LATEST_VERSION"
testImplementation "io.qameta.allure:allure-kotlin-android:$LATEST_VERSION"
```

#### Unit tests

For unit tests use an appropriate test runner (`AllureRunner`/`AllureParametrizedRunner`). Test report will be included by default in `allure-results` directory, but this can be change in configuration files ([docs][allure-configuration]).

```kotlin
@RunWith(AllureRunner::class)
class MyUnitTest {
    ...
}
```

#### Android tests

AndroidX Test introduced a new `AndroidJUnit4` class runner that can be used for both **Robolectric** and **on-device instrumentation tests**. The same pattern is used for `AllureAndroidJUnit4` class runner. It attaches the allure listener to current class runner, but under the hood it uses `AndroidJUnit4`. All you need to do is add `@RunWith(AllureAndroidJUnit4::class)` annotation to yor test. 

```kotlin
@RunWith(AllureAndroidJUnit4::class)
class MyInstrumentationTest {
    ...
}
```

#### Robolectric tests

Robolectric tests are simple unit tests, hence the API is the same. The report data will be placed in the same place as for unit tests. 

#### On-device instrumentation tests

##### Integration
As on-device instrumentation test run on an actual device, the results have to be saved there as well. To do so permissions for accessing external storage are needed. If your app doesn't have those permissions, you can include them only in your debug build type (or any other build type under which the tests are executed):

**src/debug/AndroidManifest.xml**
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="io.qameta.allure.sample.junit4.android">

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
</manifest>
```

Moreover, Allure will grant itself those permissions at runtime, so you don't have to place any special logic for Android 6.0+ devices. 

After the tests are finished you can pull the results from the external storage using an **adb** command like this one:
```
adb pull /sdcard/allure-results
```

##### Features

The Allure Android API includes couple of features to make your reports a bit better.

###### Screenshot attachment

Screenshot can be taken and appended as an attachment to step or test in which they were executed:
```kotlin
@Test
fun screenshotExample() {
    step("Step screenshot") {
        Allure.screenshot(name = "ss_step", quality = 90)
    }
    Allure.screenshot(name = "ss_test", quality = 50)
}
```

###### Screenshot rule

Test rule to make the screenshot after each test and attach it to the test report. It includes a `mode` parameter which decides for which tests to make a screenshot:
* SUCCESS - only successful tests
* FAILURE - only failed tests
* END - all tests

```kotlin
@get:Rule
val logcatRule = ScreenshotRule(mode = ScreenshotRule.Mode.END, screenshotName = "ss_end")
```

###### Logcat rule

Test rule that clears the logcat before each test and appends the log dump as an attachment in case of failure.

```kotlin
@get:Rule
val logcatRule = LogcatRule()
```

### JUnit4

Gradle doesn't support attaching test listeners but there is a way to do it using [JUnit Foundation][junit-foundation] ([example][gradle-test-listener]). In case you don't want to or can't use it, there is an alternative. All you have to do is to implement a simple class test runner with the attached listener and then use this runner via `@RunWith` annotation. Take a look at this example:

```kotlin
//Custom Runner
class AllureRunner(clazz: Class<*>) : BlockJUnit4ClassRunner(clazz) {

    override fun run(notifier: RunNotifier) {
        notifier.addListener(AllureJunit4())
        super.run(notifier)
    }
}
//Usage
@RunWith(AllureRunner::class)
class MyTest {
    ...
}
```

Basic and parametrized runners with attached `AllureJunit4` listener are included in the `allure-kotlin-junit4` library.

## Why allure-kotlin?

The main reason for the creation of `allure-kotlin` was Android support for unit tests, Robolectric tests, and on-device instrumentation tests. 

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
* `java.nio.file.*` (Java 1.7+) -> migrating form `Path` to `File`
* repeatable annotations (Java 1.8+) -> no alternatives, feature not supported by JVM 1.6 

*The only part that was not migrated is aspects support.*



[allure-examples]: https://github.com/allure-examples
[gradle-test-listener]: https://discuss.gradle.org/t/how-to-attach-a-runlistener-to-your-junit-4-tests-in-gradle/30788
[junit-foundation]: https://github.com/Nordstrom/JUnit-Foundation
[allure-docs]: https://docs.qameta.io/allure/
[allure-cli]: https://docs.qameta.io/allure/#_reporting
[allure-configuration]: https://docs.qameta.io/allure/#_configuration