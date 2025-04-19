package com.eMilestone.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.eMilestone.app.ui.HomeScreen
import com.eMilestone.app.ui.theme.eMilestoneTheme
import androidx.compose.runtime.DisposableEffect
import com.eMilestone.app.navigation.AppNavigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eMilestone.modules01.tools.features.ocr.ui.OCRScreen
import com.eMilestone.modules01.tools.features.pdf.ui.PDFScreen
import com.eMilestone.modules01.tools.features.word.ui.WordScreen
import androidx.activity.compose.BackHandler
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    // Declare AppNavigation instance to manage cleanup in the Activity lifecycle
    private val appNavigation = AppNavigation()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        setContent {
            EducationMilestoneApp(appNavigation = appNavigation, finishActivity = {
                Log.d("MainActivity", "finishActivity called")
                finish() // Pass appNavigation and finishActivity to the composable
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ensure cleanup when the Activity is destroyed
        Log.i("MainActivity", "onDestroy called")
        appNavigation.cleanup()  // Call cleanup on appNavigation when composable is disposed
    }
}

@Composable
fun EducationMilestoneApp(appNavigation: AppNavigation, finishActivity: () -> Unit) {
    val navController = rememberNavController() // Initialize NavController to handle navigation
    Log.d("eMilestoneApp", "NavController initialized")
    // Pass AppNavigation instance to manage cleanup when the composable is disposed
    DisposableEffect(Unit) {
        // Cleanup when the composable is disposed of
        Log.i("eMilestoneApp", "DisposableEffect started")
        onDispose {
            Log.i("eMilestoneApp", "DisposableEffect disposed")
            appNavigation.cleanup()  // Call cleanup on appNavigation when composable is disposed
        }
    }

    eMilestoneTheme { // Apply the theme for the app
        // NavHost to handle navigation for different screens
        NavHost(navController = navController, startDestination = "home_screen") {
            composable("home_screen") {
                Log.d("eMilestoneApp", "Navigated to HomeScreen")
                HomeScreen(navController = navController, appNavigation = appNavigation)
                // Handle back press on the Home screen
                BackHandler {
                    // You can add logic here if you want to control the back key behavior
                    // For example, exit app or handle any custom logic
                    // Uncomment below to exit the app when back is pressed from home screen
                    Log.d("eMilestoneApp", "BackHandler in HomeScreen")
                    finishActivity() // Finish the activity when back is pressed from home screen
                }
            }
            composable("feature_screen/OCR") {
                Log.d("eMilestoneApp", "Navigated to OCRScreen")
                OCRScreen(navController = navController) // Show OCRScreen when navigated to the OCR route
                // Handle back press on the OCR feature screen
                BackHandler {
                    // Unload the module before navigating back
                    Log.d("eMilestoneApp", "BackHandler in OCRScreen")
                    appNavigation.unloadModuleForFeature("OCR")
                    navController.popBackStack("home_screen", inclusive = false) // Navigates back to the home screen
                }
            }
            composable("feature_screen/PDF") {
                Log.d("eMilestoneApp", "Navigated to PDFScreen")
                PDFScreen(navController = navController) // Pass NavController to PDFScreen
                // Handle back press on the PDF feature screen
                BackHandler {
                    Log.d("eMilestoneApp", "BackHandler in PDFScreen")
                    // Unload the module before navigating back
                    appNavigation.unloadModuleForFeature("PDF")
                    navController.popBackStack("home_screen", inclusive = false) // Navigates back to the home screen
                }
            }
            composable("feature_screen/WORD") {
                Log.d("eMilestoneApp", "Navigated to WordScreen")
                WordScreen(navController = navController) // Show WordScreen when navigated to the WORD route
                // Handle back press on the Word feature screen
                BackHandler {
                    Log.d("eMilestoneApp", "BackHandler in WordScreen")
                    // Unload the module before navigating back
                    appNavigation.unloadModuleForFeature("WORD")
                    navController.popBackStack("home_screen", inclusive = false) // Navigates back to the home screen
                }
            }
        }
    }
}
