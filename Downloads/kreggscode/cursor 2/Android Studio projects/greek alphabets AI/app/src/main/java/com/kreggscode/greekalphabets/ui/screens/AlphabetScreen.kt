package com.kreggscode.greekalphabets.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.compose.foundation.BorderStroke
import com.kreggscode.greekalphabets.ui.components.*
import com.kreggscode.greekalphabets.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalAnimationApi::class)

data class GreekCharacter(
    val Greek: String,
    val lowercase: String,
    val name: String,
    val romanization: String,
    val type: String, // "consonant" or "vowel"
    val description: String
)

@Composable
fun AlphabetScreen(navController: NavController) {
    val consonants = listOf(
        GreekCharacter("Β", "β", "Beta", "v", "consonant", "like 'v' in very"),
        GreekCharacter("Γ", "γ", "Gamma", "g", "consonant", "like 'g' in go (or 'y' before front vowels)"),
        GreekCharacter("Δ", "δ", "Delta", "d", "consonant", "like 'th' in this"),
        GreekCharacter("Ζ", "ζ", "Zeta", "z", "consonant", "like 'z' in zebra"),
        GreekCharacter("Θ", "θ", "Theta", "th", "consonant", "like 'th' in think"),
        GreekCharacter("Κ", "κ", "Kappa", "k", "consonant", "like 'k' in key"),
        GreekCharacter("Λ", "λ", "Lambda", "l", "consonant", "like 'l' in love"),
        GreekCharacter("Μ", "μ", "Mu", "m", "consonant", "like 'm' in mom"),
        GreekCharacter("Ν", "ν", "Nu", "n", "consonant", "like 'n' in no"),
        GreekCharacter("Ξ", "ξ", "Xi", "x", "consonant", "like 'x' in box"),
        GreekCharacter("Π", "π", "Pi", "p", "consonant", "like 'p' in pie"),
        GreekCharacter("Ρ", "ρ", "Rho", "r", "consonant", "like 'r' in red"),
        GreekCharacter("Σ", "σ", "Sigma", "s", "consonant", "like 's' in sea (ς at word end)"),
        GreekCharacter("Τ", "τ", "Tau", "t", "consonant", "like 't' in tea"),
        GreekCharacter("Φ", "φ", "Phi", "ph", "consonant", "like 'f' in fish"),
        GreekCharacter("Χ", "χ", "Chi", "ch", "consonant", "like 'ch' in loch"),
        GreekCharacter("Ψ", "ψ", "Psi", "ps", "consonant", "like 'ps' in lips")
    )
    
    val vowels = listOf(
        GreekCharacter("Α", "α", "Alpha", "a", "vowel", "like 'a' in father"),
        GreekCharacter("Ε", "ε", "Epsilon", "e", "vowel", "like 'e' in bed"),
        GreekCharacter("Η", "η", "Eta", "ē", "vowel", "like 'ee' in see"),
        GreekCharacter("Ι", "ι", "Iota", "i", "vowel", "like 'ee' in see"),
        GreekCharacter("Ο", "ο", "Omicron", "o", "vowel", "like 'o' in go"),
        GreekCharacter("Υ", "υ", "Upsilon", "u", "vowel", "like 'ee' in see"),
        GreekCharacter("Ω", "ω", "Omega", "ō", "vowel", "like 'o' in more (long)")
    )
    
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCharacter by remember { mutableStateOf<GreekCharacter?>(null) }
    var showTracing by remember { mutableStateOf(false) }
    val isDarkMode = isSystemInDarkTheme()
    
    // Edge-to-edge: background fills entire screen, content has padding for system bars
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header
            GreekAlphabetHeader()
            
            // Tab Selector
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.padding(horizontal = 16.dp),
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    val currentTabPosition = tabPositions[selectedTab]
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.BottomStart)
                            .offset(x = currentTabPosition.left)
                            .width(currentTabPosition.width)
                            .height(3.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(PremiumIndigo, PremiumPurple)
                                ),
                                shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                            )
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Consonants", fontWeight = FontWeight.Medium) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Vowels & Special", fontWeight = FontWeight.Medium) }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Character Grid
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                }
            ) { tab ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val characters = if (tab == 0) consonants else vowels
                    items(characters) { character ->
                        GreekCharacterCard(
                            character = character,
                            onClick = {
                                selectedCharacter = character
                                showTracing = true
                            }
                        )
                    }
                }
            }
        }
        
        // Tracing Dialog
        if (showTracing && selectedCharacter != null) {
            TracingDialog(
                character = selectedCharacter!!,
                onDismiss = { 
                    showTracing = false
                    selectedCharacter = null
                }
            )
        }
    }
}

@Composable
fun GreekAlphabetHeader() {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            width = 2.dp,
            brush = Brush.linearGradient(
                colors = listOf(PremiumTeal, PremiumIndigo, PremiumPurple),
                start = Offset(0f, 0f),
                end = Offset(1000f * animatedOffset, 1000f * animatedOffset)
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            PremiumTeal.copy(alpha = 0.1f),
                            PremiumIndigo.copy(alpha = 0.1f),
                            PremiumPurple.copy(alpha = 0.1f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Α-Ω",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = PremiumIndigo
                )
                Text(
                    text = "Learn Greek Alphabet",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap any character to practice tracing",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun GreekCharacterCard(
    character: GreekCharacter,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    
    GlassmorphicCard(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale),
        onClick = onClick,
        cornerRadius = 16.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if (character.type == "consonant") 
                                PremiumIndigo.copy(alpha = 0.05f)
                            else 
                                PremiumTeal.copy(alpha = 0.05f),
                            Color.Transparent
                        )
                    )
                )
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = character.Greek,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = if (character.type == "consonant") PremiumIndigo else PremiumTeal
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = character.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = character.romanization,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TracingDialog(
    character: GreekCharacter,
    onDismiss: () -> Unit
) {
    // Single list of paths for instant rendering
    val paths = remember { mutableStateListOf<Path>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var redrawTrigger by remember { mutableStateOf(0) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(0.95f)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Practice Tracing",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Character Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = character.Greek,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = PremiumIndigo
                        )
                        Text(
                            text = character.name,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = character.romanization,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Sound",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            text = character.description,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Drawing Canvas
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    border = BorderStroke(2.dp, PremiumIndigo.copy(alpha = 0.3f))
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Background character guide
                        Text(
                            text = character.Greek,
                            fontSize = 200.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            modifier = Modifier.align(Alignment.Center)
                        )
                        
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            val newPath = Path().apply {
                                                moveTo(offset.x, offset.y)
                                            }
                                            currentPath = newPath
                                            paths.add(newPath)
                                            redrawTrigger++
                                        },
                                        onDrag = { change, _ ->
                                            currentPath?.lineTo(change.position.x, change.position.y)
                                            redrawTrigger++
                                        },
                                        onDragEnd = {
                                            currentPath = null
                                            redrawTrigger++
                                        }
                                    )
                                }
                        ) {
                            // Force redraw by reading redrawTrigger
                            redrawTrigger
                            // Draw all paths
                            paths.forEach { path ->
                                drawPath(
                                    path = path,
                                    color = PremiumIndigo,
                                    style = Stroke(
                                        width = 12.dp.toPx(),
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            paths.clear()
                            currentPath = null
                            redrawTrigger++
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Clear")
                    }
                    
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PremiumIndigo
                        )
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Done")
                    }
                }
            }
        }
    }
}
