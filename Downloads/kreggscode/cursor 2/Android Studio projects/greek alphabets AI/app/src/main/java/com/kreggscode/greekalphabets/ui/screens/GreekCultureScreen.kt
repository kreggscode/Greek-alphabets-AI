package com.kreggscode.greekalphabets.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kreggscode.greekalphabets.ui.components.GlassmorphicCard
import com.kreggscode.greekalphabets.ui.theme.*

enum class CultureTab {
    OVERVIEW, FESTIVALS, TRADITIONS, INTERESTING_FACTS, ETIQUETTE
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun GreekCultureScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Festivals", "Traditions", "Facts", "Etiquette")
    
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
                            CircleShape
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
                    text = "Greek Culture",
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
                    fadeIn() + slideInHorizontally() togetherWith fadeOut() + slideOutHorizontally()
                }
            ) { tab ->
                when (tab) {
                    0 -> CultureOverviewContent()
                    1 -> FestivalsContent()
                    2 -> TraditionsContent()
                    3 -> InterestingFactsContent()
                    4 -> EtiquetteContent()
                }
            }
        }
    }
}

@Composable
fun CultureOverviewContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CultureCard(
                title = "Ελληνικός Πολιτισμός",
                subtitle = "Greek Culture",
                description = "Greek culture is one of the world's oldest and most influential, spanning over 3000 years. Known for philosophy, democracy, art, architecture, and a deep appreciation for family, hospitality (φιλοξενία), and celebration of life.",
                gradientColors = listOf(PremiumPurple, PremiumIndigo)
            )
        }
        
        item {
            CultureInfoCard(
                title = "Historical Background",
                items = listOf(
                    "Ancient Greece (3000-146 BC) - Birth of democracy",
                    "Byzantine Empire (330-1453 AD) - Eastern Roman legacy",
                    "Ottoman Rule (1453-1821) - Centuries of occupation",
                    "Modern Greece (1821-present) - Independence and EU membership"
                )
            )
        }
        
        item {
            CultureInfoCard(
                title = "Core Values",
                items = listOf(
                    "Φιλοξενία (Philoxenia) - Hospitality and generosity",
                    "Οικογένεια (Oikogeneia) - Strong family bonds",
                    "Κλέφτης (Klefis) - Spirit of freedom and resistance",
                    "Φιλοτιμία (Philotimia) - Honor and pride"
                )
            )
        }
    }
}

@Composable
fun FestivalsContent() {
    val festivals = listOf(
        FestivalInfo("Πάσχα", "Easter", "Most important religious celebration, lamb, red eggs, midnight liturgy"),
        FestivalInfo("Γιορτή Αγίου Βασιλείου", "New Year's Day", "January 1st, Vasilopita cake with coin, Agios Vasilis gifts"),
        FestivalInfo("Καρναβάλι", "Carnival", "Apokries season, costumes, parades, Lent preparation"),
        FestivalInfo("Επέτειος Εξέγερσης", "Independence Day", "March 25th, military parades, national pride"),
        FestivalInfo("Θεοφάνεια", "Epiphany", "January 6th, blessing of waters, cross diving ceremony")
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(festivals) { festival ->
            FestivalCard(festival = festival)
        }
    }
}

@Composable
fun TraditionsContent() {
    val traditions = listOf(
        TraditionInfo("Καφενείο", "Kafeneio tradition", "Traditional coffee house, social gathering place for men"),
        TraditionInfo("Ονομαστική Εορτή", "Name Day celebration", "More important than birthday, celebrate saint's day"),
        TraditionInfo("Κομπολόι", "Worry beads", "Greek worry beads, used for relaxation and stress relief"),
        TraditionInfo("Χειροποίητα", "Handmade crafts", "Traditional handicrafts: embroidery, pottery, weaving"),
        TraditionInfo("Συγγένεια", "Extended family", "Strong family ties, weekly gatherings, multi-generational homes")
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(traditions) { tradition ->
            TraditionCard(tradition = tradition)
        }
    }
}

@Composable
fun InterestingFactsContent() {
    val facts = listOf(
        "Greece has over 6,000 islands, but only 227 are inhabited",
        "The Greek language has been spoken for over 3,000 years",
        "Greece has 18 UNESCO World Heritage Sites",
        "Ancient Greeks invented democracy, philosophy, and the Olympic Games",
        "Greeks consume more olive oil per capita than any other country",
        "The Greek alphabet is the ancestor of the Latin and Cyrillic alphabets",
        "Greece has one of the longest coastlines in Europe",
        "The country receives over 300 days of sunshine per year",
        "Greek yogurt is famous worldwide for its health benefits",
        "Greece has more archaeological museums than any other country"
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(facts) { fact ->
            FactCard(fact = fact)
        }
    }
}

@Composable
fun EtiquetteContent() {
    val etiquetteRules = listOf(
        EtiquetteRule("Hospitality", "Greeks are very hospitable. Always accept offers of coffee or food as a guest."),
        EtiquetteRule("Greetings", "Kiss on both cheeks common. 'Γεια σας' (Yasas) formal, 'Γεια σου' (Yasou) casual."),
        EtiquetteRule("Eye Contact", "Maintain eye contact during conversation as a sign of respect and attention."),
        EtiquetteRule("Gifts", "Bring small gifts when invited. Flowers (except white), sweets, or wine are appropriate."),
        EtiquetteRule("Dining", "Meals can last hours. Compliment the food. Don't finish your plate immediately - it means you want more.")
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(etiquetteRules) { rule ->
            EtiquetteCard(rule = rule)
        }
    }
}

@Composable
fun CultureCard(
    title: String,
    subtitle: String,
    description: String,
    gradientColors: List<Color>
) {
    GlassmorphicCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun CultureInfoCard(title: String, items: List<String>) {
    GlassmorphicCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            items.forEach { item ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "• ",
                        fontSize = 16.sp,
                        color = PremiumPurple,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

data class FestivalInfo(val Greek: String, val english: String, val description: String)

@Composable
fun FestivalCard(festival: FestivalInfo) {
    GlassmorphicCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = festival.Greek,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = festival.english,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = festival.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}

data class TraditionInfo(val name: String, val description: String, val details: String)

@Composable
fun TraditionCard(tradition: TraditionInfo) {
    GlassmorphicCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = tradition.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = tradition.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tradition.details,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

@Composable
fun FactCard(fact: String) {
    GlassmorphicCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = PremiumAmber,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = fact,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.weight(1f),
                lineHeight = 20.sp
            )
        }
    }
}

data class EtiquetteRule(val title: String, val description: String)

@Composable
fun EtiquetteCard(rule: EtiquetteRule) {
    GlassmorphicCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = null,
                    tint = PremiumIndigo,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = rule.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = rule.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 20.sp
            )
        }
    }
}
