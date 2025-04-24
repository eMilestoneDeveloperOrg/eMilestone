plugins {
    alias(libs.plugins.android.application)      // Android Application Plugin: Used to define the Android application project type and build settings.
    alias(libs.plugins.kotlin.android)           // Kotlin Android Plugin: Adds Kotlin support for Android, enabling Kotlin language features in the app.
    alias(libs.plugins.kotlin.compose)           // Kotlin Compose Plugin: Enables Jetpack Compose for UI in the Kotlin environment.
    alias(libs.plugins.kotlin.ksp)               // Kotlin Symbol Processing (KSP) for annotation processing
    alias(libs.plugins.hilt.android)             // Hilt for Dependency Injection
}

android {
    namespace = "com.eMilestone.app"  // Application namespace: This is the package name of the application, used to uniquely identify the app.
    compileSdk = 35                       // Compile SDK version: Specifies the API level that the app will be compiled against (API level 35).

    defaultConfig {
        applicationId = "com.eMilestone.app"  // Application ID: Unique identifier for the app, used to distinguish the app on the Google Play Store and in the system.
        minSdk = 24                             // Minimum SDK version: Specifies the lowest API level that the app supports (API level 24).
        targetSdk = 35                          // Target SDK version: The API level that the app is optimized to run against (API level 35).
        versionCode = 1                         // Version code: Integer value representing the version of the app; increments with each release.
        versionName = "1.0"                     // Version name: String representing the version name of the app for users (e.g., "1.0").

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"  // Test runner: Defines the test runner to be used for running instrumentation tests.
    }

    buildTypes {
        release {
            isMinifyEnabled = false          // Minify enabled: Disables code shrinking (minification) for the release build. Set to true for smaller APKs.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),  // Default ProGuard configuration for optimizing the release build.
                "proguard-rules.pro"                                       // Custom ProGuard rules file.
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  // Source compatibility: Specifies the version of Java the source code is written in (Java 17).
        targetCompatibility = JavaVersion.VERSION_17  // Target compatibility: Specifies the version of Java that the app will be compiled to run on (Java 17).
    }

    kotlinOptions {
        jvmTarget = "17"  // JVM Target: Specifies the target JVM version for the Kotlin code (Java 17 in this case).
    }

    buildFeatures {
        compose = true  // Enable Jetpack Compose: Enables the use of Jetpack Compose for building UIs in the app.
    }
}

dependencies {
    // Core dependencies for AndroidX and Kotlin
    implementation(libs.androidx.core.ktx)                // Core Kotlin extensions for AndroidX.
    implementation(libs.androidx.lifecycle.runtime.ktx)    // AndroidX Lifecycle KTX for lifecycle-aware components.
    implementation(libs.androidx.activity.compose)         // Activity integration for Jetpack Compose.
    implementation(platform(libs.androidx.compose.bom))    // Jetpack Compose BOM (Bill of Materials) to ensure all Compose dependencies are compatible.
    implementation(libs.androidx.ui)                       // Jetpack Compose UI module.
    implementation(libs.androidx.ui.graphics)              // Jetpack Compose Graphics module for handling UI graphics.
    implementation(libs.androidx.ui.tooling.preview)       // Jetpack Compose Tooling Preview for UI design-time previews.
    implementation(libs.androidx.material3)                // Jetpack Compose Material3 components.
    implementation(libs.androidx.activity.ktx)             // Kotlin extensions for Activity in AndroidX.

    // Testing dependencies
    testImplementation(libs.junit)                         // JUnit dependency for unit testing.
    androidTestImplementation(libs.androidx.junit)         // JUnit for Android instrumentation tests.
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing in Android.
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Ensure Compose dependencies are compatible in tests.
    androidTestImplementation(libs.androidx.ui.test.junit4) // JUnit 4 support for Compose UI testing.

    // Debugging dependencies
    debugImplementation(libs.androidx.ui.tooling)         // Debug tooling for Compose UI, helps with debugging UI issues.
    debugImplementation(libs.androidx.ui.test.manifest)   // UI testing manifest for Compose-based UI testing in debug mode.

    // Custom project dependencies
    implementation(project(":centralcore"))                // Link to the Central Core module, providing shared utilities and services.
    implementation(libs.androidx.navigation.compose)       // Navigation component for Jetpack Compose, used for handling screen navigation.
    implementation(project(":milestone:modules01"))         // Link to the Milestone Modules 01, where specific modules like OCR are handled.

    // ✅ Room Dependencies (Use KSP instead of KAPT)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)  // ✅ Use KSP for Room annotation processor
    implementation(libs.room.ktx)  // ✅ Room KTX for Kotlin coroutines

    // ✅ Hilt Dependencies (Make Sure You Are Using KSP Instead of KAPT)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)  // ✅ Hilt annotation processor
}