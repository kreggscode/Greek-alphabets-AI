package com.kreggscode.greekalphabets.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kreggscode.greekalphabets.data.ai.AIService
import com.kreggscode.greekalphabets.data.models.GreekWord
import com.kreggscode.greekalphabets.data.repository.WordRepository
import com.kreggscode.greekalphabets.data.repository.FavoritesRepository
import com.kreggscode.greekalphabets.navigation.Screen
import com.kreggscode.greekalphabets.ui.components.*
import com.kreggscode.greekalphabets.ui.theme.*
import com.kreggscode.greekalphabets.utils.speakText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerbDetailScreen(
    verbId: String,
    navController: NavController
) {
    val context = LocalContext.current
    val repository = remember { WordRepository(context) }
    val favoritesRepository = remember { FavoritesRepository(context) }
    val scope = rememberCoroutineScope()
    
    val aiService = remember { AIService(context) }
    
    var word by remember { mutableStateOf<GreekWord?>(null) }
    var allVerbs by remember { mutableStateOf<List<GreekWord>>(emptyList()) }
    var currentIndex by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var showExplanation by remember { mutableStateOf(false) }
    var aiExplanation by remember { mutableStateOf("") }
    var isLoadingAI by remember { mutableStateOf(false) }
    val favoriteIds by favoritesRepository.favoritesFlow.collectAsState()
    val isFavorite = word?.let { favoriteIds.contains(it.id) } ?: false
    
    // TTS
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
    
    // Load all words once and cache them - MUST complete before showing word
    LaunchedEffect(Unit) {
        scope.launch {
            if (allVerbs.isEmpty()) {
                allVerbs = repository.getAllVerbs()
            }
            // Once words are loaded, find and set the current word immediately
            if (allVerbs.isNotEmpty() && word == null) {
                val foundVerb = allVerbs.find { it.id == verbId } ?: repository.getVerbById(verbId)
                word = foundVerb
                currentIndex = allVerbs.indexOfFirst { it.id == verbId }
                isLoading = false
            }
        }
    }
    
    // Update current word when verbId changes (instant - from cache)
    LaunchedEffect(verbId, allVerbs) {
        if (allVerbs.isNotEmpty()) {
            // Instant update from cache - no loading state
            val foundVerb = allVerbs.find { it.id == verbId } ?: repository.getVerbById(verbId)
            word = foundVerb
            currentIndex = allVerbs.indexOfFirst { it.id == verbId }
            // Reset AI explanation when word changes
            showExplanation = false
            aiExplanation = ""
            isLoading = false
        } else if (allVerbs.isEmpty()) {
            // Show loading only if words haven't loaded yet
            isLoading = true
        }
    }
    
    fun navigateToVerb(index: Int) {
        if (index in allVerbs.indices) {
            val nextVerb = allVerbs[index]
            // Instant navigation - replace current word detail to prevent huge back stack
            navController.navigate(Screen.VerbDetail.createRoute(nextVerb.id)) {
                // Replace current word detail instead of adding to stack
                // This prevents building up 20+ screens and glitchy back navigation
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (currentRoute?.startsWith("verb_detail/") == true) {
                    popUpTo(currentRoute) { inclusive = true }
                }
                launchSingleTop = true
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        // Remove animations for instant display - prevents glitchy back navigation
        if (!isLoading && word != null) {
            word?.let { currentVerb ->
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(bottom = 100.dp)
                    ) {
                        // Header
                        VerbDetailHeader(
                            word = currentVerb,
                            onBackClick = { navController.navigateUp() },
                            onFavoriteClick = { 
                                scope.launch {
                                    favoritesRepository.toggleFavorite(currentVerb.id)
                                }
                            },
                            isFavorite = isFavorite
                        )
                        
                        // Main word Card
                        MainVerbCard(
                            word = currentVerb,
                            onSpeakGreek = {
                                speakText(
                                    tts = tts,
                                    locale = Locale.forLanguageTag("el-GR"),
                                    text = currentVerb.word,
                                    context = context,
                                    isTtsReady = isTtsReady,
                                    onAvailabilityChecked = { supportsGreek = it }
                                )
                            },
                            onSpeakEnglish = {
                                speakText(
                                    tts = tts,
                                    locale = Locale.US,
                                    text = currentVerb.englishMeaning,
                                    context = context,
                                    isTtsReady = isTtsReady,
                                    onAvailabilityChecked = { supportsEnglish = it }
                                )
                            }
                        )
                        
                        // Example Sentence Card
                        ExampleSentenceCard(
                            word = currentVerb,
                            onSpeakGreek = {
                                speakText(
                                    tts = tts,
                                    locale = Locale.forLanguageTag("el-GR"),
                                    text = currentVerb.GreekSentence,
                                    context = context,
                                    isTtsReady = isTtsReady,
                                    onAvailabilityChecked = { supportsGreek = it }
                                )
                            },
                            onSpeakEnglish = {
                                speakText(
                                    tts = tts,
                                    locale = Locale.US,
                                    text = currentVerb.englishSentence,
                                    context = context,
                                    isTtsReady = isTtsReady,
                                    onAvailabilityChecked = { supportsEnglish = it }
                                )
                            }
                        )
                        
                        // AI Explanation Section
                        Spacer(modifier = Modifier.height(16.dp))
                        AIExplanationCard(
                            word = currentVerb,
                            isExpanded = showExplanation,
                            explanation = aiExplanation,
                            isLoading = isLoadingAI,
                            onToggle = {
                                showExplanation = !showExplanation
                                if (showExplanation && aiExplanation.isEmpty()) {
                                    isLoadingAI = true
                                    scope.launch {
                                        val prompt = """
                                            Explain the Greek word '${currentVerb.word}' (${currentVerb.pronunciation}) which means '${currentVerb.englishMeaning}'.
                                            
                                            Context:
                                            - Greek: ${currentVerb.word}
                                            - Romanization: ${currentVerb.pronunciation}
                                            - English: ${currentVerb.englishMeaning}
                                            - Category: ${currentVerb.category}
                                            - Example: ${currentVerb.GreekSentence} (${currentVerb.englishSentence})
                                            
                                            Please provide a detailed explanation specifically about THIS word:
                                            1. Detailed meaning and usage of '${currentVerb.word}'
                                            2. Common sentence patterns with '${currentVerb.word}'
                                            3. Conjugation tips specific to '${currentVerb.word}'
                                            4. Common mistakes learners make with '${currentVerb.word}'
                                            5. Additional example sentences using '${currentVerb.word}' with English translations
                                            6. Related words or expressions
                                            
                                            Keep the explanation clear, practical, and focused ONLY on the word '${currentVerb.word}' (${currentVerb.englishMeaning}).
                                        """.trimIndent()
                                        aiExplanation = aiService.getResponse(prompt, emptyList())
                                        isLoadingAI = false
                                    }
                                }
                            }
                        )
                    }
                    
                    // Fixed floating navigation buttons at bottom
                    NavigationButtons(
                        hasPrevious = currentIndex > 0,
                        hasNext = currentIndex < allVerbs.size - 1,
                        onPreviousClick = { navigateToVerb(currentIndex - 1) },
                        onNextClick = { navigateToVerb(currentIndex + 1) },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PremiumIndigo)
            }
        }
    }
}

@Composable
fun VerbDetailHeader(
    word: GreekWord,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(48.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Text(
            text = word.category,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
        
        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (isFavorite) PremiumPink.copy(alpha = 0.2f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    CircleShape
                )
        ) {
            Icon(
                if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) PremiumPink else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun MainVerbCard(
    word: GreekWord,
    onSpeakGreek: () -> Unit,
    onSpeakEnglish: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        cornerRadius = 24.dp,
        elevation = 12.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            PremiumIndigo.copy(alpha = 0.05f),
                            PremiumPurple.copy(alpha = 0.05f),
                            PremiumPink.copy(alpha = 0.05f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(1000f * animatedOffset, 1000f * animatedOffset)
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Greek word - with proper spacing and overflow handling
                Text(
                    text = word.word,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    lineHeight = 56.sp,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Romanization - below Greek text, smaller size
                Text(
                    text = word.pronunciation,
                    fontSize = 16.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // English Meaning
                Text(
                    text = word.englishMeaning,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Speech Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SpeechButton(
                        text = "Greek",
                        icon = Icons.Filled.VolumeUp,
                        onClick = onSpeakGreek,
                        gradientColors = listOf(PremiumIndigo, PremiumPurple),
                        modifier = Modifier.weight(1f)
                    )
                    
                    SpeechButton(
                        text = "English",
                        icon = Icons.Filled.VolumeUp,
                        onClick = onSpeakEnglish,
                        gradientColors = listOf(PremiumTeal, PremiumEmerald),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ExampleSentenceCard(
    word: GreekWord,
    onSpeakGreek: () -> Unit,
    onSpeakEnglish: () -> Unit
) {
    PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        gradientColors = listOf(PremiumPurple.copy(alpha = 0.3f), PremiumIndigo.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Example Sentence",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Greek Sentence
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = word.GreekSentence,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = word.sentencePronunciation,
                        fontSize = 14.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(
                        onClick = onSpeakGreek,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                PremiumIndigo.copy(alpha = 0.2f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Filled.VolumeUp,
                            contentDescription = "Speak",
                            tint = PremiumIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // English Translation
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = word.englishSentence,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(
                        onClick = onSpeakEnglish,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                PremiumTeal.copy(alpha = 0.2f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Filled.VolumeUp,
                            contentDescription = "Speak",
                            tint = PremiumTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SpeechButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        )
    )
    
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .scale(scale)
            .height(48.dp),
        border = BorderStroke(
            1.5.dp,
            brush = Brush.linearGradient(colors = gradientColors)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = gradientColors[0]
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = gradientColors[0]
        )
    }
}

@Composable
fun NavigationButtons(
    hasPrevious: Boolean,
    hasNext: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = isSystemInDarkTheme()
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.Black.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            } else {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onPreviousClick,
                enabled = hasPrevious,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (hasPrevious) 
                        MaterialTheme.colorScheme.surface
                    else 
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    contentColor = if (hasPrevious) 
                        MaterialTheme.colorScheme.primary
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                ),
                border = BorderStroke(
                    1.5.dp, 
                    if (hasPrevious) 
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
            ) {
                Icon(
                    Icons.Filled.ChevronLeft,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Previous",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Button(
                onClick = onNextClick,
                enabled = hasNext,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasNext) 
                        MaterialTheme.colorScheme.primary
                    else 
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    contentColor = if (hasNext) 
                        Color.White
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            ) {
                Text(
                    "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
