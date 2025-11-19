package com.kreggscode.greekalphabets.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kreggscode.greekalphabets.ui.theme.PremiumIndigo
import com.kreggscode.greekalphabets.ui.theme.PremiumPink
import com.kreggscode.greekalphabets.ui.theme.PremiumPurple

/**
 * Formats AI response text by removing markdown symbols and applying proper styling
 */
fun formatAIResponse(text: String): String {
    return text
        .replace("**", "")  // Remove bold markers
        .replace("*", "")   // Remove italic markers
        .replace("##", "")  // Remove heading markers
        .replace("#", "")   // Remove single heading markers
        .replace("```", "") // Remove code block markers
        .trim()
}

/**
 * Composable that renders formatted AI text with styled headings, bold text, and emphasis
 */
@Composable
fun FormattedAIText(
    text: String,
    modifier: Modifier = Modifier
) {
    val lines = text.split("\n")
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        lines.forEach { line ->
            val trimmedLine = line.trim()
            when {
                // Headings (lines starting with #)
                trimmedLine.startsWith("##") -> {
                    Text(
                        text = trimmedLine.removePrefix("##").trim(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PremiumPurple,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                trimmedLine.startsWith("#") -> {
                    Text(
                        text = trimmedLine.removePrefix("#").trim(),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = PremiumIndigo,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                // Bullet points or numbered lists
                trimmedLine.startsWith("-") || trimmedLine.matches(Regex("^\\d+\\..*")) -> {
                    val cleanLine = trimmedLine
                        .removePrefix("-")
                        .replace(Regex("^\\d+\\."), "")
                        .trim()
                    Row(
                        modifier = Modifier.padding(start = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "•",
                            color = PremiumPink,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        FormattedLine(cleanLine)
                    }
                }
                // Regular paragraphs
                trimmedLine.isNotEmpty() -> {
                    FormattedLine(trimmedLine)
                }
            }
        }
    }
}

@Composable
private fun FormattedLine(line: String) {
    Text(
        text = buildAnnotatedString {
            var currentIndex = 0
            val text = line
            
            // Process markdown formatting
            while (currentIndex < text.length) {
                // Check for bold text **text**
                val boldStart = text.indexOf("**", currentIndex)
                if (boldStart != -1) {
                    // Add text before bold
                    if (boldStart > currentIndex) {
                        appendWithGreekStyling(text.substring(currentIndex, boldStart))
                    }
                    
                    // Find end of bold
                    val boldEnd = text.indexOf("**", boldStart + 2)
                    if (boldEnd != -1) {
                        // Add bold text
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            appendWithGreekStyling(text.substring(boldStart + 2, boldEnd))
                        }
                        currentIndex = boldEnd + 2
                    } else {
                        // No closing **, treat as regular text
                        appendWithGreekStyling(text.substring(currentIndex))
                        currentIndex = text.length
                    }
                } else {
                    // No more formatting, add rest of text
                    appendWithGreekStyling(text.substring(currentIndex))
                    currentIndex = text.length
                }
            }
        },
        fontSize = 15.sp,
        lineHeight = 22.sp,
        color = MaterialTheme.colorScheme.onSurface
    )
}

private fun AnnotatedString.Builder.appendWithGreekStyling(text: String) {
    if (text.isEmpty()) return
    
    // Find and style Greek text
    // Greek alphabet Unicode range: \u0370-\u03FF (Greek and Coptic)
    val greekRegex = Regex("[\\u0370-\\u03FF]+", RegexOption.IGNORE_CASE)
    var lastIndex = 0
    val greekMatches = mutableListOf<Pair<IntRange, String>>()
    
    greekRegex.findAll(text).forEach { match: MatchResult ->
        val word = match.value
        // Check if word contains Greek characters
        if (word.any { it.code in 0x0370..0x03FF }) {
            greekMatches.add(match.range to match.value)
        }
    }
    
    if (greekMatches.isEmpty()) {
        // No Greek text, just append
        append(text)
        return
    }
    
    // Process text with Greek styling
    greekMatches.forEach { match: Pair<IntRange, String> ->
        val (range, word) = match
        // Add text before Greek
        if (range.first > lastIndex) {
            append(text.substring(lastIndex, range.first))
        }
        
        // Add styled Greek text
        withStyle(
            SpanStyle(
                color = PremiumIndigo,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append(word)
        }
        
        lastIndex = range.last + 1
    }
    
    // Add remaining text
    if (lastIndex < text.length) {
        append(text.substring(lastIndex))
    }
}
