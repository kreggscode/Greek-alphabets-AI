package com.kreggscode.hebrewverbs.data

import com.kreggscode.hebrewverbs.data.model.Binyan
import com.kreggscode.hebrewverbs.data.model.HebrewVerb

/**
 * Sample Hebrew verbs covering all 7 binyanim
 * Based on the root כ-ת-ב (K-T-B) - "writing" and other common roots
 */
object SampleVerbs {
    val verbs = listOf(
        // Root: כ-ת-ב (K-T-B) - Writing
        HebrewVerb(
            id = 1,
            hebrew = "כָּתַב",
            transliteration = "katav",
            english = "wrote / to write",
            binyan = Binyan.PAAL,
            root = "כ-ת-ב",
            rootTransliteration = "K-T-B",
            frequencyRank = 1
        ),
        HebrewVerb(
            id = 2,
            hebrew = "נִכְתַּב",
            transliteration = "nikhtav",
            english = "was written",
            binyan = Binyan.NIFAL,
            root = "כ-ת-ב",
            rootTransliteration = "K-T-B",
            frequencyRank = 2
        ),
        HebrewVerb(
            id = 3,
            hebrew = "כִּתֵּב",
            transliteration = "kitav",
            english = "addressed / inscribed",
            binyan = Binyan.PIEL,
            root = "כ-ת-ב",
            rootTransliteration = "K-T-B",
            frequencyRank = 3
        ),
        HebrewVerb(
            id = 4,
            hebrew = "הִכְתִּיב",
            transliteration = "hikhtiv",
            english = "dictated",
            binyan = Binyan.HIFIL,
            root = "כ-ת-ב",
            rootTransliteration = "K-T-B",
            frequencyRank = 4
        ),
        HebrewVerb(
            id = 5,
            hebrew = "הִתְכַּתֵּב",
            transliteration = "hitkatev",
            english = "corresponded",
            binyan = Binyan.HITPAEL,
            root = "כ-ת-ב",
            rootTransliteration = "K-T-B",
            frequencyRank = 5
        ),

        // Root: א-כ-ל (A-K-L) - Eating
        HebrewVerb(
            id = 6,
            hebrew = "אָכַל",
            transliteration = "akhal",
            english = "ate / to eat",
            binyan = Binyan.PAAL,
            root = "א-כ-ל",
            rootTransliteration = "A-K-L",
            frequencyRank = 6
        ),

        // Root: ד-ב-ר (D-B-R) - Speaking
        HebrewVerb(
            id = 7,
            hebrew = "דִּבֵּר",
            transliteration = "diber",
            english = "spoke / to speak",
            binyan = Binyan.PIEL,
            root = "ד-ב-ר",
            rootTransliteration = "D-B-R",
            frequencyRank = 7
        ),

        // Root: ה-ל-כ (H-L-K) - Walking/Going
        HebrewVerb(
            id = 8,
            hebrew = "הָלַךְ",
            transliteration = "halach",
            english = "walked / to walk",
            binyan = Binyan.PAAL,
            root = "ה-ל-כ",
            rootTransliteration = "H-L-K",
            frequencyRank = 8
        ),

        // Root: ש-מ-ע (SH-M-A) - Hearing
        HebrewVerb(
            id = 9,
            hebrew = "שָׁמַע",
            transliteration = "shama",
            english = "heard / to hear",
            binyan = Binyan.PAAL,
            root = "ש-מ-ע",
            rootTransliteration = "SH-M-A",
            frequencyRank = 9
        ),

        // Root: ר-א-ה (R-A-H) - Seeing
        HebrewVerb(
            id = 10,
            hebrew = "רָאָה",
            transliteration = "ra'a",
            english = "saw / to see",
            binyan = Binyan.PAAL,
            root = "ר-א-ה",
            rootTransliteration = "R-A-H",
            frequencyRank = 10
        ),

        // Root: ע-ש-ה (A-S-H) - Doing/Making
        HebrewVerb(
            id = 11,
            hebrew = "עָשָׂה",
            transliteration = "asa",
            english = "did / made / to do",
            binyan = Binyan.PAAL,
            root = "ע-ש-ה",
            rootTransliteration = "A-S-H",
            frequencyRank = 11
        ),

        // Root: ב-ו-א (B-O-A) - Coming
        HebrewVerb(
            id = 12,
            hebrew = "בָּא",
            transliteration = "ba",
            english = "came / to come",
            binyan = Binyan.PAAL,
            root = "ב-ו-א",
            rootTransliteration = "B-O-A",
            frequencyRank = 12
        ),

        // Root: י-צ-א (Y-TZ-A) - Going out
        HebrewVerb(
            id = 13,
            hebrew = "יָצָא",
            transliteration = "yatza",
            english = "went out / to go out",
            binyan = Binyan.PAAL,
            root = "י-צ-א",
            rootTransliteration = "Y-TZ-A",
            frequencyRank = 13
        ),

        // Root: ל-ק-ח (L-K-CH) - Taking
        HebrewVerb(
            id = 14,
            hebrew = "לָקַח",
            transliteration = "lakach",
            english = "took / to take",
            binyan = Binyan.PAAL,
            root = "ל-ק-ח",
            rootTransliteration = "L-K-CH",
            frequencyRank = 14
        ),

        // Root: נ-ת-נ (N-T-N) - Giving
        HebrewVerb(
            id = 15,
            hebrew = "נָתַן",
            transliteration = "natan",
            english = "gave / to give",
            binyan = Binyan.PAAL,
            root = "נ-ת-נ",
            rootTransliteration = "N-T-N",
            frequencyRank = 15
        )
    )
}
