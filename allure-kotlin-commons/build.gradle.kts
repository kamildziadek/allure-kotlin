description = "Allure Kotlin Commons"

//TODO extract constants
dependencies {
    api(project(":allure-kotlin-model"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kontlinxSerialization}")
    testImplementation("io.github.benas:random-beans:3.8.0")
    testImplementation("io.github.glytching:junit-extensions:2.3.0")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.5.2")
    testImplementation("org.mockito:mockito-core:2.24.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

tasks.test {
    useJUnitPlatform()
}
