pluginManagement {
    repositories {
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://dl.bintray.com/kotlin/kotlin-dev")
        maven(url = "https://dl.bintray.com/kotlin/kotlin-eap")
        maven(url = "https://plugins.gradle.org/m2/")
    }

    resolutionStrategy.eachPlugin {
        if (requested.id.id == "org.jetbrains.kotlin.jvm") {
            useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        }
        if (requested.id.id == "kotlinx-serialization") {
            useModule("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")
        }
    }

}

rootProject.name = "allure-kotlin"
include("allure-kotlin-model")
include("allure-kotlin-commons")
include("allure-kotlin-commons-test")
include("allure-kotlin-junit4")
