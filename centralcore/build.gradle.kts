// ✅ Apply necessary plugins for an Android Library module
plugins {
    alias(libs.plugins.android.library)    // Plugin for Android libraries: Configures the project as an Android library, allowing it to be used as a dependency in other Android apps or libraries.
    alias(libs.plugins.kotlin.android)     // Plugin for Kotlin support: Adds Kotlin support to the project, enabling Kotlin language features.
    alias(libs.plugins.kotlin.ksp)         // Kotlin Symbol Processing (KSP) for annotation processing
    alias(libs.plugins.hilt.android)       // Hilt for Dependency Injection
}

android {
    namespace = "com.eMilestone.centralcore" // ✅ Unique package namespace for Central Core module: The package name of the library, used to uniquely identify this module.
    compileSdk = 35                       // ✅ Target latest stable SDK: Specifies the API level used to compile the library (API level 35).

    defaultConfig {
        minSdk = 24                       // ✅ Minimum supported Android version: Specifies the lowest API level that the library supports (API level 24).
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Specifies the test runner for Android instrumentation tests.
        consumerProguardFiles("consumer-rules.pro") // ✅ ProGuard rules for consumer modules: Provides ProGuard rules for any consuming modules that use this library.
    }

    buildTypes {
        release {
            isMinifyEnabled = true        // ✅ Enable ProGuard & R8 shrinking for smaller APKs: This enables ProGuard and R8 to shrink the code in the release build, optimizing the APK size.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),  // Default ProGuard configuration file for optimizing the release build.
                "proguard-rules.pro"                                       // Custom ProGuard rules specific to the library.
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  // ✅ Use Java 17 for better performance: Ensures that the code is compiled with Java 17 for improved performance and modern features.
        targetCompatibility = JavaVersion.VERSION_17  // Ensures the generated code is compatible with Java 17.
    }

    kotlinOptions {
        jvmTarget = "17"  // ✅ Kotlin JVM target set to 17: Specifies that Kotlin will be compiled for JVM 17, taking advantage of the features and performance improvements of Java 17.
    }

    buildFeatures {
        viewBinding = true  // ✅ Enable ViewBinding (if using XML-based UI components): Enables ViewBinding for easy access to views in layouts, reducing boilerplate code.
    }
}

dependencies {
    // ✅ Core Android Libraries
    implementation(libs.androidx.core.ktx)  // Android KTX extensions: Provides Kotlin extensions for Android APIs, making the code more concise and easier to work with.
    implementation(libs.androidx.appcompat)  // Support for older Android versions: Provides backward-compatible support for modern UI components, ensuring compatibility with older devices.

    implementation(libs.material)  // ✅ Fix: Use correct Material Components dependency: Adds Material Components for UI elements following Material Design guidelines.

    // ✅ Testing Dependencies
    testImplementation(libs.junit)  // ✅ Unit Testing: Adds JUnit for unit testing in the library module.
    androidTestImplementation(libs.androidx.junit)  // JUnit for Android instrumentation tests: Adds JUnit support for Android-specific tests.
    androidTestImplementation(libs.androidx.espresso.core)  // Espresso for UI testing in Android: Adds Espresso for testing the UI interactions in Android apps.

    // ✅ Room Dependencies (Use KSP instead of KAPT)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)  // ✅ Use KSP for Room annotation processor
    implementation(libs.room.ktx)  // ✅ Room KTX for Kotlin coroutines

    // ✅ Hilt Dependencies (Make Sure You Are Using KSP Instead of KAPT)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)  // ✅ Hilt annotation processor
}
