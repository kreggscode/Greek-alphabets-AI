package com.kreggscode.hebrewverbs.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kreggscode.hebrewverbs.ui.components.AnimatedGradientButton
import com.kreggscode.hebrewverbs.ui.components.GlassmorphicCard
import com.kreggscode.hebrewverbs.ui.components.PulsingIcon
import com.kreggscode.hebrewverbs.ui.components.StatCard

data class FeatureCard(
    val titleHebrew: String,
    val titleEnglish: String,
    val icon: ImageVector,
    val description: String,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToFeature: (String) -> Unit,
    onNavigateToScanner: () -> Unit,
    onNavigateToChat: () -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val features = remember {
        listOf(
            FeatureCard(
                titleHebrew = "שבעת הבניינים",
                titleEnglish = "The 7 Binyanim",
                icon = Icons.Default.AccountTree,
                description = "Master the 7 Hebrew verb patterns - the core of Hebrew verbs",
                route = "binyanim"
            ),
            FeatureCard(
                titleHebrew = "שורשים",
                titleEnglish = "Verb Roots",
                icon = Icons.Default.Grain,
                description = "Understanding trilateral roots (3-consonant patterns)",
                route = "roots"
            ),
            FeatureCard(
                titleHebrew = "פעלים נפוצים",
                titleEnglish = "Common Verbs",
                icon = Icons.Default.List,
                description = "100+ most frequently used Hebrew verbs across all binyanim",
                route = "verbs"
            ),
            FeatureCard(
                titleHebrew = "הווה",
                titleEnglish = "Present Tense",
                icon = Icons.Default.Today,
                description = "Present tense conjugations (4 forms: masc/fem, sg/pl)",
                route = "present"
            ),
            FeatureCard(
                titleHebrew = "עבר",
                titleEnglish = "Past Tense",
                icon = Icons.Default.History,
                description = "Past tense with person, gender, number agreement",
                route = "past"
            ),
            FeatureCard(
                titleHebrew = "עתיד",
                titleEnglish = "Future Tense",
                icon = Icons.Default.Schedule,
                description = "Future tense conjugations",
                route = "future"
            ),
            FeatureCard(
                titleHebrew = "פעיל/סביל",
                titleEnglish = "Active/Passive Pairs",
                icon = Icons.Default.SwapHoriz,
                description = "Understanding binyan pairs (Pa'al/Nif'al, Pi'el/Pu'al, Hif'il/Huf'al)",
                route = "pairs"
            ),
            FeatureCard(
                titleHebrew = "פעלים חוזרים",
                titleEnglish = "Reflexive Verbs",
                icon = Icons.Default.Autorenew,
                description = "Hitpa'el binyan (reflexive/reciprocal actions)",
                route = "reflexive"
            ),
            FeatureCard(
                titleHebrew = "פעלים חריגים",
                titleEnglish = "Irregular Verbs",
                icon = Icons.Default.Warning,
                description = "Special roots and irregular patterns",
                route = "irregular"
            ),
            FeatureCard(
                titleHebrew = "תרגול",
                titleEnglish = "Practice Quiz",
                icon = Icons.Default.Quiz,
                description = "Interactive binyan and conjugation practice",
                route = "quiz"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "פעלים עבריים",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Hebrew Verbs AI",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleDarkMode) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle theme"
                        )
                    }
                    IconButton(onClick = onNavigateToScanner) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Scanner"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats section
            item {
                Text(
                    text = "Your Progress",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Verbs Learned",
                        value = "15",
                        icon = {
                            Icon(
                                Icons.Default.Book,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Binyanim",
                        value = "7",
                        icon = {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        modifier = Modifier.weight(1f),
                        gradientColors = listOf(Color(0xFFEC4899), Color(0xFFF59E0B))
                    )
                }
            }

            // AI Chat quick access
            item {
                AnimatedGradientButton(
                    text = "💬 Ask AI About Hebrew Verbs",
                    onClick = onNavigateToChat,
                    modifier = Modifier.fillMaxWidth(),
                    gradientColors = listOf(
                        Color(0xFF14B8A6),
                        Color(0xFF6366F1),
                        Color(0xFF8B5CF6)
                    )
                )
            }

            // Features section
            item {
                Text(
                    text = "Learning Features",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                )
            }

            items(features) { feature ->
                FeatureCardItem(
                    feature = feature,
                    onClick = { onNavigateToFeature(feature.route) },
                    onAskAI = onNavigateToChat
                )
            }
        }
    }
}

@Composable
fun FeatureCardItem(
    feature: FeatureCard,
    onClick: () -> Unit,
    onAskAI: () -> Unit
) {
    GlassmorphicCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = feature.icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = feature.titleHebrew,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Right
                    )
                    Text(
                        text = feature.titleEnglish,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = feature.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            IconButton(onClick = onAskAI) {
                PulsingIcon(
                    icon = {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = "Ask AI",
                            tint = Color(0xFF8B5CF6)
                        )
                    }
                )
            }
        }
    }
}
