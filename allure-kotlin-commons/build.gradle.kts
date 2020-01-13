description = "Allure Kotlin Commons"

dependencies {
    api(project(":allure-kotlin-model"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kontlinxSerialization}")
}
