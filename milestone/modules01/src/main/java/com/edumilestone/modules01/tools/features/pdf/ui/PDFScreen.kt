package com.eMilestone.modules01.tools.features.pdf.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.util.Log

@Composable
fun PDFScreen(navController: NavController) {
    // Log the initial state
    Log.i("PDFScreen", "PDFScreen composable started")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light background color
            .padding(16.dp), // Padding around the screen
        horizontalAlignment = Alignment.CenterHorizontally, // Center align items horizontally
        verticalArrangement = Arrangement.Center // Center align items vertically
    ) {
        // Icon for the PDF feature
        Icon(
            imageVector = Icons.Filled.Description,
            contentDescription = "PDF Icon",
            tint = Color(0xFF6200EE), // Primary color for the icon
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 16.dp)
        )

        // Header text for the PDF feature
        Text(
            text = "PDF Viewer",
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE), // Primary color for the header
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Description text for the PDF feature
        Text(
            text = "Upcoming Features Under Tools_Section",
            color = Color(0xFF757575), // Secondary color for the description
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Additional description text
        Text(
            text = "Development in progress",
            color = Color(0xFF757575), // Secondary color for the description
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Button to navigate back to the Home Screen
        Button(
            onClick = {
                Log.i("PDFScreen", "Back to Home button clicked")
                navController.navigate("home_screen") {
                    popUpTo("home_screen") { inclusive = true }
                }
            },
            shape = RoundedCornerShape(8.dp), // Rounded corners for the button
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)), // Primary color for the button
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp) // Padding around the button
        ) {
            Text(
                text = "Back to Home",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}