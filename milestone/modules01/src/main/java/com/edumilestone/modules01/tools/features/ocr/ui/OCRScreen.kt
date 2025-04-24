package com.eMilestone.modules01.tools.features.ocr.ui

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.animation.core.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.window.Dialog

/**
 * Composable function that represents the OCR (Optical Character Recognition) screen.
 * This screen allows users to select an image from the gallery or take a picture using the camera,
 * processes the image to extract text, and displays the extracted text.
 *
 * @param navController The NavController used for navigation.
 */
@Composable
fun OCRScreen(navController: NavController) {
    // State variables to manage UI state
    var extractedText by remember { mutableStateOf(TextFieldValue("")) } // Holds the extracted text
    var isTextPanelVisible by remember { mutableStateOf(false) } // Controls visibility of the text panel
    var isDarkMode by remember { mutableStateOf(false) } // Toggles dark mode
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) } // Holds the selected image bitmap
    var isProcessing by remember { mutableStateOf(false) } // Indicates if OCR processing is ongoing

    // List of colors to transition through
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color(0xFF8B4513)) // Soil color
    val infiniteTransition = rememberInfiniteTransition()

    // Animate color transition
    val colorTransition = infiniteTransition.animateColor(
        initialValue = colors[0],
        targetValue = colors[1],
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 4000
                colors.forEachIndexed { index, color ->
                    color at (index * 1000) using LinearEasing
                }
            }
        )
    )

    // Colors based on dark mode state
    val backgroundColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
    val textColor = if (isDarkMode) Color(0xFFE0E0E0) else Color(0xFF6200EE)
    val secondaryTextColor = if (isDarkMode) Color(0xFFB0B0B0) else Color(0xFF757575)
    val buttonColor = if (isDarkMode) Color(0xFF3A3A3A) else Color(0xFF6200EE)
    val extractedTextBackgroundColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5)
    val extractedTextColor = if (isDarkMode) Color(0xFFE0E0E0) else Color(0xFF000000)

    // Get the current view
    val view = LocalView.current

    // Launcher for selecting an image from the gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageBitmap = loadImageFromUri(view, it)
        }
    }

    // Launcher for taking a picture with the camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        selectedImageBitmap = bitmap
    }

    // Main container with background color
    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)) {
        // Top bar with navigation and color transition
        TopBar(navController, colorTransition.value)

        // Main content of the screen
        Content(
            selectedImageBitmap,
            textColor,
            secondaryTextColor,
            buttonColor,
            cameraLauncher,
            galleryLauncher,
            isProcessing
        ) { isProcessing = true }

        // Toggle for dark mode
        DarkModeToggle(isDarkMode) { isDarkMode = it }

        // Show processing animation if OCR is in progress
        if (isProcessing) {
            ModernProcessingAnimation()
            LaunchedEffect(Unit) {
                delay(2000) // Simulate processing delay
                extractedText = TextFieldValue("Sample extracted text for demonstration purposes.")
                isTextPanelVisible = true
                isProcessing = false
                Log.d("OCRScreen", "Extracted text: ${extractedText.text}") // Log the extracted text
                Toast.makeText(view.context, "Text extracted", Toast.LENGTH_SHORT).show() // Show a toast message
            }
        }

        // Show the extracted text panel if text is available
        if (isTextPanelVisible) {
            ExtractedTextPanel(
                extractedText,
                { extractedText = it },
                isDarkMode,
                extractedTextBackgroundColor,
                textColor,
                extractedTextColor,
                buttonColor,
                view,
                onDismissRequest = { isTextPanelVisible = false }, // Handle dismiss action
                onBackPress = { isTextPanelVisible = false } // Handle back press
            )
        }
    }
}

/**
 * Composable function that represents the top bar of the OCR screen.
 * This top bar includes a back button that navigates the user back to the home screen.
 *
 * @param navController The NavController used for navigation.
 * @param color The background color of the back button.
 */
@Composable
fun TopBar(navController: NavController, color: Color) {
    // Heartbeat-like scale transition
    val scale = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 1000
                    1.2f at 500 using LinearEasing // Scale up at half duration
                    1f at 1000 using LinearEasing // Scale down at full duration
                }
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        IconButton(
            onClick = {
                navController.navigate("home_screen") {
                    popUpTo("home_screen") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(40.dp)
                .background(color, shape = CircleShape)
                .padding(8.dp)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value
                ) // Apply heartbeat-like scale transition using graphicsLayer
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to Home",
                tint = Color.White
            )
        }
    }
}

/**
 * Composable function that represents the main content of the OCR screen.
 * This content includes an icon, descriptive text, image display, and buttons for capturing or uploading images.
 * It also includes a button to start the OCR process and shows a processing animation if OCR is in progress.
 *
 * @param selectedImageBitmap The bitmap of the selected image.
 * @param textColor The color of the primary text.
 * @param secondaryTextColor The color of the secondary text.
 * @param buttonColor The color of the buttons.
 * @param cameraLauncher The launcher for capturing an image using the camera.
 * @param galleryLauncher The launcher for selecting an image from the gallery.
 * @param isProcessing Indicates if OCR processing is ongoing.
 * @param onConvertToTextClick The callback function to start the OCR process.
 */
@Composable
fun Content(
    selectedImageBitmap: Bitmap?,
    textColor: Color,
    secondaryTextColor: Color,
    buttonColor: Color,
    cameraLauncher: ActivityResultLauncher<Void?>,
    galleryLauncher: ActivityResultLauncher<String>,
    isProcessing: Boolean,
    onConvertToTextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon representing the OCR feature
        Icon(
            imageVector = Icons.Filled.Description,
            contentDescription = "OCR Icon",
            tint = textColor,
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 16.dp)
        )

        // Title text for the OCR feature
        Text(
            text = "OCR Feature",
            fontWeight = FontWeight.Bold,
            color = textColor,
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Description text for the OCR feature
        Text(
            text = "Extract text from images easily with our OCR tool.",
            color = secondaryTextColor,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Display the selected image if available
        selectedImageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 16.dp)
                    .scale(1.1f) // Modern design with slight scaling
            )
        }

        // Row containing buttons for capturing or uploading images
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { cameraLauncher.launch(null) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CameraAlt,
                    contentDescription = "Capture Image",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Button(
                onClick = { galleryLauncher.launch("image/*") },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoLibrary,
                    contentDescription = "Upload from Gallery",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Button to start the OCR process
        Button(
            onClick = onConvertToTextClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Description,
                contentDescription = "Convert to Text",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Convert to Text",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Show processing animation if OCR is in progress
        AnimatedVisibility(visible = isProcessing) {
            ModernProcessingAnimation()
        }
    }
}

/**
 * Composable function that represents a toggle switch for dark mode.
 * This toggle allows users to switch between dark mode and light mode.
 *
 * @param isDarkMode Indicates whether dark mode is currently enabled.
 * @param onToggle The callback function to handle the toggle switch change.
 */
@Composable
fun DarkModeToggle(isDarkMode: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Text label for the dark mode toggle
        Text(
            text = "Dark Mode",
            color = if (isDarkMode) Color.White else Color.Black,
            modifier = Modifier.padding(end = 8.dp)
        )

        // Switch to toggle dark mode
        Switch(
            checked = isDarkMode,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFFBB86FC),
                uncheckedThumbColor = Color.Gray,
                checkedTrackColor = Color(0xFF3700B3),
                uncheckedTrackColor = Color(0xFF757575)
            )
        )
    }
}

/**
 * Composable function that represents a panel displaying the extracted text.
 * This panel includes a text field for the extracted text, and buttons to copy or share the text.
 *
 * @param extractedText The extracted text to be displayed.
 * @param onValueChange The callback function to handle changes to the text field.
 * @param isDarkMode Indicates whether dark mode is currently enabled.
 * @param backgroundColor The background color of the panel.
 * @param textColor The color of the primary text.
 * @param extractedTextColor The color of the extracted text.
 * @param buttonColor The color of the buttons.
 * @param view The current view context.
 * @param onDismissRequest Callback function to close the panel.
 * @param onBackPress Callback function to handle back button press.
 */
@Composable
fun ExtractedTextPanel(
    extractedText: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isDarkMode: Boolean,
    backgroundColor: Color,
    textColor: Color,
    extractedTextColor: Color,
    buttonColor: Color,
    view: View,
    onDismissRequest: () -> Unit,
    onBackPress: () -> Unit // Add a callback for back press
) {
    // Remember the focus requester to request focus on the text field
    val focusRequester = remember { FocusRequester() }
    // Get the software keyboard controller to show the keyboard
    val keyboardController = LocalSoftwareKeyboardController.current

    // Request focus and show the keyboard when the composable is launched
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    // Handle the back button press
    BackHandler {
        onBackPress() // Call the back press callback
    }

    // Animated visibility for the panel with slide-in and slide-out animations
    AnimatedVisibility(
        visible = true, // Change this dynamically if needed
        enter = slideInHorizontally { fullWidth -> fullWidth }, // Slide in from Right
        exit = slideOutHorizontally { fullWidth -> -fullWidth }  // Slide out to Left
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize() // Adapt to all screen sizes
                .background(if (isDarkMode) Color(0xFF121212) else backgroundColor)
                .padding(16.dp)
                .pointerInput(Unit) { detectTapGestures { /* Consume touch to prevent triggering parent clicks */ }
                }
        ) {
            Column {
                // Header with Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Extracted Text:",
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        fontSize = 20.sp
                    )

                    // Close button to dismiss panel
                    IconButton(onClick = onDismissRequest) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = textColor)
                    }
                }

                // Horizontal Divider
                HorizontalDivider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())

                // Custom TextField with scroll support
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .padding(4.dp)
                        .focusRequester(focusRequester)
                        .focusable()
                        .onFocusChanged {
                            if (it.isFocused) {
                                keyboardController?.show()
                            }
                        }
                ) {
                    CustomTextField(
                        value = extractedText,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxSize(), // Ensure it takes full space
                        isDarkMode = isDarkMode,
                        textColor = extractedTextColor
                    )
                }

                // Buttons for Copy and Share
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            // Copy the extracted text to the clipboard
                            val clipboard = view.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Extracted Text", extractedText.text)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(view.context, "Text copied", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text(text = "Copy", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            // Share the extracted text via an intent
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, extractedText.text)
                                type = "text/plain"
                            }
                            view.context.startActivity(Intent.createChooser(shareIntent, "Share text via"))
                            Toast.makeText(view.context, "Text shared", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    ) {
                        Text(text = "Share", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/**
 * Composable function that represents a custom text field.
 * This text field adapts its appearance based on the dark mode state.
 *
 * @param value The current text value of the text field.
 * @param onValueChange The callback function to handle changes to the text field.
 * @param modifier The modifier to be applied to the text field.
 * @param isDarkMode Indicates whether dark mode is currently enabled.
 * @param textColor The color of the text.
 */
@Composable
fun CustomTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    textColor: Color
) {
    // Set background and indicator colors based on dark mode state
    val backgroundColor = if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFF5F5F5)
    val indicatorColor = if (isDarkMode) Color(0xFFE0E0E0) else Color(0xFF6200EE)

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .shadow(4.dp, shape = RoundedCornerShape(8.dp), clip = true)
            .scale(1.05f) // Modern design with slight scaling
            .focusable(), // Ensure the TextField is focusable
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            cursorColor = textColor,
            focusedIndicatorColor = indicatorColor,
            unfocusedIndicatorColor = indicatorColor,
            disabledTextColor = textColor
        ),
        textStyle = LocalTextStyle.current.copy(color = textColor)
    )
}

/**
 * Composable function that represents a modern processing animation.
 * This animation shows rotating dots in different colors.
 */
@Composable
fun ModernProcessingAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val colors = listOf(Color.Red, Color.Blue, Color.Green)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(modifier = Modifier.size(100.dp)) {
            val radius = size.minDimension / 2
            val dotRadius = 6.dp.toPx()
            val distance = 15.dp.toPx() // Decreased distance between dots
            colors.forEachIndexed { index, color ->
                val angle = Math.toRadians((rotation + index * 120).toDouble())
                val x = radius + distance * cos(angle).toFloat()
                val y = radius + distance * sin(angle).toFloat()
                drawCircle(color = color, radius = dotRadius, center = Offset(x, y))
            }
        }
    }
}

/**
 * Function to load an image from a URI.
 * This function handles different Android versions for loading the image.
 *
 * @param view The current view context.
 * @param uri The URI of the image to be loaded.
 * @return The loaded bitmap.
 */
fun loadImageFromUri(view: View, uri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT < 28) {
        val inputStream = view.context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream).also {
            inputStream?.close()
        }
    } else {
        val source = ImageDecoder.createSource(view.context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}