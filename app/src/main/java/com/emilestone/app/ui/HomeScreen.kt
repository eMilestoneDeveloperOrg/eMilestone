package com.eMilestone.app.ui

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eMilestone.app.navigation.AppNavigation
import kotlinx.coroutines.delay

/***
 * Composable function for the HomeScreen.
 * Displays the main UI including animated text, tool buttons, and a menu.
 * @param navController Used for navigation between screens.
 * @param appNavigation Handles feature requests and navigation actions.
 */
@Composable
fun HomeScreen(navController: NavController, appNavigation: AppNavigation) {
    Log.d("HomeScreen", "HomeScreen composable started")

    var menuExpanded by remember { mutableStateOf(false) }
    var toolsExpanded by remember { mutableStateOf(false) }
    val menuWidth by animateDpAsState(targetValue = if (menuExpanded) 200.dp else 0.dp)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            AnimatedAlphabetText(
                text = "eMilestone",
                colors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5), Color(0xFFFFC107)),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Welcome!!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    menuExpanded = !menuExpanded
                    Log.d("HomeScreen", "Menu toggled: $menuExpanded")
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ToolButton("OCR", Color(0xFFA5D6A7)) {
                    Log.d("HomeScreen", "OCR button clicked")
                    appNavigation.handleFeatureRequest(navController, "OCR")
                }
                ToolButton("PDF", Color(0xFFFFCDD2)) {
                    Log.d("HomeScreen", "PDF button clicked")
                    appNavigation.handleFeatureRequest(navController, "PDF")
                }
                ToolButton("WORD", Color(0xFF81D4FA)) {
                    Log.d("HomeScreen", "WORD button clicked")
                    appNavigation.handleFeatureRequest(navController, "WORD")
                }
            }
        }

        if (menuExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { menuExpanded = false }
            ) {
                Column(
                    modifier = Modifier
                        .width(menuWidth)
                        .fillMaxHeight()
                        .background(Color(0xFF6200EE))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Menu",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    MenuItem("Settings") { Log.d("HomeScreen", "Settings menu item clicked") }
                    MenuItem("Profile") { Log.d("HomeScreen", "Profile menu item clicked") }
                    MenuItem("Module 01: Tools") {
                        Log.d("HomeScreen", "Module 01: Tools menu item clicked")
                        toolsExpanded = !toolsExpanded
                    }
                    if (toolsExpanded) {
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            MenuItem("OCR") {
                                Log.d("HomeScreen", "OCR menu item clicked")
                                appNavigation.handleFeatureRequest(navController, "OCR")
                                menuExpanded = false
                            }
                            MenuItem("PDF") {
                                Log.d("HomeScreen", "PDF menu item clicked")
                                appNavigation.handleFeatureRequest(navController, "PDF")
                                menuExpanded = false
                            }
                            MenuItem("WORD") {
                                Log.d("HomeScreen", "WORD menu item clicked")
                                appNavigation.handleFeatureRequest(navController, "WORD")
                                menuExpanded = false
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Milestone Module",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "V.1.0",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            FloatingActionButton(
                onClick = {
                    Log.d("HomeScreen", "OCR FAB clicked")
                    appNavigation.handleFeatureRequest(navController, "OCR")
                },
                modifier = Modifier.padding(bottom = 16.dp),
                containerColor = Color(0xFFA5D6A7)
            ) {
                Text("OCR", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            FloatingActionButton(
                onClick = {
                    Log.d("HomeScreen", "PDF FAB clicked")
                    appNavigation.handleFeatureRequest(navController, "PDF")
                },
                modifier = Modifier.padding(bottom = 16.dp),
                containerColor = Color(0xFFFFCDD2)
            ) {
                Text("PDF", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            FloatingActionButton(
                onClick = {
                    Log.d("HomeScreen", "WORD FAB clicked")
                    appNavigation.handleFeatureRequest(navController, "WORD")
                },
                containerColor = Color(0xFF81D4FA)
            ) {
                Text("WORD", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/***
 * Composable function to display animated text with changing colors and rotation.
 * @param text The text to display.
 * @param colors List of colors for the gradient animation.
 * @param modifier Modifier for customizing the layout.
 * @param fontSize Size of the text.
 * @param fontStyle Style of the text (optional).
 * @param fontWeight Weight of the text.
 * @param fontFamily Font family of the text.
 */
@Composable
fun AnimatedAlphabetText(
    text: String,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    fontSize: TextUnit,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight,
    fontFamily: FontFamily
) {
    var currentColorIndex by remember { mutableIntStateOf(0) }
    val gradient by animateColorAsState(
        targetValue = colors[currentColorIndex],
        animationSpec = tween(durationMillis = 2000) // 2 seconds duration
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(9000) // Stable for 9 seconds
            currentColorIndex = (currentColorIndex + 1) % colors.size
            delay(6000) // Change color and rotation for 6 seconds
        }
    }

    Row(modifier = modifier) {
        text.forEachIndexed { index, char ->
            val rotation by animateFloatAsState(
                targetValue = if (currentColorIndex % 2 == 0) 0f else if (index % 2 == 0) 360f else -360f,
                animationSpec = tween(durationMillis = 2000) // 2 seconds duration
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(brush = Brush.linearGradient(colors = listOf(gradient, colors[(currentColorIndex + 1) % colors.size])))) {
                        append(char.toString())
                    }
                },
                modifier = Modifier.rotate(rotation),
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontFamily = fontFamily,
                fontStyle = fontStyle,
                textAlign = TextAlign.Center
            )
        }
    }
}
/***
 * Composable function to create a button for a specific tool feature.
 * @param featureName Name of the feature (e.g., OCR, PDF, WORD).
 * @param buttonColor Default color of the button.
 * @param onClick Lambda function to handle button clicks.
 */
@Composable
fun ToolButton(featureName: String, buttonColor: Color, onClick: () -> Unit) {
    // Button composable with a fixed background color
    Button(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        // Text inside the button
        Text(
            text = featureName,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

/***
 * Composable function to create a clickable menu item.
 * @param text Text to display in the menu item.
 * @param onClick Lambda function to handle item clicks.
 */
@Composable
fun MenuItem(text: String, onClick: () -> Unit) {
    // Text composable for the menu item
    Text(
        text = text,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick) // Make the text clickable
    )
}