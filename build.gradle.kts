plugins {
    kotlin("jvm") version "1.3.61"
}

configure(subprojects) {
    group = "io.qameta.allure"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(kotlin("stdlib"))
    }

}