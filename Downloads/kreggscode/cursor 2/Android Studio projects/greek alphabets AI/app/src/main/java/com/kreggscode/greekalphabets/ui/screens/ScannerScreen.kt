package com.kreggscode.greekalphabets.ui.screens

import android.Manifest
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.kreggscode.greekalphabets.data.translation.ObjectTranslator
import com.kreggscode.greekalphabets.data.translation.TranslationDirection
import com.kreggscode.greekalphabets.data.translation.TranslatorManager
import com.kreggscode.greekalphabets.ui.components.*
import com.kreggscode.greekalphabets.ui.theme.*
import com.kreggscode.greekalphabets.utils.containsGreek
import com.kreggscode.greekalphabets.utils.pronounceGreek
import com.kreggscode.greekalphabets.utils.speakText
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@androidx.camera.core.ExperimentalGetImage
@Composable
fun ScannerScreen(navController: NavController) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    LaunchedEffect(Unit) {
        cameraPermissionState.launchPermissionRequest()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        when {
            cameraPermissionState.status.isGranted -> {
                CameraView(navController)
            }
            cameraPermissionState.status.shouldShowRationale -> {
                PermissionRationale(
                    onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
                    onCancel = { navController.navigateUp() }
                )
            }
            else -> {
                PermissionDenied(
                    onGoBack = { navController.navigateUp() }
                )
            }
        }
    }
}

@Composable
fun CameraView(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    
    var detectedText by remember { mutableStateOf("") }
    var greekTranslation by remember { mutableStateOf("") }
    var pronouncedText by remember { mutableStateOf("") }
    var translationStatus by remember { mutableStateOf<TranslationStatus>(TranslationStatus.Idle) }
    var isProcessing by remember { mutableStateOf(false) }
    var scanMode by remember { mutableStateOf(ScanMode.OBJECT) }
    var showResult by remember { mutableStateOf(false) }
    var shouldCapture by remember { mutableStateOf(false) }

    var isTtsReady by remember { mutableStateOf(false) }
    var supportsGreek by remember { mutableStateOf(false) }
    var supportsEnglish by remember { mutableStateOf(false) }
    
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    val translatorManager = remember { TranslatorManager() }
    
    // TTS
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    
    DisposableEffect(context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.let { engine ->
                    val greekLocale = Locale.forLanguageTag("el-GR")
                    val greekAvailability = engine.isLanguageAvailable(greekLocale)
                    val englishAvailability = engine.isLanguageAvailable(Locale.US)
                    
                    supportsGreek = greekAvailability >= TextToSpeech.LANG_AVAILABLE
                    supportsEnglish = englishAvailability >= TextToSpeech.LANG_AVAILABLE
                    
                    when {
                        supportsGreek -> engine.language = greekLocale
                        supportsEnglish -> engine.language = Locale.US
                        else -> engine.language = Locale.getDefault()
                    }
                    
                    isTtsReady = true
                }
            } else {
                isTtsReady = false
                supportsGreek = false
                supportsEnglish = false
            }
        }
        
        onDispose {
            isTtsReady = false
            supportsGreek = false
            supportsEnglish = false
            tts?.stop()
            tts?.shutdown()
            cameraExecutor.shutdown()
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            translatorManager.close()
        }
    }
    
    // Remember the analyzer to update it dynamically
    val imageAnalyzer = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
    }
    
    // Timeout mechanism - reset processing if it takes too long
    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            kotlinx.coroutines.delay(3000) // 3 second timeout
            if (isProcessing) {
                isProcessing = false // Reset if still processing after timeout
                shouldCapture = false
                scope.launch {
                    Toast.makeText(context, "Processing timed out. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    DisposableEffect(Unit) {
        imageAnalyzer.setAnalyzer(cameraExecutor) { imageProxy ->
            try {
                if (!shouldCapture || isProcessing) {
                    imageProxy.close()
                    return@setAnalyzer
                }
                
                shouldCapture = false
                isProcessing = true
                showResult = false
                
                val currentMode = scanMode
                
                processImage(
                    imageProxy = imageProxy,
                    scanMode = currentMode,
                    onTextDetected = { text ->
                        val cleanedText = text.lineSequence()
                            .map { it.trim() }
                            .firstOrNull { it.isNotEmpty() }
                        
                        if (!cleanedText.isNullOrBlank()) {
                            scope.launch {
                                try {
                                    handleDetectedText(
                                        rawText = cleanedText,
                                        translatorManager = translatorManager,
                                        context = context,
                                        onEnglish = { detectedText = it },
                                        onGreek = { greekTranslation = it },
                                        onRomanization = { pronouncedText = it },
                                        onStatus = { translationStatus = it },
                                        onShowResult = { showResult = it }
                                    )
                                } finally {
                                    isProcessing = false
                                }
                            }
                        } else {
                            scope.launch {
                                resetScanResult(
                                    englishState = { detectedText = it },
                                    greekState = { greekTranslation = it },
                                    romanizationState = { pronouncedText = it },
                                    statusState = { translationStatus = it }
                                )
                                Toast.makeText(context, "No text detected. Try again.", Toast.LENGTH_SHORT).show()
                                isProcessing = false
                            }
                        }
                    },
                    onLabelsDetected = { labels ->
                        if (labels.isNotEmpty()) {
                            val label = labels.firstOrNull().orEmpty().trim()
                            if (label.isNotEmpty()) {
                                scope.launch {
                                    try {
                                        handleDetectedText(
                                            rawText = label,
                                            translatorManager = translatorManager,
                                            context = context,
                                            onEnglish = { detectedText = it },
                                            onGreek = { greekTranslation = it },
                                            onRomanization = { pronouncedText = it },
                                            onStatus = { translationStatus = it },
                                            onShowResult = { showResult = it }
                                        )
                                    } finally {
                                        isProcessing = false
                                    }
                                }
                            } else {
                                scope.launch {
                                    resetScanResult(
                                        englishState = { detectedText = it },
                                        greekState = { greekTranslation = it },
                                        romanizationState = { pronouncedText = it },
                                        statusState = { translationStatus = it }
                                    )
                                    Toast.makeText(context, "No object detected. Try again.", Toast.LENGTH_SHORT).show()
                                    isProcessing = false
                                }
                            }
                        } else {
                            scope.launch {
                                resetScanResult(
                                    englishState = { detectedText = it },
                                    greekState = { greekTranslation = it },
                                    romanizationState = { pronouncedText = it },
                                    statusState = { translationStatus = it }
                                )
                                Toast.makeText(context, "No object detected. Try again.", Toast.LENGTH_SHORT).show()
                                isProcessing = false
                            }
                        }
                    },
                    onError = {
                        isProcessing = false
                        shouldCapture = false
                        resetScanResult(
                            englishState = { detectedText = it },
                            greekState = { greekTranslation = it },
                            romanizationState = { pronouncedText = it },
                            statusState = { translationStatus = it }
                        )
                        scope.launch {
                            Toast.makeText(context, "Unable to process image. Try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
                imageProxy.close()
                isProcessing = false
                shouldCapture = false
                scope.launch {
                    Toast.makeText(context, "Processing failed. Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        
        onDispose {
            imageAnalyzer.clearAnalyzer()
        }
    }
    
    // Initialize camera once
    LaunchedEffect(Unit) {
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        
        // Overlay UI
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar
            ScannerTopBar(
                onBackClick = { navController.navigateUp() },
                scanMode = scanMode,
                onModeChange = { scanMode = it }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (!showResult) {
                Text(
                    text = if (isProcessing) "Processing capture..." else "Tap the capture button to scan",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Capture/Reset Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        if (showResult) {
                            // Reset and continue scanning
                            showResult = false
                            detectedText = ""
                            greekTranslation = ""
                            pronouncedText = ""
                            translationStatus = TranslationStatus.Idle
                        }
                        if (!isProcessing) {
                            shouldCapture = true
                        }
                    },
                    enabled = !isProcessing || showResult,
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(PremiumIndigo, PremiumPurple)
                            ),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        if (showResult) Icons.Filled.Refresh else Icons.Filled.Camera,
                        contentDescription = if (showResult) "Scan again" else "Capture",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        
        // Scanning Animation
        AnimatedVisibility(
            visible = isProcessing,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(
                        color = PremiumIndigo,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Processing image...",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    )
                }
            }
        }
        
        // Result Card
        AnimatedVisibility(
            visible = showResult && !isProcessing,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ScanResultCard(
                detectedText = detectedText,
                greekText = greekTranslation,
                romanization = pronouncedText,
                translationStatus = translationStatus,
                supportsGreek = supportsGreek,
                supportsEnglish = supportsEnglish,
                onSpeakGreek = {
                    speakText(
                        tts = tts,
                        locale = Locale.forLanguageTag("el-GR"),
                        text = greekTranslation,
                        context = context,
                        isTtsReady = isTtsReady,
                        onAvailabilityChecked = { available -> supportsGreek = available }
                    )
                },
                onSpeakEnglish = {
                    val spokenText = detectedText.ifBlank { greekTranslation }
                    speakText(
                        tts = tts,
                        locale = Locale.US,
                        text = spokenText,
                        context = context,
                        isTtsReady = isTtsReady,
                        onAvailabilityChecked = { available -> supportsEnglish = available }
                    )
                },
                onDismiss = { 
                    showResult = false
                    detectedText = ""
                    greekTranslation = ""
                    pronouncedText = ""
                    translationStatus = TranslationStatus.Idle
                }
            )
        }
    }
}

enum class ScanMode {
    OBJECT, TEXT
}

@Composable
fun ScannerTopBar(
    onBackClick: () -> Unit,
    scanMode: ScanMode,
    onModeChange: (ScanMode) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Text(
                    text = "Object Scanner",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                IconButton(onClick = { /* Settings */ }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = scanMode == ScanMode.OBJECT,
                    onClick = { onModeChange(ScanMode.OBJECT) },
                    label = { Text("Object Detection") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = scanMode == ScanMode.TEXT,
                    onClick = { onModeChange(ScanMode.TEXT) },
                    label = { Text("Text Recognition") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ScanResultCard(
    detectedText: String,
    greekText: String,
    romanization: String,
    translationStatus: TranslationStatus,
    supportsGreek: Boolean,
    supportsEnglish: Boolean,
    onSpeakGreek: () -> Unit,
    onSpeakEnglish: () -> Unit,
    onDismiss: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        cornerRadius = 24.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            PremiumIndigo.copy(alpha = 0.1f),
                            PremiumPurple.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Detected Text",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TranslationStatusView(
                status = translationStatus,
                modifier = Modifier.fillMaxWidth(),
                loadingMessage = "Translating to Greek..."
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // English Text
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "English",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = detectedText.ifBlank { "—" },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PremiumPink
                        )
                    }
                    IconButton(
                        onClick = onSpeakEnglish,
                        enabled = supportsEnglish && detectedText.isNotBlank(),
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (supportsEnglish && detectedText.isNotBlank()) PremiumPink.copy(alpha = 0.12f) else MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.05f
                                ),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Filled.VolumeUp,
                            contentDescription = "Speak English",
                            tint = if (supportsEnglish && detectedText.isNotBlank()) PremiumPink else MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.4f
                            )
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Greek Translation with Romanization
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Greek (Ελληνικά)",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = greekText.ifBlank { "—" },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = PremiumIndigo
                        )
                        if (romanization.isNotEmpty()) {
                            Text(
                                text = romanization,
                                fontSize = 16.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
                
                IconButton(
                    onClick = onSpeakGreek,
                    enabled = supportsGreek && greekText.isNotBlank(),
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (supportsGreek && greekText.isNotBlank()) PremiumIndigo.copy(alpha = 0.1f) else MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.05f
                            ),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Filled.VolumeUp,
                        contentDescription = "Speak Greek",
                        tint = if (supportsGreek && greekText.isNotBlank()) PremiumIndigo else MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.4f
                        )
                    )
                }
            }
        }
    }
}

private fun resetScanResult(
    englishState: (String) -> Unit,
    greekState: (String) -> Unit,
    romanizationState: (String) -> Unit,
    statusState: (TranslationStatus) -> Unit
) {
    englishState("")
    greekState("")
    romanizationState("")
    statusState(TranslationStatus.Idle)
}

private suspend fun handleDetectedText(
    rawText: String,
    translatorManager: TranslatorManager,
    context: android.content.Context,
    onEnglish: (String) -> Unit,
    onGreek: (String) -> Unit,
    onRomanization: (String) -> Unit,
    onStatus: (TranslationStatus) -> Unit,
    onShowResult: (Boolean) -> Unit
) {
    onEnglish(rawText)
    onShowResult(true)

    val dictionaryMatch = ObjectTranslator.translate(rawText)
    if (dictionaryMatch != null) {
        onGreek(dictionaryMatch.Greek)
        onRomanization(dictionaryMatch.romanization)
        onStatus(TranslationStatus.Dictionary)
        return
    }

    if (containsGreek(rawText)) {
        onGreek(rawText)
        onRomanization(pronounceGreek(rawText))
        onStatus(TranslationStatus.DetectedGreek)
        return
    }

    onStatus(TranslationStatus.Loading)
    onGreek("")
    onRomanization("")

    val translated = translatorManager.translate(rawText, TranslationDirection.ENGLISH_TO_Greek)
    if (!translated.isNullOrBlank()) {
        onGreek(translated)
        onRomanization(pronounceGreek(translated))
        onStatus(TranslationStatus.Machine)
    } else {
        onStatus(TranslationStatus.Error("Translation unavailable"))
        Toast.makeText(
            context,
            "Unable to find a Greek equivalent for \"$rawText\".",
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
fun PermissionRationale(
    onRequestPermission: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.CameraAlt,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = PremiumIndigo
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Camera Permission Required",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "This feature needs camera access to scan objects and translate them to Greek.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        AnimatedGradientButton(
            text = "Grant Permission",
            onClick = onRequestPermission,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        TextButton(onClick = onCancel) {
            Text("Cancel", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun PermissionDenied(
    onGoBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.CameraAlt,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Camera Permission Denied",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Please enable camera permission in your device settings to use this feature.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedButton(
            onClick = onGoBack,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text("Go Back")
        }
    }
}

// ML Kit Processing Functions
@androidx.camera.core.ExperimentalGetImage
fun processImage(
    imageProxy: ImageProxy,
    scanMode: ScanMode,
    onTextDetected: (String) -> Unit,
    onLabelsDetected: (List<String>) -> Unit,
    onError: () -> Unit = {}
) {
    try {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            when (scanMode) {
                ScanMode.TEXT -> {
                    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    recognizer.process(image)
                        .addOnSuccessListener { visionText ->
                            val text = visionText.text.trim()
                            if (text.isNotEmpty()) {
                                onTextDetected(text)
                            } else {
                                onError() // No text detected
                            }
                            imageProxy.close()
                        }
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                            onError()
                            imageProxy.close()
                        }
                        .addOnCompleteListener {
                            imageProxy.close() // Ensure always closed
                        }
                }
                ScanMode.OBJECT -> {
                    val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                    labeler.process(image)
                        .addOnSuccessListener { labels ->
                            val validLabels = labels.filter { it.confidence > 0.5f }.map { it.text }
                            if (validLabels.isNotEmpty()) {
                                onLabelsDetected(validLabels)
                            } else {
                                onError() // No valid labels
                            }
                            imageProxy.close()
                        }
                        .addOnFailureListener { e ->
                            e.printStackTrace()
                            onError()
                            imageProxy.close()
                        }
                        .addOnCompleteListener {
                            imageProxy.close() // Ensure always closed
                        }
                }
            }
        } else {
            imageProxy.close()
            onError()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        imageProxy.close()
        onError()
    }
}

// Simple translation function (you can expand this)
