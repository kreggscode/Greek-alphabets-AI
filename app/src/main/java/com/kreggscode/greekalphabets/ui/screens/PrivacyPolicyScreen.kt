package com.kreggscode.greekalphabets.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kreggscode.greekalphabets.ui.components.GlassmorphicCard
import com.kreggscode.greekalphabets.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
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
                PolicySection(
                    title = "Introduction",
                    content = "Greek Words ('we', 'our', or 'us') is committed to protecting your privacy. This Privacy Policy explains how we collect, use, and safeguard your information when you use our mobile application."
                )
            }
            item {
                PolicySection(
                    title = "Information We Collect",
                    content = "We collect minimal information necessary to provide our services:\n\n" +
                            "• Learning Progress: Words you've studied, quiz results, and favorites\n" +
                            "• App Usage: Features you use to improve the app experience\n" +
                            "• Device Information: Basic device info for app functionality\n\n" +
                            "We do NOT collect:\n" +
                            "• Personal identification information\n" +
                            "• Payment information\n" +
                            "• Location data\n" +
                            "• Contact information"
                )
            }
            item {
                PolicySection(
                    title = "How We Use Information",
                    content = "We use collected information to:\n\n" +
                            "• Provide and improve our learning services\n" +
                            "• Track your learning progress\n" +
                            "• Personalize your learning experience\n" +
                            "• Fix bugs and improve app performance\n" +
                            "• Analyze app usage patterns"
                )
            }
            item {
                PolicySection(
                    title = "Data Storage",
                    content = "All data is stored locally on your device. We do not transmit your learning data to external servers. Your progress and favorites are stored securely on your device."
                )
            }
            item {
                PolicySection(
                    title = "Third-Party Services",
                    content = "Our app uses the following third-party services:\n\n" +
                            "• Google ML Kit: For text recognition and translation (processed locally)\n" +
                            "• AI Services: For AI tutor functionality\n\n" +
                            "These services may have their own privacy policies. We recommend reviewing them."
                )
            }
            item {
                PolicySection(
                    title = "Your Rights",
                    content = "You have the right to:\n\n" +
                            "• Access your learning data\n" +
                            "• Delete your data (uninstall the app)\n" +
                            "• Opt out of data collection (limited functionality)\n" +
                            "• Request information about data we hold"
                )
            }
            item {
                PolicySection(
                    title = "Children's Privacy",
                    content = "Our app is suitable for all ages. We do not knowingly collect personal information from children. All data is stored locally on the device."
                )
            }
            item {
                PolicySection(
                    title = "Changes to Privacy Policy",
                    content = "We may update this Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy in the app. Changes are effective immediately upon posting."
                )
            }
            item {
                PolicySection(
                    title = "Contact Us",
                    content = "If you have questions about this Privacy Policy, please contact us through the app or visit our support page."
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Last updated: November 2025",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Composable
fun PolicySection(title: String, content: String) {
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

