package com.kreggscode.greekalphabets.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kreggscode.greekalphabets.ui.components.GlassmorphicCard
import com.kreggscode.greekalphabets.ui.theme.*
import com.kreggscode.greekalphabets.utils.pronounceGreek
import com.kreggscode.greekalphabets.utils.speakText
import java.util.*

data class GreekPhrase(
    val greek: String,
    val romanization: String,
    val english: String,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreekPhrasesScreen(navController: NavController) {
    val context = LocalContext.current
    
    // TTS Setup
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
    
    val phrases = remember {
        listOf(
            // Greetings
            GreekPhrase("Γεια σας!", "YAH sahss", "Hello! (Formal)", "Greetings"),
            GreekPhrase("Γεια σου!", "YAH soo", "Hello! (Casual)", "Greetings"),
            GreekPhrase("Καλημέρα", "kah-lee-MEH-rah", "Good morning", "Greetings"),
            GreekPhrase("Καλησπέρα", "kah-lee-SPEH-rah", "Good evening", "Greetings"),
            GreekPhrase("Καληνύχτα", "kah-lee-NEEKH-tah", "Good night", "Greetings"),
            GreekPhrase("Χαίρετε", "HEH-reh-teh", "Greetings (Formal)", "Greetings"),
            GreekPhrase("Τι κάνεις;", "tee KAH-nees", "How are you? (Casual)", "Greetings"),
            GreekPhrase("Τι κάνετε;", "tee KAH-neh-teh", "How are you? (Formal)", "Greetings"),
            GreekPhrase("Καλά είμαι", "kah-LAH EE-meh", "I'm fine", "Greetings"),
            GreekPhrase("Πολύ καλά", "poh-LEE kah-LAH", "Very well", "Greetings"),
            
            // Common Expressions
            GreekPhrase("Ευχαριστώ", "ef-khah-rees-TOH", "Thank you", "Common"),
            GreekPhrase("Παρακαλώ", "pah-rah-kah-LOH", "Please/You're welcome", "Common"),
            GreekPhrase("Συγγνώμη", "see-GHNO-mee", "Excuse me/Sorry", "Common"),
            GreekPhrase("Ναι", "neh", "Yes", "Common"),
            GreekPhrase("Όχι", "OH-khee", "No", "Common"),
            GreekPhrase("Μάλλον", "MAH-lohn", "Maybe/Probably", "Common"),
            GreekPhrase("Βεβαίως", "veh-VEH-ohs", "Of course", "Common"),
            GreekPhrase("Δεν πειράζει", "then pee-RAH-zee", "No problem/It's okay", "Common"),
            GreekPhrase("Εντάξει", "en-DAH-ksee", "Okay/Alright", "Common"),
            GreekPhrase("Συγνώμη για την αναμονή", "see-GHNO-mee yah teen ah-nah-moh-NEE", "Sorry for the wait", "Common"),
            
            // Introductions
            GreekPhrase("Πώς σε λένε;", "pohs seh LEH-neh", "What's your name? (Casual)", "Introductions"),
            GreekPhrase("Πώς σας λένε;", "pohs sahss LEH-neh", "What's your name? (Formal)", "Introductions"),
            GreekPhrase("Με λένε...", "meh LEH-neh", "My name is...", "Introductions"),
            GreekPhrase("Χαίρω πολύ", "HEH-roh poh-LEE", "Nice to meet you", "Introductions"),
            GreekPhrase("Κι εγώ", "kee eh-GHOH", "Me too", "Introductions"),
            GreekPhrase("Από πού είσαι;", "ah-POH poo EE-seh", "Where are you from? (Casual)", "Introductions"),
            GreekPhrase("Από πού είστε;", "ah-POH poo EE-steh", "Where are you from? (Formal)", "Introductions"),
            GreekPhrase("Είμαι από...", "EE-meh ah-POH", "I'm from...", "Introductions"),
            
            // Questions
            GreekPhrase("Τι;", "tee", "What?", "Questions"),
            GreekPhrase("Πού;", "poo", "Where?", "Questions"),
            GreekPhrase("Πώς;", "pohs", "How?", "Questions"),
            GreekPhrase("Πότε;", "POH-teh", "When?", "Questions"),
            GreekPhrase("Γιατί;", "yah-TEE", "Why?", "Questions"),
            GreekPhrase("Ποιος;", "pyohss", "Who?", "Questions"),
            GreekPhrase("Πόσο;", "POH-soh", "How much?", "Questions"),
            GreekPhrase("Πόσα;", "POH-sah", "How many?", "Questions"),
            GreekPhrase("Πού είσαι;", "poo EE-seh", "Where are you?", "Questions"),
            GreekPhrase("Πού είναι;", "poo EE-neh", "Where is...?", "Questions"),
            
            // Communication
            GreekPhrase("Δεν καταλαβαίνω", "then kah-tah-lah-VEH-noh", "I don't understand", "Communication"),
            GreekPhrase("Μιλάτε αγγλικά;", "mee-LAH-teh ahng-lee-KAH", "Do you speak English?", "Communication"),
            GreekPhrase("Μιλάω λίγα ελληνικά", "mee-LAH-oh LEE-ghah eh-lee-nee-KAH", "I speak a little Greek", "Communication"),
            GreekPhrase("Μπορείτε να μιλήσετε πιο αργά;", "boh-REH-teh nah mee-LEE-seh-teh pyoh ar-GHAH", "Can you speak slower?", "Communication"),
            GreekPhrase("Μπορείτε να το επαναλάβετε;", "boh-REH-teh nah toh eh-pah-nah-LAH-veh-teh", "Can you repeat that?", "Communication"),
            GreekPhrase("Τι είπατε;", "tee EE-pah-teh", "What did you say?", "Communication"),
            GreekPhrase("Πώς λέγεται αυτό;", "pohs LEH-yeh-teh ahf-TOH", "How do you say this?", "Communication"),
            
            // Shopping & Money
            GreekPhrase("Πόσο κοστίζει;", "POH-soh kohs-TEE-zee", "How much does it cost?", "Shopping"),
            GreekPhrase("Αυτό παρακαλώ", "ahf-TOH pah-rah-kah-LOH", "This one, please", "Shopping"),
            GreekPhrase("Έχετε...;", "EH-kheh-teh", "Do you have...?", "Shopping"),
            GreekPhrase("Θα ήθελα...", "thah EE-theh-lah", "I would like...", "Shopping"),
            GreekPhrase("Το ταμείο", "toh tah-MEE-oh", "The cashier", "Shopping"),
            GreekPhrase("Η απόδειξη", "ee ah-POH-dee-ksee", "The receipt", "Shopping"),
            GreekPhrase("Ακριβό", "ah-kree-VOH", "Expensive", "Shopping"),
            GreekPhrase("Φθηνό", "fthih-NOH", "Cheap", "Shopping"),
            
            // Food & Dining
            GreekPhrase("Καλή όρεξη", "kah-LEE OH-reh-ksee", "Enjoy your meal", "Food"),
            GreekPhrase("Στην υγεία σας", "steen ee-YEE-ah sahss", "Cheers/To your health", "Food"),
            GreekPhrase("Το μενού", "toh meh-NOO", "The menu", "Food"),
            GreekPhrase("Ο λογαριασμός", "oh loh-ghah-ree-ahss-MOHSS", "The bill", "Food"),
            GreekPhrase("Νερό", "neh-ROH", "Water", "Food"),
            GreekPhrase("Καφές", "kah-FEHS", "Coffee", "Food"),
            GreekPhrase("Ψωμί", "psoh-MEE", "Bread", "Food"),
            GreekPhrase("Γλυκό", "ghlee-KOH", "Sweet/Dessert", "Food"),
            
            // Directions & Travel
            GreekPhrase("Πού είναι το...;", "poo EE-neh toh", "Where is the...?", "Travel"),
            GreekPhrase("Πώς πάω στο...;", "pohs PAH-oh stoh", "How do I get to...?", "Travel"),
            GreekPhrase("Ευθεία", "ef-THEE-ah", "Straight ahead", "Travel"),
            GreekPhrase("Δεξιά", "theh-ksee-AH", "Right", "Travel"),
            GreekPhrase("Αριστερά", "ah-rees-teh-RAH", "Left", "Travel"),
            GreekPhrase("Εδώ", "eh-DOH", "Here", "Travel"),
            GreekPhrase("Εκεί", "eh-KEE", "There", "Travel"),
            GreekPhrase("Κοντά", "kohn-DAH", "Near", "Travel"),
            GreekPhrase("Μακριά", "mah-kree-AH", "Far", "Travel"),
            GreekPhrase("Το αεροδρόμιο", "toh ah-eh-roh-DROH-mee-oh", "The airport", "Travel"),
            GreekPhrase("Το ξενοδοχείο", "toh kseh-noh-doh-KHEE-oh", "The hotel", "Travel"),
            GreekPhrase("Ο σταθμός", "oh stahth-MOHSS", "The station", "Travel"),
            
            // Time & Dates
            GreekPhrase("Τι ώρα είναι;", "tee OH-rah EE-neh", "What time is it?", "Time"),
            GreekPhrase("Σήμερα", "SEE-meh-rah", "Today", "Time"),
            GreekPhrase("Αύριο", "AHV-ree-oh", "Tomorrow", "Time"),
            GreekPhrase("Χθες", "khthehs", "Yesterday", "Time"),
            GreekPhrase("Τώρα", "TOH-rah", "Now", "Time"),
            GreekPhrase("Αργότερα", "ar-GHOH-teh-rah", "Later", "Time"),
            GreekPhrase("Πριν", "preen", "Before", "Time"),
            GreekPhrase("Μετά", "meh-TAH", "After", "Time"),
            
            // Emotions & Feelings
            GreekPhrase("Σ' αγαπώ", "sah-ghah-POH", "I love you", "Emotions"),
            GreekPhrase("Σε μισώ", "seh mee-SOH", "I hate you", "Emotions"),
            GreekPhrase("Χαίρομαι", "HEH-roh-meh", "I'm happy", "Emotions"),
            GreekPhrase("Είμαι λυπημένος", "EE-meh lee-pee-MEH-nohss", "I'm sad", "Emotions"),
            GreekPhrase("Είμαι κουρασμένος", "EE-meh koo-rahss-MEH-nohss", "I'm tired", "Emotions"),
            GreekPhrase("Είμαι πεινασμένος", "EE-meh pee-nahss-MEH-nohss", "I'm hungry", "Emotions"),
            GreekPhrase("Είμαι διψασμένος", "EE-meh theep-sahss-MEH-nohss", "I'm thirsty", "Emotions"),
            
            // Emergency & Help
            GreekPhrase("Βοήθεια!", "voh-EE-thee-ah", "Help!", "Emergency"),
            GreekPhrase("Καλέστε την αστυνομία", "kah-LEH-steh teen ah-stee-noh-MEE-ah", "Call the police", "Emergency"),
            GreekPhrase("Καλέστε έναν γιατρό", "kah-LEH-steh EH-nahn yah-TROH", "Call a doctor", "Emergency"),
            GreekPhrase("Χάθηκα", "KHAH-thee-kah", "I'm lost", "Emergency"),
            GreekPhrase("Ασφάλεια", "ahss-FAH-lee-ah", "Safety", "Emergency"),
            
            // Compliments & Social
            GreekPhrase("Συγχαρητήρια", "seen-khah-ree-TEE-ree-ah", "Congratulations", "Social"),
            GreekPhrase("Καλή τύχη", "kah-LEE TEE-khee", "Good luck", "Social"),
            GreekPhrase("Πολύ ωραία", "poh-LEE oh-REH-ah", "Very nice", "Social"),
            GreekPhrase("Ωραίος!", "oh-REH-ohss", "Great!/Cool!", "Social"),
            GreekPhrase("Μπράβο", "BRAH-voh", "Well done/Bravo", "Social"),
            GreekPhrase("Καλή εβδομάδα", "kah-LEE ev-doh-MAH-thah", "Have a good week", "Social"),
            GreekPhrase("Καλό Σαββατοκύριακο", "kah-LOH sah-vah-toh-KEE-ree-ah-koh", "Have a good weekend", "Social")
        )
    }
    
    val categories = remember {
        phrases.map { it.category }.distinct().sorted()
    }
    
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredPhrases = remember(selectedCategory, searchQuery) {
        phrases.filter { phrase ->
            (selectedCategory == null || phrase.category == selectedCategory) &&
            (searchQuery.isBlank() || 
             phrase.greek.contains(searchQuery, ignoreCase = true) ||
             phrase.romanization.contains(searchQuery, ignoreCase = true) ||
             phrase.english.contains(searchQuery, ignoreCase = true))
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Greek Phrases",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.padding(16.dp)
            )
            
            // Category Filter Chips
            CategoryFilterChips(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { category ->
                    selectedCategory = if (selectedCategory == category) null else category
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            // Phrases List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPhrases) { phrase ->
                    PhraseCard(
                        phrase = phrase,
                        onSpeakGreek = {
                            speakText(
                                tts = tts,
                                locale = Locale.forLanguageTag("el-GR"),
                                text = phrase.greek,
                                context = context,
                                isTtsReady = isTtsReady && supportsGreek
                            )
                        },
                        onSpeakEnglish = {
                            speakText(
                                tts = tts,
                                locale = Locale.US,
                                text = phrase.english,
                                context = context,
                                isTtsReady = isTtsReady && supportsEnglish
                            )
                        },
                        supportsGreek = supportsGreek && isTtsReady,
                        supportsEnglish = supportsEnglish && isTtsReady
                    )
                }
            }
        }
    }
}

@Composable
fun PhraseCard(
    phrase: GreekPhrase,
    onSpeakGreek: () -> Unit,
    onSpeakEnglish: () -> Unit,
    supportsGreek: Boolean,
    supportsEnglish: Boolean
) {
    GlassmorphicCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp,
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Category Badge
            CategoryBadge(
                category = phrase.category,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Greek Text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = phrase.greek,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PremiumIndigo,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Text(
                        text = phrase.romanization,
                        fontSize = 16.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                // Speak Button for Greek
                IconButton(
                    onClick = onSpeakGreek,
                    enabled = supportsGreek,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (supportsGreek) PremiumIndigo.copy(alpha = 0.15f) 
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = "Speak Greek",
                        tint = if (supportsGreek) PremiumIndigo 
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Divider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
            
            // English Translation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = phrase.english,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                
                // Speak Button for English
                IconButton(
                    onClick = onSpeakEnglish,
                    enabled = supportsEnglish,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            if (supportsEnglish) PremiumTeal.copy(alpha = 0.15f) 
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.VolumeUp,
                        contentDescription = "Speak English",
                        tint = if (supportsEnglish) PremiumTeal 
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryBadge(
    category: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(PremiumIndigo.copy(alpha = 0.2f), PremiumPurple.copy(alpha = 0.2f))
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = category,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = PremiumIndigo
        )
    }
}

@Composable
fun CategoryFilterChips(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected("") },
                label = { Text("All") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PremiumIndigo.copy(alpha = 0.2f),
                    selectedLabelColor = PremiumIndigo
                )
            )
        }
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = PremiumIndigo.copy(alpha = 0.2f),
                    selectedLabelColor = PremiumIndigo
                )
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Search phrases...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PremiumIndigo,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        ),
        singleLine = true
    )
}

