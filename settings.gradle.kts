// =======================================
// 🔧 Plugin Management Configuration
// =======================================
// Defines repositories where Gradle searches for plugins like Hilt, Kotlin, Android Gradle Plugin, etc.

pluginManagement {
    repositories {
        google {
            content {
                // ✅ Includes Android and Google-related dependencies only
                // This prevents unnecessary repository lookups for unrelated dependencies
                includeGroupByRegex("com\\.android.*") // Android libraries (AGP, Jetpack, etc.)
                includeGroupByRegex("com\\.google.*") // Google libraries (Firebase, Play Services, etc.)
                includeGroupByRegex("androidx.*") // AndroidX libraries (Jetpack components, UI, etc.)
            }
        }
        mavenCentral() // ✅ Central repository for Java/Kotlin libraries (Retrofit, Room, Coroutines)
        gradlePluginPortal() // ✅ Repository for Gradle-specific plugins (Kotlin Gradle Plugin, Hilt, etc.)
    }
}

// =======================================
// 📦 Dependency Resolution Management
// =======================================
// Ensures Gradle fetches dependencies from the correct sources.

dependencyResolutionManagement {
    repositories {
        google() // ✅ Official Google repository for Android dependencies
        mavenCentral() // ✅ Official Maven repository for general Java/Kotlin dependencies
    }
}

// =======================================
// 📂 Root Project Configuration
// =======================================
// Sets the **root project name** for better organization.

rootProject.name = "eMilestone" // ✅ The main project name (visible in Android Studio and Gradle tasks)

// =======================================
// 📂 Including Project Modules
// =======================================
// Specifies submodules that make up the project.

include(":app")                  // ✅ Main application module (UI, navigation, business logic)
include(":centralcore")           // ✅ Core module (Authentication, Networking, Shared Utilities)
include(":milestone:modules01")   // ✅ Milestone Modules (OCR, PDF Viewer, Word Processing, etc.)

// =======================================
// 📌 Custom Directory Structure for Modules
// =======================================
// Maps submodules to their actual directories.

project(":milestone:modules01").projectDir = file("milestone/modules01")
