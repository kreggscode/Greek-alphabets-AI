package com.kreggscode.greekalphabets.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.greekalphabets.ui.theme.PremiumIndigo

sealed class TranslationStatus {
    object Idle : TranslationStatus()
    object Loading : TranslationStatus()
    object Dictionary : TranslationStatus()
    object Machine : TranslationStatus()
    object DetectedGreek : TranslationStatus()
    data class Error(val message: String) : TranslationStatus()
}

@Composable
fun TranslationStatusView(
    status: TranslationStatus,
    modifier: Modifier = Modifier,
    loadingMessage: String = "Translating..."
) {
    when (status) {
        TranslationStatus.Idle -> Spacer(modifier = Modifier.height(4.dp))
        TranslationStatus.Loading -> Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(18.dp),
                strokeWidth = 2.dp,
                color = PremiumIndigo
            )
            Text(
                text = loadingMessage,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        TranslationStatus.Dictionary -> StatusBadge(
            modifier = modifier,
            text = "Dictionary match",
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
        TranslationStatus.Machine -> StatusBadge(
            modifier = modifier,
            text = "ML translation",
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
        TranslationStatus.DetectedGreek -> StatusBadge(
            modifier = modifier,
            text = "Detected Greek text",
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
        is TranslationStatus.Error -> StatusBadge(
            modifier = modifier,
            text = status.message,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
private fun StatusBadge(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

