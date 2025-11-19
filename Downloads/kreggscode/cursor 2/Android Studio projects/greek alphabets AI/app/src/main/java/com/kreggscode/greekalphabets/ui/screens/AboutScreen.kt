package com.kreggscode.greekalphabets.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kreggscode.greekalphabets.ui.components.GlassmorphicCard
import com.kreggscode.greekalphabets.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About & Learning") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AboutHeader()
            }
            item {
                LearningSection()
            }
            item {
                FeaturesSection()
            }
            item {
                TipsSection()
            }
        }
    }
}

@Composable
fun AboutHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Greek Words",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PremiumIndigo
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Version 1.2",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Master Greek with AI-powered learning",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun LearningSection() {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "📚 How to Learn",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PremiumIndigo
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "1. Start with the Alphabet\n" +
                        "Learn Greek letters (A-Ö) and their sounds.\n\n" +
                        "2. Practice Words\n" +
                        "Browse words by category and practice pronunciation.\n\n" +
                        "3. Take Quizzes\n" +
                        "Test your knowledge with interactive quizzes.\n\n" +
                        "4. Use the Scanner\n" +
                        "Scan text or objects to learn new words.\n\n" +
                        "5. Chat with AI\n" +
                        "Get personalized help from your AI tutor.\n\n" +
                        "6. Study Grammar\n" +
                        "Learn Greek grammar rules and patterns.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun FeaturesSection() {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "✨ Features",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PremiumIndigo
            )
            Spacer(modifier = Modifier.height(12.dp))
            FeatureItem("📖", "Word Dictionary", "2000+ Greek words with pronunciation")
            FeatureItem("🎯", "Interactive Quizzes", "Test your knowledge")
            FeatureItem("📷", "Text Scanner", "Scan and translate text")
            FeatureItem("🤖", "AI Tutor", "Get personalized learning help")
            FeatureItem("📚", "Grammar Guide", "Learn Greek grammar")
            FeatureItem("🎨", "Beautiful UI", "Modern, intuitive design")
        }
    }
}

@Composable
fun FeatureItem(emoji: String, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp,
            modifier = Modifier.width(40.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun TipsSection() {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "💡 Learning Tips",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = PremiumIndigo
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "• Practice daily, even for just 10 minutes\n" +
                        "• Use the pronunciation feature to hear words\n" +
                        "• Review words you've marked as favorites\n" +
                        "• Take quizzes regularly to reinforce learning\n" +
                        "• Use the AI tutor for grammar questions\n" +
                        "• Explore Greek culture and cuisine sections",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )
        }
    }
}

