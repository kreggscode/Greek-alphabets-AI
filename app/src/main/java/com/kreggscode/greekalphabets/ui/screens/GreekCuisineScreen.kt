package com.kreggscode.greekalphabets.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun GreekCuisineScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Popular", "Famous", "Regular", "Likes", "Dislikes")
    
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
                    text = "Greek Cuisine",
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
                label = "tab_content"
            ) { tab: Int ->
                when (tab) {
                    0 -> PopularFoodsContent()
                    1 -> FamousFoodsContent()
                    2 -> RegularFoodsContent()
                    3 -> LikedFoodsContent()
                    4 -> DislikedFoodsContent()
                    else -> PopularFoodsContent()
                }
            }
        }
    }
}

@Composable
fun PopularFoodsContent() {
    val popularFoods = listOf(
        FoodInfo("Μουσακάς", "Moussaka", "Greece's most famous baked dish", "Layers of eggplant, meat sauce, and béchamel"),
        FoodInfo("Σουβλάκι", "Souvlaki", "Traditional Greek skewers", "Grilled meat with pita, tomatoes, onions, tzatziki"),
        FoodInfo("Γύρος", "Gyros", "Greek street food favorite", "Rotisserie meat wrapped in pita bread with vegetables"),
        FoodInfo("Παστίτσιο", "Pastitsio", "Greek baked pasta", "Pasta with meat sauce and creamy béchamel topping"),
        FoodInfo("Σπανακόπιτα", "Spanakopita", "Spinach pie", "Phyllo pastry filled with spinach and feta cheese"),
        FoodInfo("Μπακλαβάς", "Baklava", "Sweet pastry dessert", "Layers of phyllo, nuts, honey, and cinnamon")
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(popularFoods) { food ->
            FoodCard(food = food, icon = Icons.Filled.ThumbUp, color = PremiumEmerald)
        }
    }
}

@Composable
fun FamousFoodsContent() {
    val famousFoods = listOf(
        FoodInfo("Γλυκό του κουταλιού", "Spoon sweets", "Traditional preserved fruits", "Fruits preserved in sugar syrup"),
        FoodInfo("Ντολμάδες", "Dolmades", "Stuffed grape leaves", "Rice and herbs wrapped in grape leaves"),
        FoodInfo("Μεζέδες", "Mezedes", "Greek small plates", "Assorted appetizers: olives, cheese, dips"),
        FoodInfo("Ταραμοσαλάτα", "Taramosalata", "Fish roe dip", "Creamy dip made from fish roe, bread, olive oil"),
        FoodInfo("Χωριάτικη σαλάτα", "Greek salad", "Traditional village salad", "Tomatoes, cucumbers, olives, feta, olive oil"),
        FoodInfo("Κοκκινιστό", "Kokkinisto", "Red stewed meat", "Meat cooked in tomato sauce with onions")
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(famousFoods) { food ->
            FoodCard(food = food, icon = Icons.Filled.Star, color = PremiumAmber)
        }
    }
}

@Composable
fun RegularFoodsContent() {
    val regularFoods = listOf(
        FoodInfo("Ψωμί", "Bread", "Traditional Greek bread", "Fresh, crusty, often with sesame seeds"),
        FoodInfo("Φέτα", "Feta cheese", "Traditional Greek cheese", "Salty, crumbly, made from sheep's milk"),
        FoodInfo("Ελιές", "Olives", "Greek table staple", "Kalamata olives, served with most meals"),
        FoodInfo("Τζατζίκι", "Tzatziki", "Yogurt cucumber dip", "Yogurt, cucumber, garlic, dill, olive oil"),
        FoodInfo("Χυμός πορτοκαλιού", "Orange juice", "Fresh morning juice", "Freshly squeezed oranges"),
        FoodInfo("Λουκάνικα", "Loukanika", "Greek sausage", "Seasoned pork sausage, often grilled")
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(regularFoods) { food ->
            FoodCard(food = food, icon = Icons.Filled.LunchDining, color = PremiumIndigo)
        }
    }
}

@Composable
fun LikedFoodsContent() {
    val likedFoods = listOf(
        "Moussaka (Μουσακάς) - Greece's beloved casserole",
        "Baklava (Μπακλαβάς) - Sweet, nutty, honey-soaked pastry",
        "Souvlaki (Σουβλάκι) - Perfect grilled meat skewers",
        "Greek Salad (Χωριάτικη) - Fresh and healthy",
        "Tzatziki (Τζατζίκι) - Cool, refreshing yogurt dip",
        "Pastitsio (Παστίτσιο) - Comforting baked pasta",
        "Spanakopita (Σπανακόπιτα) - Crispy spinach pie",
        "Gyros (Γύρος) - Iconic street food",
        "Feta Cheese (Φέτα) - Salty and delicious",
        "Kalamata Olives - Rich, flavorful olives"
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(likedFoods) { food ->
            LikedFoodCard(food = food)
        }
    }
}

@Composable
fun DislikedFoodsContent() {
    val dislikedFoods = listOf(
        "Ouzo (Ούζο) - Strong anise-flavored alcohol",
        "Octopus (Χταπόδι) - Some find texture challenging",
        "Very Salty Feta - Can be too salty for some",
        "Strong Olive Oil Flavor - Acquired taste",
        "Oxtail Soup - Rich, gelatinous texture"
    )
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GlassmorphicCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Note",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Greek cuisine is celebrated worldwide! These are just foods that some people might find challenging due to strong flavors, unique textures, or acquired tastes. Mediterranean flavors are generally beloved, but some traditional dishes might be an acquired taste.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        items(dislikedFoods) { food ->
            DislikedFoodCard(food = food)
        }
    }
}

data class FoodInfo(
    val Greek: String,
    val english: String,
    val description: String,
    val details: String
)

@Composable
fun FoodCard(food: FoodInfo, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    GlassmorphicCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(color, color.copy(alpha = 0.7f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.Greek,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = food.english,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = food.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = food.details,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun LikedFoodCard(food: String) {
    GlassmorphicCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Favorite,
                contentDescription = null,
                tint = PremiumPink,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = food,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.weight(1f),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun DislikedFoodCard(food: String) {
    GlassmorphicCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = food,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.weight(1f),
                lineHeight = 20.sp
            )
        }
    }
}
