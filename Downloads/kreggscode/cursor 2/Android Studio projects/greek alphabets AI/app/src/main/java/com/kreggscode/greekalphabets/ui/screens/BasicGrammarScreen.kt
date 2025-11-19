package com.kreggscode.greekalphabets.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kreggscode.greekalphabets.ui.components.GlassmorphicCard
import com.kreggscode.greekalphabets.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun BasicGrammarScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Basics", "Nouns", "Verbs", "Adjectives", "Word Order")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            androidx.compose.foundation.shape.CircleShape
                        )
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Basic Grammar",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Scrollable Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Visible
                            )
                        }
                    )
                }
            }

            // Content
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) + slideInHorizontally() togetherWith
                    fadeOut(animationSpec = tween(300)) + slideOutHorizontally()
                },
                label = "grammar_tab_content"
            ) { tab: Int ->
                when (tab) {
                    0 -> GrammarBasicsContent()
                    1 -> NounsContent()
                    2 -> VerbsContent()
                    3 -> AdjectivesContent()
                    4 -> WordOrderContent()
                    else -> GrammarBasicsContent()
                }
            }
        }
    }
}

@Composable
fun GrammarBasicsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GrammarCard(
                title = "Greek Alphabet",
                content = "Greek uses its own alphabet with 24 letters.\n\n" +
                        "• Α-Ω (24 letters)\n" +
                        "• Vowels: Α, Ε, Η, Ι, Ο, Υ, Ω\n" +
                        "• Example: αγάπη (love), φίλος (friend), σπίτι (house)"
            )
        }
        item {
            GrammarCard(
                title = "Definite Articles",
                content = "Greek has definite articles that agree with gender and case.\n\n" +
                        "• Masculine: ο (o) - ο φίλος (the friend)\n" +
                        "• Feminine: η (i) - η γυναίκα (the woman)\n" +
                        "• Neuter: το (to) - το σπίτι (the house)"
            )
        }
        item {
            GrammarCard(
                title = "Gender System",
                content = "All Greek nouns have one of three genders.\n\n" +
                        "• Masculine: ο άντρας (the man)\n" +
                        "• Feminine: η γυναίκα (the woman)\n" +
                        "• Neuter: το παιδί (the child)\n" +
                        "• Gender affects adjectives and articles"
            )
        }
        item {
            GrammarCard(
                title = "Plural Forms",
                content = "Greek nouns change endings in plural.\n\n" +
                        "• Masculine: ο φίλος → οι φίλοι (the friend → the friends)\n" +
                        "• Feminine: η γυναίκα → οι γυναίκες (the woman → the women)\n" +
                        "• Neuter: το σπίτι → τα σπίτια (the house → the houses)"
            )
        }
    }
}

@Composable
fun NounsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GrammarCard(
                title = "Noun Declensions",
                content = "Greek nouns change endings based on case.\n\n" +
                        "• Nominative: ο φίλος (the friend - subject)\n" +
                        "• Genitive: του φίλου (of the friend - possession)\n" +
                        "• Accusative: τον φίλο (the friend - object)\n" +
                        "• Vocative: φίλε (friend - calling someone)"
            )
        }
        item {
            GrammarCard(
                title = "Case System",
                content = "Greek uses four cases for nouns.\n\n" +
                        "• Nominative: subject of sentence\n" +
                        "• Genitive: shows possession (του, της, των)\n" +
                        "• Accusative: direct object\n" +
                        "• Vocative: addressing someone"
            )
        }
        item {
            GrammarCard(
                title = "Possessive",
                content = "Show possession using genitive case.\n\n" +
                        "• του αντρόπου (the man's)\n" +
                        "• της γυναίκας (the woman's)\n" +
                        "• του παιδιού (the child's)"
            )
        }
        item {
            GrammarCard(
                title = "Common Patterns",
                content = "• Masculine -ος: ο φίλος (friend), ο άντρας (man)\n" +
                        "• Feminine -α: η γυναίκα (woman), η μητέρα (mother)\n" +
                        "• Neuter -ο: το παιδί (child), το σπίτι (house)\n" +
                        "• Many exceptions exist - must memorize!"
            )
        }
    }
}

@Composable
fun VerbsContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GrammarCard(
                title = "Present Tense",
                content = "Greek verbs conjugate by person and number.\n\n" +
                        "• εγώ (I): γράφω (I write)\n" +
                        "• εσύ (you): γράφεις (you write)\n" +
                        "• αυτός/αυτή (he/she): γράφει (he/she writes)\n" +
                        "• εμείς (we): γράφουμε (we write)"
            )
        }
        item {
            GrammarCard(
                title = "Past Tense (Aorist)",
                content = "Simple past tense in Greek.\n\n" +
                        "• έγραψα (I wrote)\n" +
                        "• έγραψες (you wrote)\n" +
                        "• έγραψε (he/she wrote)\n" +
                        "• γράψαμε (we wrote)"
            )
        }
        item {
            GrammarCard(
                title = "Infinitive",
                content = "Greek infinitives end in -ω.\n\n" +
                        "• να γράφω (to write)\n" +
                        "• να διαβάζω (to read)\n" +
                        "• να τρώω (to eat)\n" +
                        "• 'να' is the particle for infinitive"
            )
        }
        item {
            GrammarCard(
                title = "Common Verbs",
                content = "• είμαι (be): είμαι (I am), είσαι (you are)\n" +
                        "• έχω (have): έχω (I have), έχεις (you have)\n" +
                        "• πηγαίνω (go): πηγαίνω (I go)\n" +
                        "• έρχομαι (come): έρχομαι (I come)"
            )
        }
    }
}

@Composable
fun AdjectivesContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GrammarCard(
                title = "Agreement",
                content = "Adjectives must agree with noun in gender, number, and case.\n\n" +
                        "• Masculine: μεγάλος φίλος (big friend)\n" +
                        "• Feminine: μεγάλη γυναίκα (big woman)\n" +
                        "• Neuter: μεγάλο σπίτι (big house)\n" +
                        "• Plural: μεγάλοι φίλοι (big friends)"
            )
        }
        item {
            GrammarCard(
                title = "Endings",
                content = "Adjective endings match noun endings.\n\n" +
                        "• -ος/-η/-ο pattern: καλός, καλή, καλό (good)\n" +
                        "• -ος/-ια/-ο pattern: ωραίος, ωραία, ωραίο (beautiful)\n" +
                        "• Some adjectives are indeclinable"
            )
        }
        item {
            GrammarCard(
                title = "Common Adjectives",
                content = "• καλός/καλή/καλό (good)\n" +
                        "• κακός/κακή/κακό (bad)\n" +
                        "• μεγάλος/μεγάλη/μεγάλο (big)\n" +
                        "• μικρός/μικρή/μικρό (small)"
            )
        }
        item {
            GrammarCard(
                title = "Comparatives",
                content = "Use πιο (more) + adjective for comparative.\n\n" +
                        "• καλός → πιο καλός (good → better)\n" +
                        "• Superlative: ο πιο καλός (the best)\n" +
                        "• Irregular: καλός → καλύτερος → καλύτερος (better/best)"
            )
        }
    }
}

@Composable
fun WordOrderContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GrammarCard(
                title = "Basic Word Order",
                content = "Greek typically follows Subject-Verb-Object (SVO).\n\n" +
                        "• Εγώ γράφω ένα γράμμα (I write a letter)\n" +
                        "• Αυτή διαβάζει ένα βιβλίο (She reads a book)\n" +
                        "• But word order is flexible due to cases"
            )
        }
        item {
            GrammarCard(
                title = "Flexible Word Order",
                content = "Because of case endings, word order can vary.\n\n" +
                        "• Ο φίλος βλέπει τη γυναίκα (The friend sees the woman)\n" +
                        "• Τη γυναίκα βλέπει ο φίλος (The woman sees the friend - emphasis)\n" +
                        "• Cases show relationships, not word order"
            )
        }
        item {
            GrammarCard(
                title = "Questions",
                content = "Questions use question words or intonation.\n\n" +
                        "• Τι κάνεις; (What are you doing?)\n" +
                        "• Πού πηγαίνεις; (Where are you going?)\n" +
                        "• Or simply raise intonation: Πηγαίνεις; (Are you going?)"
            )
        }
        item {
            GrammarCard(
                title = "Question Words",
                content = "Common Greek question words:\n\n" +
                        "• Τι (What): Τι κάνεις; (What are you doing?)\n" +
                        "• Πού (Where): Πού είσαι; (Where are you?)\n" +
                        "• Πώς (How): Πώς είσαι; (How are you?)\n" +
                        "• Ποιος (Who): Ποιος είναι; (Who is it?)"
            )
        }
    }
}

@Composable
fun GrammarCard(title: String, content: String) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PremiumIndigo
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = content,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )
        }
    }
}
