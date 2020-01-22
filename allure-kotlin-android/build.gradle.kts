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
    }

    sourceSets {
        val sharedTestDir = "src/sharedTest/java"
        getByName("test").java.srcDir(sharedTestDir)
        getByName("androidTest").java.srcDir(sharedTestDir)
    }

    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    api(project(":allure-kotlin-junit4"))
    implementation(kotlin("stdlib-jdk7", Versions.kotlin))
    implementation("androidx.test.ext:junit:${Versions.Android.Test.junit}")
    implementation("androidx.test:runner:${Versions.Android.Test.runner}")

    debugImplementation("androidx.appcompat:appcompat:${Versions.Android.androidX}")
    debugImplementation("androidx.core:core-ktx:${Versions.Android.androidX}")

    testImplementation("org.robolectric:robolectric:${Versions.Android.Test.robolectric}")
    testImplementation("junit:junit:${Versions.junit4}")
    testImplementation("androidx.test.espresso:espresso-core:${Versions.Android.Test.espresso}")

    androidTestImplementation("junit:junit:${Versions.junit4}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Versions.Android.Test.espresso}")
}
