package com.kreggscode.greekalphabets.ui.screens

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kreggscode.greekalphabets.data.translation.ObjectTranslator
import com.kreggscode.greekalphabets.data.translation.TranslationDirection
import com.kreggscode.greekalphabets.data.translation.TranslatorManager
import com.kreggscode.greekalphabets.ui.components.GlassmorphicCard
import com.kreggscode.greekalphabets.ui.components.TranslationStatus
import com.kreggscode.greekalphabets.ui.components.TranslationStatusView
import com.kreggscode.greekalphabets.ui.theme.PremiumIndigo
import com.kreggscode.greekalphabets.ui.theme.PremiumPink
import com.kreggscode.greekalphabets.ui.theme.PremiumPurple
import com.kreggscode.greekalphabets.ui.theme.PremiumTeal
import com.kreggscode.greekalphabets.utils.containsGreek
import com.kreggscode.greekalphabets.utils.pronounceGreek
import com.kreggscode.greekalphabets.utils.speakText
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(navController: NavController) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val translatorManager = remember { TranslatorManager() }

    var direction by remember { mutableStateOf(TranslationDirection.ENGLISH_TO_Greek) }
    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }
    var inputRomanization by remember { mutableStateOf("") }
    var outputRomanization by remember { mutableStateOf("") }
    var translationStatus by remember { mutableStateOf<TranslationStatus>(TranslationStatus.Idle) }
    var isTranslating by remember { mutableStateOf(false) }

    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    var supportsGreek by remember { mutableStateOf(false) }
    var supportsEnglish by remember { mutableStateOf(false) }

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
        }
    }

    DisposableEffect(Unit) {
        onDispose { translatorManager.close() }
    }

    fun clearResults() {
        translatedText = ""
        inputRomanization = ""
        outputRomanization = ""
        translationStatus = TranslationStatus.Idle
    }

    fun handleDirectionChange(newDirection: TranslationDirection) {
        if (direction == newDirection) return
        direction = newDirection
        clearResults()
    }

    fun performTranslation() {
        val text = inputText.trim()
        if (text.isEmpty()) {
            Toast.makeText(context, "Please enter some text to translate.", Toast.LENGTH_SHORT).show()
            return
        }

        scope.launch {
            isTranslating = true
            translationStatus = TranslationStatus.Loading

            if (direction == TranslationDirection.ENGLISH_TO_Greek) {
                val dictionaryMatch = ObjectTranslator.translate(text)
                if (dictionaryMatch != null) {
                    translatedText = dictionaryMatch.Greek
                    inputRomanization = computeRomanization(text)
                    outputRomanization = computeRomanization(dictionaryMatch.Greek)
                    translationStatus = TranslationStatus.Dictionary
                    isTranslating = false
                    return@launch
                }
            }

            val translated = translatorManager.translate(text, direction)
            if (!translated.isNullOrBlank()) {
                translatedText = translated
                inputRomanization = computeRomanization(text)
                outputRomanization = computeRomanization(translated)
                translationStatus = TranslationStatus.Machine
            } else {
                translatedText = ""
                inputRomanization = computeRomanization(text)
                outputRomanization = ""
                translationStatus = TranslationStatus.Error("Translation unavailable")
                Toast.makeText(
                    context,
                    "Unable to translate \"$text\" right now.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            isTranslating = false
        }
    }

    val inputLanguageLabel = if (direction == TranslationDirection.ENGLISH_TO_Greek) "English" else "Greek"
    val outputLanguageLabel = if (direction == TranslationDirection.ENGLISH_TO_Greek) "Greek" else "English"
    val loadingMessage = if (direction == TranslationDirection.ENGLISH_TO_Greek) {
        "Translating to Greek..."
    } else {
        "Translating to English..."
    }

    val outputSpeakEnabled = translatedText.isNotBlank() && when (direction) {
        TranslationDirection.ENGLISH_TO_Greek -> supportsGreek
        TranslationDirection.Greek_TO_ENGLISH -> supportsEnglish
    }

    val inputSpeakEnabled = inputText.isNotBlank() && when (direction) {
        TranslationDirection.ENGLISH_TO_Greek -> supportsEnglish
        TranslationDirection.Greek_TO_ENGLISH -> supportsGreek
    }

    val speakOutput: () -> Unit = {
        val locale = if (direction == TranslationDirection.ENGLISH_TO_Greek) Locale.forLanguageTag("el-GR") else Locale.US
        speakText(
            tts = tts,
            locale = locale,
            text = translatedText,
            context = context,
            isTtsReady = isTtsReady,
            onAvailabilityChecked = { available ->
                if (direction == TranslationDirection.ENGLISH_TO_Greek) {
                    supportsGreek = available
                } else {
                    supportsEnglish = available
                }
            }
        )
    }

    val speakInput: () -> Unit = {
        val locale = if (direction == TranslationDirection.ENGLISH_TO_Greek) Locale.US else Locale.forLanguageTag("el-GR")
        speakText(
            tts = tts,
            locale = locale,
            text = inputText,
            context = context,
            isTtsReady = isTtsReady,
            onAvailabilityChecked = { available ->
                if (direction == TranslationDirection.ENGLISH_TO_Greek) {
                    supportsEnglish = available
                } else {
                    supportsGreek = available
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TranslatorTopBar(
                onBackClick = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            DirectionSelector(
                direction = direction,
                onDirectionChange = ::handleDirectionChange
            )

            InputCard(
                inputText = inputText,
                onTextChange = {
                    inputText = it
                    clearResults()
                },
                inputLanguageLabel = inputLanguageLabel,
                isTranslating = isTranslating,
                onTranslate = {
                    focusManager.clearFocus(force = true)
                    keyboardController?.hide()
                    performTranslation()
                },
                direction = direction
            )

            TranslationResultCard(
                translatedText = translatedText,
                translationStatus = translationStatus,
                loadingMessage = loadingMessage,
                outputLanguageLabel = outputLanguageLabel,
                inputLanguageLabel = inputLanguageLabel,
                onSpeakOutput = speakOutput,
                onSpeakInput = speakInput,
                outputSpeakEnabled = outputSpeakEnabled,
                inputSpeakEnabled = inputSpeakEnabled,
                inputText = inputText,
                inputRomanization = inputRomanization,
                outputRomanization = outputRomanization,
                onCopyOutput = {
                    if (translatedText.isNotBlank()) {
                        clipboardManager.setText(AnnotatedString(translatedText))
                    }
                },
                onCopyInput = {
                    if (inputText.isNotBlank()) {
                        clipboardManager.setText(AnnotatedString(inputText))
                    }
                }
            )
        }
    }
}

private fun computeRomanization(text: String): String {
    val trimmed = text.trim()
    if (trimmed.isEmpty()) return ""

    if (containsGreek(trimmed)) {
        // For Greek text, return pronunciation
        val pronounced = pronounceGreek(trimmed)
        return if (pronounced.isNotBlank()) pronounced else trimmed
    }

    // For English text, return as is (no romanization needed)
    return trimmed
}

@Composable
private fun TranslatorTopBar(onBackClick: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Translator",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun DirectionSelector(
    direction: TranslationDirection,
    onDirectionChange: (TranslationDirection) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = direction == TranslationDirection.ENGLISH_TO_Greek,
                    onClick = { onDirectionChange(TranslationDirection.ENGLISH_TO_Greek) },
                    label = { Text("English → Greek") }
                )
                FilterChip(
                    selected = direction == TranslationDirection.Greek_TO_ENGLISH,
                    onClick = { onDirectionChange(TranslationDirection.Greek_TO_ENGLISH) },
                    label = { Text("Greek → English") }
                )
            }

            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InputCard(
    inputText: String,
    onTextChange: (String) -> Unit,
    inputLanguageLabel: String,
    isTranslating: Boolean,
    onTranslate: () -> Unit,
    direction: TranslationDirection
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Input ($inputLanguageLabel)",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = inputText,
                onValueChange = onTextChange,
                modifier = Modifier
                    .fillMaxWidth(),
                minLines = 4,
                placeholder = {
                    Text(
                        text = if (direction == TranslationDirection.ENGLISH_TO_Greek) {
                            "Type something in English..."
                        } else {
                            "Type something in Greek..."
                        },
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            )

            AnimatedVisibility(
                visible = inputText.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "${inputText.length} characters",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            TranslateActionButton(
                isTranslating = isTranslating,
                onTranslate = onTranslate
            )
        }
    }
}

@Composable
private fun TranslateActionButton(
    isTranslating: Boolean,
    onTranslate: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PremiumTeal, PremiumIndigo)
                    )
                )
        ) {
            androidx.compose.material3.TextButton(
                onClick = onTranslate,
                enabled = !isTranslating,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = if (isTranslating) "Translating..." else "Translate",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun TranslationResultCard(
    translatedText: String,
    translationStatus: TranslationStatus,
    loadingMessage: String,
    outputLanguageLabel: String,
    inputLanguageLabel: String,
    onSpeakOutput: () -> Unit,
    onSpeakInput: () -> Unit,
    outputSpeakEnabled: Boolean,
    inputSpeakEnabled: Boolean,
    inputText: String,
    inputRomanization: String,
    outputRomanization: String,
    onCopyOutput: () -> Unit,
    onCopyInput: () -> Unit
) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        cornerRadius = 26.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            PremiumIndigo.copy(alpha = 0.08f),
                            PremiumPurple.copy(alpha = 0.1f),
                            PremiumPink.copy(alpha = 0.08f)
                        )
                    )
                )
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Translation ($outputLanguageLabel)",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            TranslationStatusView(
                status = translationStatus,
                loadingMessage = loadingMessage,
                modifier = Modifier.fillMaxWidth()
            )

            ResultSection(
                title = "Output",
                languageLabel = outputLanguageLabel,
                text = translatedText,
                romanization = outputRomanization,
                onSpeak = onSpeakOutput,
                speakEnabled = outputSpeakEnabled,
                onCopy = onCopyOutput
            )

            Divider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )

            ResultSection(
                title = "Input",
                languageLabel = inputLanguageLabel,
                text = inputText,
                romanization = inputRomanization,
                onSpeak = onSpeakInput,
                speakEnabled = inputSpeakEnabled,
                onCopy = onCopyInput
            )
        }
    }
}

@Composable
private fun ResultSection(
    title: String,
    languageLabel: String,
    text: String,
    romanization: String,
    onSpeak: () -> Unit,
    speakEnabled: Boolean,
    onCopy: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$title ($languageLabel)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = if (text.isBlank()) "—" else text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onSpeak,
                    enabled = speakEnabled && text.isNotBlank(),
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (speakEnabled && text.isNotBlank()) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = "Speak",
                        tint = if (speakEnabled && text.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }

                IconButton(
                    onClick = onCopy,
                    enabled = text.isNotBlank(),
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (text.isNotBlank()) MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = "Copy",
                        tint = if (text.isNotBlank()) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Romanization",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = if (romanization.isNotBlank()) romanization else "—",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (romanization.isNotBlank()) 0.9f else 0.5f)
            )
        }
    }
}

