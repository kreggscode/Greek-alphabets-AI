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
fun TermsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms & Conditions") },
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
                TermsSection(
                    title = "Agreement to Terms",
                    content = "By downloading, installing, or using the Greek Words mobile application, you agree to be bound by these Terms and Conditions. If you do not agree, please do not use the app."
                )
            }
            item {
                TermsSection(
                    title = "Use License",
                    content = "Permission is granted to temporarily download one copy of Greek Words for personal, non-commercial use only. This is a license, not a transfer of title, and under this license you may not:\n\n" +
                            "• Modify or copy the materials\n" +
                            "• Use the materials for commercial purposes\n" +
                            "• Attempt to reverse engineer the software\n" +
                            "• Remove any copyright or proprietary notations"
                )
            }
            item {
                TermsSection(
                    title = "User Responsibilities",
                    content = "You agree to:\n\n" +
                            "• Use the app only for lawful purposes\n" +
                            "• Not interfere with app functionality\n" +
                            "• Not attempt to gain unauthorized access\n" +
                            "• Respect intellectual property rights\n" +
                            "• Provide accurate information when required"
                )
            }
            item {
                TermsSection(
                    title = "Intellectual Property",
                    content = "All content, features, and functionality of Greek Words, including but not limited to text, graphics, logos, and software, are the property of the app developers and are protected by copyright, trademark, and other intellectual property laws."
                )
            }
            item {
                TermsSection(
                    title = "Disclaimer",
                    content = "The materials in Greek Words are provided on an 'as is' basis. We make no warranties, expressed or implied, and hereby disclaim all other warranties including, without limitation, implied warranties of merchantability, fitness for a particular purpose, or non-infringement of intellectual property."
                )
            }
            item {
                TermsSection(
                    title = "Limitations",
                    content = "In no event shall Greek Words or its developers be liable for any damages (including, without limitation, damages for loss of data or profit, or due to business interruption) arising out of the use or inability to use the app, even if we have been notified of the possibility of such damage."
                )
            }
            item {
                TermsSection(
                    title = "Accuracy of Materials",
                    content = "The materials appearing in Greek Words could include technical, typographical, or photographic errors. We do not warrant that any of the materials are accurate, complete, or current. We may make changes to the materials at any time without notice."
                )
            }
            item {
                TermsSection(
                    title = "Modifications",
                    content = "We may revise these Terms and Conditions at any time without notice. By using Greek Words, you are agreeing to be bound by the then current version of these Terms and Conditions."
                )
            }
            item {
                TermsSection(
                    title = "Termination",
                    content = "We may terminate or suspend your access to Greek Words immediately, without prior notice, for any reason, including breach of these Terms. Upon termination, your right to use the app will cease immediately."
                )
            }
            item {
                TermsSection(
                    title = "Governing Law",
                    content = "These Terms and Conditions are governed by and construed in accordance with applicable laws. You agree to submit to the jurisdiction of the courts in the applicable jurisdiction."
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
fun TermsSection(title: String, content: String) {
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

