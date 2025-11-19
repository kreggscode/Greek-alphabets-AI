package com.kreggscode.hebrewverbs.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verbs")
data class HebrewVerb(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val hebrew: String,
    val transliteration: String,
    val english: String,
    val binyan: Binyan,
    val root: String,
    val rootTransliteration: String,
    val frequencyRank: Int,
    val isFavorite: Boolean = false
)

enum class Binyan(val hebrewName: String, val description: String, val colorHex: String) {
    PAAL("פָּעַל", "Simple Active", "#6366F1"),
    NIFAL("נִפְעַל", "Simple Passive", "#8B5CF6"),
    PIEL("פִּעֵל", "Intensive Active", "#EC4899"),
    PUAL("פֻּעַל", "Intensive Passive", "#F59E0B"),
    HITPAEL("הִתְפַּעֵל", "Reflexive", "#14B8A6"),
    HIFIL("הִפְעִיל", "Causative Active", "#10B981"),
    HUFAL("הֻפְעַל", "Causative Passive", "#EF4444")
}

data class HebrewText(
    val hebrew: String,
    val transliteration: String,
    val vocalized: String = hebrew
)

data class VerbConjugation(
    val verbId: Int,
    val past: PastConjugation,
    val present: PresentConjugation,
    val future: FutureConjugation,
    val infinitive: HebrewText,
    val imperative: ImperativeConjugation?
)

data class PresentConjugation(
    val mascSingular: HebrewText,
    val femSingular: HebrewText,
    val mascPlural: HebrewText,
    val femPlural: HebrewText
)

data class PastConjugation(
    val firstSg: HebrewText,
    val secondMascSg: HebrewText,
    val secondFemSg: HebrewText,
    val thirdMascSg: HebrewText,
    val thirdFemSg: HebrewText,
    val firstPl: HebrewText,
    val secondMascPl: HebrewText,
    val secondFemPl: HebrewText,
    val thirdMascPl: HebrewText,
    val thirdFemPl: HebrewText
)

data class FutureConjugation(
    val firstSg: HebrewText,
    val secondMascSg: HebrewText,
    val secondFemSg: HebrewText,
    val thirdMascSg: HebrewText,
    val thirdFemSg: HebrewText,
    val firstPl: HebrewText,
    val secondMascPl: HebrewText,
    val secondFemPl: HebrewText,
    val thirdMascPl: HebrewText,
    val thirdFemPl: HebrewText
)

data class ImperativeConjugation(
    val mascSingular: HebrewText,
    val femSingular: HebrewText,
    val mascPlural: HebrewText,
    val femPlural: HebrewText
)

data class HebrewExample(
    val hebrew: String,
    val transliteration: String,
    val english: String,
    val tense: String,
    val context: String
)
