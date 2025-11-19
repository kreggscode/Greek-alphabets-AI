package com.kreggscode.greekalphabets.utils

import android.icu.text.Transliterator
import java.util.Locale

fun pronounceGreek(greek: String): String {
    if (!containsGreek(greek)) return ""

    return try {
        // Greek to Latin transliteration mapping for characters
        val charMap = mapOf(
            // Uppercase letters
            'Α' to "A", 'Β' to "V", 'Γ' to "G", 'Δ' to "D", 'Ε' to "E",
            'Ζ' to "Z", 'Η' to "I", 'Θ' to "Th", 'Ι' to "I", 'Κ' to "K",
            'Λ' to "L", 'Μ' to "M", 'Ν' to "N", 'Ξ' to "X", 'Ο' to "O",
            'Π' to "P", 'Ρ' to "R", 'Σ' to "S", 'Τ' to "T", 'Υ' to "Y",
            'Φ' to "F", 'Χ' to "Ch", 'Ψ' to "Ps", 'Ω' to "O",
            // Lowercase letters
            'α' to "a", 'β' to "v", 'γ' to "g", 'δ' to "d", 'ε' to "e",
            'ζ' to "z", 'η' to "i", 'θ' to "th", 'ι' to "i", 'κ' to "k",
            'λ' to "l", 'μ' to "m", 'ν' to "n", 'ξ' to "x", 'ο' to "o",
            'π' to "p", 'ρ' to "r", 'σ' to "s", 'ς' to "s", 'τ' to "t",
            'υ' to "y", 'φ' to "f", 'χ' to "ch", 'ψ' to "ps", 'ω' to "o",
            // Accented vowels (lowercase)
            'ά' to "a", 'έ' to "e", 'ή' to "i", 'ί' to "i", 'ό' to "o",
            'ύ' to "y", 'ώ' to "o",
            // Accented vowels (uppercase)
            'Ά' to "A", 'Έ' to "E", 'Ή' to "I", 'Ί' to "I", 'Ό' to "O",
            'Ύ' to "Y", 'Ώ' to "O"
        )
        
        // Diphthong mapping (must be handled before single characters)
        val diphthongMap = mapOf(
            "ΑΥ" to "av", "ΕΥ" to "ev", "ΗΥ" to "iv", "ΟΥ" to "ou",
            "ΑΙ" to "ai", "ΕΙ" to "ei", "ΟΙ" to "oi",
            "αυ" to "av", "ευ" to "ev", "ηυ" to "iv", "ου" to "ou",
            "αι" to "ai", "ει" to "ei", "οι" to "oi"
        )

        var result = greek.trim()
            .replace(Regex("\\s+"), " ")
        
        // Handle diphthongs first (replace longer sequences before single characters)
        diphthongMap.forEach { (diphthong, replacement) ->
            result = result.replace(diphthong, replacement, ignoreCase = false)
        }
        
        // Convert remaining characters
        result = result.map { char ->
            charMap[char] ?: char.toString()
        }.joinToString("")
        
        result.lowercase(Locale.ROOT)
    } catch (e: Exception) {
        ""
    }
}

fun containsGreek(text: String): Boolean {
    // Check for Greek alphabet characters (Unicode range \u0370-\u03FF)
    val greekUnicodeRange = 0x0370..0x03FF
    if (text.any { it.code in greekUnicodeRange }) return true
    
    // Check for common Greek word patterns
    val greekPatterns = listOf("είμαι", "έχω", "και", "για", "με", "από", "στο", "στον", "στην", "στο")
    val lowerText = text.lowercase()
    return greekPatterns.any { lowerText.contains(it) }
}

