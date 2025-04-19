// ✅ Apply necessary plugins for an Android Library module
plugins {
    alias(libs.plugins.android.library)    // Android Library plugin: Used to define an Android library module and configure the build settings.
    alias(libs.plugins.kotlin.android)     // Kotlin Android plugin: Enables Kotlin language support for Android.
    alias(libs.plugins.kotlin.compose)     // Kotlin Compose plugin: Enables Jetpack Compose for UI within Kotlin-based Android libraries.
    alias(libs.plugins.kotlin.ksp)         // Kotlin Symbol Processing (KSP) for annotation processing
    alias(libs.plugins.hilt.android)       // Hilt for Dependency Injection
}

android {
    namespace = "com.eMilestone.modules01" // ✅ Correct package name: The unique namespace for the library module to distinguish it from other modules.
    compileSdk = 35                       // ✅ Target latest stable SDK: Specifies the API level used to compile the app (API level 35).

    defaultConfig {
        minSdk = 24                       // ✅ Minimum supported Android version: Specifies the lowest API level that the library supports (API level 24).

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"  // Specifies the test runner for Android instrumentation tests.
        consumerProguardFiles("consumer-rules.pro") // ✅ ProGuard rules for consumer modules: ProGuard rules for modules consuming this library.
    }

    buildTypes {
        release {
            isMinifyEnabled = false      // ✅ Disable ProGuard minification for now: This disables code shrinking (minification) for the release build. Set to `true` for reducing APK size.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),  // Default ProGuard configuration for optimizing the release build.
                "proguard-rules.pro"                                       // Custom ProGuard rules file for the library.
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  // Source compatibility: Specifies the version of Java the source code is written in (Java 17).
        targetCompatibility = JavaVersion.VERSION_17  // Target compatibility: Specifies the version of Java that the app will be compiled to run on (Java 17).
    }

    kotlinOptions {
        jvmTarget = "17"  // ✅ Use Java 11 for better performance: Sets the target JVM version for Kotlin code to Java 11 for better performance and compatibility.
    }

    // ✅ Enable Jetpack Compose
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"  // ✅ Specify Compose compiler version: Defines the Jetpack Compose compiler version to ensure compatibility.
    }

    buildFeatures {
        compose = true  // ✅ Enable Jetpack Compose UI framework: Enables Jetpack Compose support for building UIs in the library.
    }
}

dependencies {
    // ✅ Core AndroidX Dependencies
    implementation(libs.androidx.core.ktx)  // Core Kotlin extensions for AndroidX.
    implementation(libs.androidx.appcompat)  // Provides backward-compatible support for ActionBar and modern UI components.
    implementation(libs.androidx.multidex)  // Multidex support for handling large APKs on older devices.

    // ✅ Jetpack Compose Dependencies (Using BOM)
    implementation(platform(libs.androidx.compose.bom))  // Jetpack Compose BOM (Bill of Materials): Ensures that all Compose dependencies are compatible.
    implementation(libs.androidx.ui)  // Jetpack Compose UI module for creating the app’s user interface.
    implementation(libs.androidx.ui.graphics)  // Jetpack Compose Graphics module for handling UI-related graphics.
    implementation(libs.androidx.ui.tooling.preview)  // Jetpack Compose Tooling Preview for UI design-time previews.

    // ✅ Correct Material3 for Jetpack Compose
    implementation(libs.androidx.material3)  // Jetpack Compose Material3 components: Provides the latest Material Design components for Compose.

    // ✅ Jetpack Compose Navigation
    implementation(libs.androidx.navigation.compose)  // Jetpack Compose Navigation: Helps in managing navigation between Compose-based screens.

    // ✅ Tooling for Debugging (Only in Debug Builds)
    debugImplementation(libs.androidx.ui.tooling)  // Debug tooling for Compose UI: Enables debugging features in Compose-based UIs.

    // ✅ Link Central Core to Module 01
    implementation(project(":centralcore"))  // Links the `central core` project/module, which provides shared utilities and services, to this module.

    // ✅ Testing Dependencies
    testImplementation(libs.junit)  // JUnit for unit testing.
    androidTestImplementation(libs.androidx.junit)  // Android JUnit for instrumentation testing in Android.
    androidTestImplementation(libs.androidx.espresso.core)  // Espresso for UI testing in Android.

    // ✅ Material Icons Extended for Jetpack Compose using version catalog
    implementation(libs.compose.material.icons.extended)  // Material Icons Extended for Compose: Adds extended icons for Material Design in Compose-based UIs.

    // ✅ Room Dependencies (Use KSP instead of KAPT)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)  // ✅ Use KSP for Room annotation processor
    implementation(libs.room.ktx)  // ✅ Room KTX for Kotlin coroutines

    // ✅ Hilt Dependencies (Make Sure You Are Using KSP Instead of KAPT)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)  // ✅ Hilt annotation processor
}
