package com.kreggscode.hebrewverbs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kreggscode.hebrewverbs.ui.screens.ChatScreen
import com.kreggscode.hebrewverbs.ui.screens.HomeScreen
import com.kreggscode.hebrewverbs.ui.theme.HebrewVerbsAITheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val ComponentActivity.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val scope = rememberCoroutineScope()
            val darkModeKey = booleanPreferencesKey("dark_mode")
            val isDarkMode by dataStore.data
                .map { preferences -> preferences[darkModeKey] ?: false }
                .collectAsState(initial = false)

            HebrewVerbsAITheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HebrewVerbsApp(
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = {
                            scope.launch {
                                dataStore.edit { preferences ->
                                    preferences[darkModeKey] = !isDarkMode
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HebrewVerbsApp(
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToFeature = { route ->
                    // Navigate to feature screens (to be implemented)
                    navController.navigate(route)
                },
                onNavigateToScanner = {
                    navController.navigate("scanner")
                },
                onNavigateToChat = {
                    navController.navigate("chat")
                },
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode
            )
        }

        composable("chat") {
            ChatScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("scanner") {
            // Scanner screen placeholder
            ScannerPlaceholder(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Feature screens placeholders
        composable("binyanim") {
            FeaturePlaceholder("The 7 Binyanim") {
                navController.popBackStack()
            }
        }

        composable("verbs") {
            FeaturePlaceholder("Common Verbs") {
                navController.popBackStack()
            }
        }

        composable("roots") {
            FeaturePlaceholder("Verb Roots") {
                navController.popBackStack()
            }
        }

        composable("present") {
            FeaturePlaceholder("Present Tense") {
                navController.popBackStack()
            }
        }

        composable("past") {
            FeaturePlaceholder("Past Tense") {
                navController.popBackStack()
            }
        }

        composable("future") {
            FeaturePlaceholder("Future Tense") {
                navController.popBackStack()
            }
        }

        composable("pairs") {
            FeaturePlaceholder("Active/Passive Pairs") {
                navController.popBackStack()
            }
        }

        composable("reflexive") {
            FeaturePlaceholder("Reflexive Verbs") {
                navController.popBackStack()
            }
        }

        composable("irregular") {
            FeaturePlaceholder("Irregular Verbs") {
                navController.popBackStack()
            }
        }

        composable("quiz") {
            FeaturePlaceholder("Practice Quiz") {
                navController.popBackStack()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerPlaceholder(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scanner") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("Scanner feature - Coming soon!")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturePlaceholder(title: String, onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text("$title - Coming soon!")
        }
    }
}
