description = "Allure Kotlin JUnit 4 Integration"

//TODO extract constants
dependencies {
    api(project(":allure-kotlin-commons"))
    implementation("junit:junit:4.12")
    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation(project(":allure-kotlin-commons-test"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

tasks.test {
    useJUnitPlatform()
}