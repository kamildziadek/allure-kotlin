repositories {
    mavenCentral()
    jcenter()
}

plugins {
    java
    kotlin("jvm") version "1.3.61"
}

configure(subprojects) {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }
    group = "io.qameta.allure"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx")
    }

    dependencies {
        implementation(kotlin("stdlib"))
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_6
        targetCompatibility = JavaVersion.VERSION_1_6
    }

    tasks.test {
        systemProperty("allure.model.indentOutput", "true")
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    tasks.processTestResources {
        filesMatching("**/allure.properties") {
            filter {
                it.replace("#project.description#", project.description ?: project.name)
            }
        }
    }
}