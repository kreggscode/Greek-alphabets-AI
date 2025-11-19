package com.kreggscode.greekalphabets.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : NavigationItem(
        route = "home",
        title = "Home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )
    
    object words : NavigationItem(
        route = "words",
        title = "words",
        icon = Icons.Outlined.MenuBook,
        selectedIcon = Icons.Filled.MenuBook
    )
    
    object alphabet : NavigationItem(
        route = "alphabet",
        title = "alphabet",
        icon = Icons.Outlined.Translate,
        selectedIcon = Icons.Filled.Translate
    )
    
    object Quiz : NavigationItem(
        route = "quiz",
        title = "Quiz",
        icon = Icons.Outlined.Quiz,
        selectedIcon = Icons.Filled.Quiz
    )
    
    object AI : NavigationItem(
        route = "ai",
        title = "AI Chat",
        icon = Icons.Outlined.Psychology,
        selectedIcon = Icons.Filled.Psychology
    )
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object words : Screen("words")
    object VerbDetail : Screen("verb_detail/{verbId}") {
        fun createRoute(verbId: String) = "verb_detail/$verbId"
    }
    object alphabet : Screen("alphabet")
    object Quiz : Screen("quiz")
    object AI : Screen("ai")
    object Scanner : Screen("scanner")
    object VerbCategory : Screen("verb_category/{category}") {
        fun createRoute(category: String) = "verb_category/$category"
    }
    object GreekCulture : Screen("Greek_culture")
    object GreekCuisine : Screen("Greek_cuisine")
    object BasicGrammar : Screen("basic_grammar")
    object Translator : Screen("translator")
    object About : Screen("about")
    object PrivacyPolicy : Screen("privacy_policy")
    object Terms : Screen("terms")
    object GreekPhrases : Screen("greek_phrases")
}
