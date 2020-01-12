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
    }

    dependencies {
        implementation(kotlin("stdlib"))
        testImplementation("junit", "junit", "4.12")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_6
        targetCompatibility = JavaVersion.VERSION_1_6
    }
}