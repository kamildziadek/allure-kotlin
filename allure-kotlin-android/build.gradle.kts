plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(Versions.Android.compileSdk)
    defaultConfig {
        minSdkVersion(Versions.Android.minSdk)
        targetSdkVersion(Versions.Android.targetSdk)
        versionCode = 1
        versionName = version as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {

        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(project(":allure-kotlin-junit4"))
    implementation(kotlin("stdlib-jdk7", Versions.kotlin))
    implementation("androidx.test.ext:junit:${Versions.Android.Test.junit}")
    implementation("androidx.test:runner:${Versions.Android.Test.runner}")
    testImplementation("junit:junit:${Versions.junit4}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.Android.Test.expresso}")
}
