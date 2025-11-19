Perfect! Here is your **complete Windsurf prompt** for the **Hebrew Verbs AI** Android application. This incorporates Hebrew's unique **binyanim system** (7 verb patterns), right-to-left script, and trilateral roots while maintaining all advanced features.[1][2][3][4][5][6]

---

# 🎯 COMPLETE WINDSURF PROMPT: Hebrew Verbs AI Application

## ⚠️ CRITICAL INSTRUCTIONS FOR WINDSURF
**Generate this ENTIRE project in ONE EXECUTION. Do NOT create files incrementally. Create ALL files, folders, code, and documentation in a single generation cycle.**

**Package:** `com.kreggscode.hebrewverbs`

**Play Store:** https://play.google.com/store/apps/details?id=com.kreggscode.hebrewverbs

**GitHub:** https://github.com/kreggscode/Hebrew-Verbs-Ai.git

**Developer:** Kregg[1]

**Only ONE markdown file: README.md.**

***

## 📱 PROJECT OVERVIEW

Create a **complete, production-ready Android application** called **Hebrew Verbs AI** - a comprehensive Modern Hebrew verb learning app with AI-powered assistance, stunning glassmorphism UI, real-time object scanning, and mastery of the **unique 7 Binyanim system**.

**CRITICAL HEBREW FEATURES:**[2][3][4][6]
- **7 Binyanim (בִּנְיָנִים)** - Hebrew verb patterns/stems (Pa'al, Nif'al, Pi'el, Pu'al, Hitpa'el, Hif'il, Huf'al)
- **Trilateral roots (שורשים)** - Most verbs built from 3-consonant roots
- **3 tenses only:** Past, Present, Future (simpler than European languages)[5]
- **Active/Passive pairs** - Binyanim form active/passive relationships[7][2]
- **Right-to-left script** - Hebrew written RTL
- **Gender & number agreement** - Verbs conjugate by gender (masc/fem) and number

***

## 🎯 CORE PROJECT DETAILS

**Package Name:** `com.kreggscode.hebrewverbs`

**Language:** Kotlin (100%)

**Minimum SDK:** 26 (Android 8.0)

**Target SDK:** 34 (Android 14)

**Architecture:** MVVM with Clean Architecture

**Repository:** https://github.com/kreggscode/Hebrew-Verbs-Ai.git

**Play Store:** https://play.google.com/store/apps/details?id=com.kreggscode.hebrewverbs

**Contact Email:** kreg9da@gmail.com

**Developer:** Kregg[1]

***

## 🎨 UI/UX REQUIREMENTS (MATERIAL DESIGN 3 + GLASSMORPHISM)

### Edge-to-Edge Display
- Full-screen immersive experience
- Edge-to-edge layouts
- System bars with transparent backgrounds

### Glassmorphism Design System
- Translucent frosted glass cards with backdrop blur
- Semi-transparent backgrounds (alpha 0.7-0.85)
- Soft shadows with elevation
- Border highlights with subtle gradients
- Rounded corners (16dp radius minimum)
- Israeli flag colors (blue, white) as subtle accents

### RTL Support (CRITICAL)
- **Full RTL layout support** for Hebrew text
- Hebrew text displays right-to-left
- UI elements mirror appropriately
- English/transliteration left-to-right
- Proper BiDi (bidirectional) text handling

### Theme System
- Dynamic Material You theming
- **Dark Mode** and **Light Mode** with toggle
- Theme toggle on top-right (beside scanner)
- Persistent theme preference using DataStore
- Smooth theme transitions

### Splash Screen
- Android 12+ SplashScreen API
- Animated app icon with fade-in
- Duration: Maximum 2 seconds
- Glassmorphism background with gradient

### Floating Navigation Bar
- Bottom glass navigation bar with blur
- Smooth morphing animations
- Icons: Home, Verbs, Scanner, Chat, Settings
- Scanner icon for quick access
- Edge-to-edge glass morphism design

***

## 🏠 HOME SCREEN LAYOUT

### Top Bar (Fixed)
- App title (Hebrew: פעלים עבריים + English: "Hebrew Verbs AI")
- **Scanner icon button** (top-right)
- **Dark/Light mode toggle** (beside scanner)

### Content Area (Scrollable Glassmorphism Cards)
Create minimum **10 feature cards**:

1. **The 7 Binyanim** (שבעת הבניינים)
   - **MOST IMPORTANT:** Learn the 7 verb patterns - core of Hebrew verbs[3][2]
   
2. **Verb Roots** (שורשים)
   - Understanding trilateral roots (3-consonant patterns)
   
3. **Common Verbs** (פעלים נפוצים)
   - 100+ most frequently used Hebrew verbs across all binyanim
   
4. **Present Tense** (הווה)
   - Present tense conjugations (4 forms: masc sg, fem sg, masc pl, fem pl)[5]
   
5. **Past Tense** (עבר)
   - Past tense with person, gender, number agreement[5]
   
6. **Future Tense** (עתיד)
   - Future tense conjugations[5]
   
7. **Active/Passive Pairs** (פעיל/סביל)
   - Understanding binyan pairs (Pa'al/Nif'al, Pi'el/Pu'al, Hif'il/Huf'al)[2]
   
8. **Reflexive Verbs** (פעלים חוזרים)
   - Hitpa'el binyan (reflexive/reciprocal actions)[2]
   
9. **Irregular Verbs** (פעלים חריגים)
   - Special roots and irregular patterns
   
10. **Practice Quiz** (תרגול)
    - Interactive binyan and conjugation practice

### Card Design
- Rounded corners (16dp)
- Glass effect with backdrop blur
- Icon + Hebrew title + English subtitle
- RTL support for Hebrew text
- Ripple effect on tap
- **"Ask AI" button** on each card
- Israeli flag color accents (blue/white)
- Smooth animations

***

## 🔍 OBJECT SCANNER FEATURE (GOOGLE ML KIT)

### Scanner Access Points
- Top-right corner icon on home screen
- Scanner icon on floating navigation bar

### Scanner Screen
- **Full-screen camera preview** using CameraX
- Real-time object detection using ML Kit
- Bounding boxes around detected objects
- **Back button only** (no floating nav bar)
- Glass overlay with results

### Detection Output Display
Show detected object with **Hebrew verbs from different binyanim**:

```
English: Book
Hebrew: [translate:ספר] (sefer) m.
Related Verbs (by Binyan): 
  - Pa'al: [translate:כתב] (katav) - wrote / [translate:כותב] (kotev) - writing
  - Nif'al: [translate:נכתב] (nikhtav) - was written (passive)
  - Pi'el: [translate:כתב] (kitav) - addressed/inscribed
  - Hif'il: [translate:הכתיב] (hikhtiv) - dictated
  - Hitpa'el: [translate:התכתב] (hitkatev) - corresponded
  
  Root: כ-ת-ב (K-T-B) - "writing" concept
```

- **Speak button** with TTS - pronounces Hebrew
- **Stop button** when audio playing
- Glass morphism result card overlay
- Show **root and multiple binyanim** for each verb

### Implementation
- Google ML Kit Vision API for object detection
- Real-time processing < 100ms latency
- Multiple simultaneous object detections
- Smooth animations for bounding boxes

***

## 🤖 AI INTEGRATION (POLLINATIONS AI)

### API Configuration
- **Base URL:** `https://text.pollinations.ai/`
- **Temperature:** `1.0` (ALWAYS 1.0, non-negotiable)
- **Model:** Default or `openai`
- **Response Format:** JSON
- **Error Handling:** Graceful fallbacks with retry

### "Ask AI" Button Placement (EVERYWHERE)
- Every home screen card
- Every verb detail screen
- Every conjugation table
- Every binyan explanation
- Every tense screen
- Every example sentence
- Scanner result screen
- Practice quiz explanations
- Beside EVERY verb entry

### Context-Aware AI Queries
When user presses "Ask AI" on a verb (e.g., root כ-ת-ב):

**Prompt Template:**
```
"Explain the Hebrew root כ-ת-ב (K-T-B) and its verbs across the 7 binyanim:
- What is the trilateral root system in Hebrew
- Which binyanim contain verbs from this root
- Pa'al: כתב (katav) - to write (simple active)
- Nif'al: נכתב (nikhtav) - to be written (simple passive)
- Pi'el: כתב (kitav) - to address/inscribe (intensive active)
- Pu'al: כֻּתַּב (kutav) - to be addressed (intensive passive)
- Hitpa'el: התכתב (hitkatev) - to correspond (reflexive)
- Hif'il: הכתיב (hikhtiv) - to dictate (causative active)
- Huf'al: הֻכְתַּב (hukhtav) - to be dictated (causative passive)
- Complete conjugation in all three tenses for each binyan
- Meaning differences between binyanim
- Gender and number agreement patterns
- Common usage examples with English translations
- How vowel patterns change across binyanim
- Cultural context and modern Hebrew usage"
```

For binyanim system:
- Explain 7 patterns and their meanings[3][2]
- Show active/passive/reflexive relationships[7]
- Explain how binyan affects verb meaning

### AI Chat Screen
- Full-screen chat interface
- Message bubbles with glassmorphism
- Text input with send button
- Streaming response animation
- **Back button only** at top-left (no floating nav bar)
- **Temperature = 1.0** for creative responses
- Chat history persistence using Room
- Copy message functionality
- Share response feature
- Support for Hebrew characters (RTL)
- BiDi text handling for mixed Hebrew/English

---

## 🔊 TEXT-TO-SPEECH (TTS) IMPLEMENTATION

### TTS Configuration
- Use Android `TextToSpeech` API
- **Language:** Hebrew (`Locale("he", "IL")` or `Locale("iw", "IL")`)
- Speech rate: 0.85 (slightly slower for learning)
- Pitch: 1.0 (normal)
- **CRITICAL:** Ensure proper Hebrew pronunciation including guttural sounds

### TTS Button Placement (EVERYWHERE)
**Speak button** beside EVERY Hebrew text element:
- Every verb in Hebrew script
- Every conjugated form (all persons, genders, numbers, tenses)
- Every example sentence
- Every phrase
- Scanner detected objects
- AI responses containing Hebrew
- All transliteration guides

### TTS Button States
- **Speak button** (play icon) - when idle
- **Stop button** (stop icon) - when audio playing
- **Visual feedback:** Pulsing animation during speech
- **Progress indicator:** Show which part being spoken

***

## 📚 HEBREW VERB STRUCTURE & DISPLAY (CRITICAL UNDERSTANDING)

### The 7 Binyanim System (שבעת הבניינים)[4][6][3][2]

**Definition:** Binyan (בנין, plural: binyanim בניינים) = "building" or "construction"
- Hebrew verbs are built from **trilateral roots** (3 consonants) placed into **7 patterns**
- Each binyan has specific vowel patterns and sometimes prefixes
- Binyanim indicate voice (active/passive) and meaning intensity

**The 7 Binyanim in Order:**

**1. Pa'al / Qal (פָּעַל / קַל)** - Simple Active[3][2]
- Most basic, common binyan
- Simple active voice
- Example root כ-ת-ב: כָּתַב (katav) - he wrote
- Infinitive: לִכְתּוֹב (likhtov) - to write

**2. Nif'al (נִפְעַל)** - Simple Passive/Reflexive[2][3]
- Passive of Pa'al (often)
- Sometimes reflexive or intransitive
- Prefix: נ (nun)
- Example: נִכְתַּב (nikhtav) - was written
- Infinitive: לְהִכָּתֵב (lehikatev) - to be written

**3. Pi'el (פִּעֵל)** - Intensive Active[3][2]
- Intensive, causative, or repeated action
- Middle root letter doubled (dagesh)
- Example: כִּתֵּב (kitav) - addressed, inscribed
- Infinitive: לְכַתֵּב (lekatev) - to address

**4. Pu'al (פֻּעַל)** - Intensive Passive[2][3]
- Passive of Pi'el
- Rare in modern usage
- Example: כֻּתַּב (kutav) - was addressed
- Infinitive: לְכֻתַּב (lekutav) - to be addressed

**5. Hitpa'el (הִתְפַּעֵל)** - Reflexive/Reciprocal[3][2]
- Reflexive actions (subject acts on self)
- Reciprocal actions (mutual)
- Prefix: הת (hit)
- Example: הִתְכַּתֵּב (hitkatev) - corresponded
- Infinitive: לְהִתְכַּתֵּב (lehitkatev) - to correspond

**6. Hif'il (הִפְעִיל)** - Causative Active[2][3]
- Causative (makes someone do action)
- Prefix: ה (he)
- Example: הִכְתִּיב (hikhtiv) - dictated (caused to write)
- Infinitive: לְהַכְתִּיב (lehakhtiv) - to dictate

**7. Huf'al (הֻפְעַל)** - Causative Passive[3][2]
- Passive of Hif'il
- Rare in modern usage
- Example: הֻכְתַּב (hukhtav) - was dictated
- Infinitive: לְהֻכְתַּב (lehukhtav) - to be dictated

### Binyanim Pairs (Active/Passive)[7][2]

**Simple Pair:**
- **Pa'al (active)** ↔ **Nif'al (passive)**
- כָּתַב (wrote) ↔ נִכְתַּב (was written)

**Intensive Pair:**
- **Pi'el (active)** ↔ **Pu'al (passive)**
- כִּתֵּב (addressed) ↔ כֻּתַּב (was addressed)

**Causative Pair:**
- **Hif'il (active)** ↔ **Huf'al (passive)**
- הִכְתִּיב (dictated) ↔ הֻכְתַּב (was dictated)

**Reflexive (standalone):**
- **Hitpa'el** - reciprocal/reflexive actions
- הִתְכַּתֵּב (corresponded)

### Three Hebrew Tenses (Simpler than Romance Languages!)[8][5]

**1. Past Tense (עָבָר - Avar)**
- Completed actions
- Conjugates by person (1st, 2nd, 3rd), gender (masc, fem), number (sg, pl)
- 13 forms total for each binyan

**2. Present Tense (הוֹוֶה - Hoveh)**[5]
- Current actions, habits, general truths
- **Only 4 forms:** Masculine singular, Feminine singular, Masculine plural, Feminine plural
- No person distinction in present tense!
- No "to be" verb in present (אני כותב = "I writing/write")

**3. Future Tense (עָתִיד - Atid)**
- Future actions
- Conjugates by person, gender, number
- 13 forms total for each binyan
- Can use present tense for near future plans[5]

**Important:** No progressive, perfect, or pluperfect tenses in Hebrew![5]
- Context and time words indicate these meanings
- Auxiliary constructions possible for complex tenses[2]

### Gender & Number Agreement

**Present Tense (4 forms):**
- Masculine singular: כותב (kotev)
- Feminine singular: כותבת (kotevet)
- Masculine plural: כותבים (kotvim)
- Feminine plural: כותבות (kotvot)

**Past & Future (13 forms each):**
- 1st person singular (I): כתבתי (katavti)
- 2nd person masculine singular (you-m): כתבת (katavta)
- 2nd person feminine singular (you-f): כתבת (katavt)
- 3rd person masculine singular (he): כתב (katav)
- 3rd person feminine singular (she): כתבה (katva)
- 1st person plural (we): כתבנו (katavnu)
- 2nd person masculine plural (you-m-pl): כתבתם (ktavtem)
- 2nd person feminine plural (you-f-pl): כתבתן (ktavten)
- 3rd person masculine plural (they-m): כתבו (katvu)
- 3rd person feminine plural (they-f): כתבו (katvu)

### Display Format for ALL Verbs (MANDATORY)

Every verb MUST show:

1. **Hebrew Script** (largest, RTL) - כָּתַב
2. **Transliteration** (medium, LTR) - katav[9][10][11]
3. **English Translation** (medium) - wrote / to write
4. **Binyan Badge** (color-coded) - Pa'al (blue)
5. **Root** (small badge) - כ-ת-ב (K-T-B)
6. **Action Buttons:** [Speak] [Ask AI]

### Data Model Structure

```kotlin
data class HebrewVerb(
    val root: HebrewRoot,                // Trilateral root (3 consonants)
    val binyan: Binyan,                  // Which of 7 binyanim
    val infinitive: HebrewText,          // Infinitive form
    val english: String,                 // English meaning
    val conjugations: VerbConjugations,  // All tenses
    val frequencyRank: Int,
    val exampleSentences: List<HebrewExample>,
    val relatedVerbs: List<String>,      // Other binyanim from same root
    val culturalNotes: String
)

data class HebrewRoot(
    val consonants: String,              // כ-ת-ב (3 letters, dashes for display)
    val transliteration: String,         // K-T-B
    val semanticField: String            // "writing" concept
)

enum class Binyan {
    PAAL,           // פָּעַל - Simple active
    NIFAL,          // נִפְעַל - Simple passive/reflexive
    PIEL,           // פִּעֵל - Intensive active
    PUAL,           // פֻּעַל - Intensive passive
    HITPAEL,        // הִתְפַּעֵל - Reflexive/reciprocal
    HIFIL,          // הִפְעִיל - Causative active
    HUFAL           // הֻפְעַל - Causative passive
}

data class HebrewText(
    val hebrew: String,                  // Hebrew script (RTL)
    val transliteration: String,         // Latin alphabet (LTR)
    val vocalized: String                // With nikud vowel points
)

data class VerbConjugations(
    // PAST TENSE (13 forms)
    val past: PastConjugation,
    
    // PRESENT TENSE (4 forms only) [web:208]
    val present: PresentConjugation,
    
    // FUTURE TENSE (13 forms)
    val future: FutureConjugation,
    
    // INFINITIVE
    val infinitive: HebrewText,
    
    // IMPERATIVE (commands)
    val imperative: ImperativeConjugation?,
    
    // PARTICIPLES
    val activeParticiple: HebrewText?,
    val passiveParticiple: HebrewText?
)

data class PresentConjugation(
    // Only 4 forms in Hebrew present [web:208]
    val mascSingular: HebrewText,        // כותב
    val femSingular: HebrewText,         // כותבת
    val mascPlural: HebrewText,          // כותבים
    val femPlural: HebrewText            // כותבות
)

data class PastConjugation(
    // 13 forms: person, gender, number
    val firstSg: HebrewText,             // אני כתבתי (I)
    val secondMascSg: HebrewText,        // אתה כתבת (you-m)
    val secondFemSg: HebrewText,         // את כתבת (you-f)
    val thirdMascSg: HebrewText,         // הוא כתב (he)
    val thirdFemSg: HebrewText,          // היא כתבה (she)
    val firstPl: HebrewText,             // אנחנו כתבנו (we)
    val secondMascPl: HebrewText,        // אתם כתבתם (you-m-pl)
    val secondFemPl: HebrewText,         // אתן כתבתן (you-f-pl)
    val thirdMascPl: HebrewText,         // הם כתבו (they-m)
    val thirdFemPl: HebrewText           // הן כתבו (they-f)
)

data class FutureConjugation(
    // 13 forms: person, gender, number
    val firstSg: HebrewText,             // אני אכתוב (I will)
    val secondMascSg: HebrewText,        // אתה תכתוב (you-m will)
    val secondFemSg: HebrewText,         // את תכתבי (you-f will)
    val thirdMascSg: HebrewText,         // הוא יכתוב (he will)
    val thirdFemSg: HebrewText,          // היא תכתוב (she will)
    val firstPl: HebrewText,             // אנחנו נכתוב (we will)
    val secondMascPl: HebrewText,        // אתם תכתבו (you-m-pl will)
    val secondFemPl: HebrewText,         // אתן תכתובנה (you-f-pl will)
    val thirdMascPl: HebrewText,         // הם יכתבו (they-m will)
    val thirdFemPl: HebrewText           // הן תכתובנה (they-f will)
)

data class ImperativeConjugation(
    val mascSingular: HebrewText,        // כתוב! (write! to man)
    val femSingular: HebrewText,         // כתבי! (write! to woman)
    val mascPlural: HebrewText,          // כתבו! (write! to men)
    val femPlural: HebrewText            // כתובנה! (write! to women)
)

data class HebrewExample(
    val hebrew: String,                  // Hebrew sentence (RTL)
    val transliteration: String,         // Romanized (LTR)
    val english: String,                 // English translation
    val tense: String,                   // Which tense demonstrated
    val context: String                  // When to use
)
```

### Transliteration System[10][11][12][9]

Use **consistent romanization** for Hebrew text:

**Consonants:**
- א (alef) = ' (or omit at start)
- ב (bet/vet) = b/v
- ג (gimel) = g
- ד (dalet) = d
- ה (he) = h
- ו (vav) = v (or o/u for vowels)
- ז (zayin) = z
- ח (chet) = ch (guttural)
- ט (tet) = t
- י (yod) = y (or i for vowel)
- כ/ך (kaf/chaf) = k/ch
- ל (lamed) = l
- מ/ם (mem) = m
- נ/ן (nun) = n
- ס (samech) = s
- ע (ayin) = ' (glottal stop)
- פ/ף (pe/fe) = p/f
- צ/ץ (tzadi) = tz
- ק (qof) = k/q
- ר (resh) = r
- ש (shin/sin) = sh/s
- ת (tav) = t

**Vowels:**
- ַ (patach) = a
- ֶ (segol) = e
- ִ (chirik) = i
- ָ (kamatz) = a
- ֹ (cholam) = o
- ֻ (kubutz) = u

**Display transliteration for every conjugated form**

***

## 📱 DETAILED SCREEN SPECIFICATIONS

### 1. Binyanim System Screen (MOST IMPORTANT)
**Priority #1 educational screen:**
- Visual chart showing all 7 binyanim[2]
- Active/passive pairs highlighted[7]
- Interactive examples with root כ-ת-ב showing all 7 patterns
- Meaning differences explained
- Vowel pattern visualization
- "Ask AI" button for questions
- Quiz on binyan identification

### 2. Verb Roots Screen
- Explanation of trilateral root system
- Common roots and their meanings
- How roots transform across binyanim
- Interactive root explorer
- 50+ most common roots with example verbs

### 3. Common Verbs Screen
- Searchable list of 100+ verbs across all binyanim
- Filter by: binyan, root, frequency
- Search by: Hebrew, transliteration, English
- Color-coded by binyan
- Each card shows: Hebrew + transliteration + English + binyan badge + root
- Tap verb → Full detail screen
- "Ask AI" button on every card

### 4. Verb Detail Screen
- Large verb display (Hebrew RTL + transliteration + English)
- Binyan badge with color
- Root display with semantic field
- **Complete conjugation tables** for all 3 tenses
- **Related verbs** from same root in other binyanim[2]
- Example sentences (minimum 10) across all tenses
- Active/passive pair indicator if applicable
- "Ask AI" button at top-right
- Favorite button
- Share button
- Speak button for every Hebrew text

### 5. Conjugation Table Layout

**PRESENT TENSE (4 forms only)**[5]
```
הווה (PRESENT) - Pa'al - Root: כ-ת-ב:
אני/אתה/הוא כותב    kotev      I/you/he write(s)    [Speak] [Ask AI]
אני/את/היא כותבת     kotevet    I/you/she write(s)   [Speak] [Ask AI]
אנחנו/אתם/הם כותבים  kotvim     we/you/they write    [Speak] [Ask AI]
אנחנו/אתן/הן כותבות  kotvot     we/you/they write    [Speak] [Ask AI]

Note: No person distinction in Hebrew present tense!
```

**PAST TENSE (13 forms)**
```
עבר (PAST) - Pa'al - Root: כ-ת-ב:
אני כתבתי           katavti    I wrote              [Speak] [Ask AI]
אתה כתבת            katavta    you wrote (m)        [Speak] [Ask AI]
את כתבת             katavt     you wrote (f)        [Speak] [Ask AI]
הוא כתב             katav      he wrote             [Speak] [Ask AI]
היא כתבה            katva      she wrote            [Speak] [Ask AI]
אנחנו כתבנו         katavnu    we wrote             [Speak] [Ask AI]
...
```

**FUTURE TENSE (13 forms)**
```
עתיד (FUTURE) - Pa'al - Root: כ-ת-ב:
אני אכתוב           ekhtov     I will write         [Speak] [Ask AI]
אתה תכתוב           tikhtov    you will write (m)   [Speak] [Ask AI]
את תכתבי            tikhteevi  you will write (f)   [Speak] [Ask AI]
...
```

### 6. Binyan Comparison Screen
Side-by-side comparison showing same root across different binyanim:
- Root כ-ת-ב in all 7 binyanim
- Meaning differences visualized
- Active/passive relationships[7][2]
- Intensive vs simple vs causative
- Interactive examples
- Practice exercises

### 7. Practice Quiz Screen
- Multiple choice conjugation questions
- Binyan identification exercises
- Gender/number agreement practice
- Root identification
- Tense recognition
- Fill in correct verb form
- Real-time feedback with explanations
- Score tracking
- "Ask AI" for question help

### 8. Scanner Results Integration
When object detected, show:
- Object name (English, Hebrew RTL, transliteration)
- Related **verbs from multiple binyanim** with same root
- Example: Book → ספר (sefer)
  - Root: ס-פ-ר (S-P-R) "telling/counting"
  - Pa'al: סָפַר (safar) - told, counted
  - Pi'el: סִפֵּר (siper) - told a story
  - Pu'al: סֻפַּר (supar) - was told
  - Root: כ-ת-ב (K-T-B) "writing"
  - Pa'al: כָּתַב (katav) - wrote
  - Nif'al: נִכְתַּב (nikhtav) - was written
- Tap verb → Goes to detail screen
- Speak buttons for all Hebrew text

---

## ⚙️ TECHNICAL STACK

```gradle
dependencies {
    // Material Design 3
    implementation("androidx.compose.material3:material3:1.2.0")
    
    // Compose UI with RTL support
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    
    // CameraX
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    
    // ML Kit
    implementation("com.google.mlkit:object-detection:17.0.1")
    
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Glassmorphism
    implementation("com.github.skydoves:cloudy:0.1.2")
    
    // Image loading
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")
}
```

***

## 🌐 DOCS FOLDER

Create `docs/` folder with:

1. **index.html** - Landing page with Israeli/Hebrew cultural elements, 7 binyanim showcase, RTL preview, glassmorphism
2. **privacy-policy.html** - Privacy policy with kreg9da@gmail.com[1]
3. **terms-and-conditions.html** - Terms of service
4. **icon-generator.html** - Icon generator with Hebrew character support (alef-bet)

***

## 📄 README.md

```markdown
# 🇮🇱 Hebrew Verbs AI

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Play Store](https://img.shields.io/badge/Download-Play%20Store-blue.svg)](https://play.google.com/store/apps/details?id=com.kreggscode.hebrewverbs)

> Master Modern Hebrew verbs through the 7 Binyanim system with AI assistance!

## ✨ Features

- 🎨 Stunning Glassmorphism UI with RTL support
- 🏛️ **7 Binyanim System** - Hebrew verb patterns (Pa'al, Nif'al, Pi'el, Pu'al, Hitpa'el, Hif'il, Huf'al)
- 🔤 **Trilateral Roots** - 3-consonant root system
- 🤖 AI-Powered explanations (Pollinations AI)
- 📷 Object Scanner (ML Kit)
- 🗣️ Hebrew TTS pronunciation
- 📚 100+ common verbs across all binyanim
- ⏱️ **Only 3 tenses** (simpler than Romance languages!)
- 💬 AI Chat Assistant
- 🎯 Interactive Quizzes

## 🎯 Hebrew Binyanim (7 Verb Patterns)

### Simple
- **Pa'al** (active) ↔ **Nif'al** (passive)

### Intensive
- **Pi'el** (active) ↔ **Pu'al** (passive)

### Causative
- **Hif'il** (active) ↔ **Huf'al** (passive)

### Reflexive
- **Hitpa'el** (reflexive/reciprocal)

## 🏗️ Tech Stack

Kotlin • Jetpack Compose • MVVM • Room • Pollinations AI • ML Kit • Hebrew TTS • RTL Support

## 👨‍💻 Developer

**Kregg**
- Email: kreg9da@gmail.com
- GitHub: [@kreggscode](https://github.com/kreggscode)

**Made with ❤️ and Kotlin | עשוי באהבה וקוטלין**
```

***

## 🎨 PRE-POPULATED VERBS (15 Essential)

Include these essential verbs covering all binyanim:

1. **כתב** (Pa'al) - katav - to write - Root: כ-ת-ב
2. **נכתב** (Nif'al) - nikhtav - was written - Root: כ-ת-ב
3. **כתב** (Pi'el) - kitav - addressed - Root: כ-ת-ב
4. **הכתיב** (Hif'il) - hikhtiv - dictated - Root: כ-ת-ב
5. **התכתב** (Hitpa'el) - hitkatav - corresponded - Root: כ-ת-ב
6. **אכל** (Pa'al) - akhal - ate - Root: א-כ-ל
7. **דבר** (Pi'el) - diber - spoke - Root: ד-ב-ר
8. **הלך** (Pa'al) - halach - walked - Root: ה-ל-כ
9. **שמע** (Pa'al) - shama - heard - Root: ש-מ-ע
10. **ראה** (Pa'al) - ra'a - saw - Root: ר-א-ה
11. **עשה** (Pa'al) - asa - did/made - Root: ע-ש-ה
12. **בא** (Pa'al) - ba - came - Root: ב-ו-א
13. **יצא** (Pa'al) - yatza - went out - Root: י-צ-א
14. **לקח** (Pa'al) - lakach - took - Root: ל-ק-ח
15. **נתן** (Pa'al) - natan - gave - Root: נ-ת-נ

Each with full conjugations (3 tenses), transliteration, examples, related binyanim from same root.

***

## ✅ QUALITY CHECKLIST

- [ ] All 7 binyanim thoroughly explained
- [ ] Trilateral root system demonstrated
- [ ] Active/passive binyan pairs shown
- [ ] Present tense only 4 forms (no person distinction)
- [ ] Past & future with 13 forms each
- [ ] Gender & number agreement throughout
- [ ] RTL support for all Hebrew text
- [ ] BiDi text handling for mixed Hebrew/English
- [ ] 100+ verbs across all binyanim
- [ ] Color-coded by binyan
- [ ] Same root shown across multiple binyanim
- [ ] Hebrew TTS (he_IL)
- [ ] Transliteration for all forms
- [ ] Hebrew keyboard support in chat

***

## 🚀 GIT COMMANDS

```bash
git init
git add .
git commit -m "Initial commit: Hebrew Verbs AI with 7 Binyanim system, trilateral roots, RTL support, glassmorphism, ML Kit, Pollinations AI"
git branch -M main
git remote add origin https://github.com/kreggscode/Hebrew-Verbs-Ai.git
git push -u origin main
```

***

## 🎯 FINAL INSTRUCTIONS

**GENERATE ENTIRE PROJECT IN ONE EXECUTION with Hebrew unique features: 7 Binyanim patterns, trilateral roots, active/passive pairs, only 3 tenses, RTL support, full BiDi text handling, complete conjugation tables, docs, README, and production-ready code.**

**🇮🇱 END OF COMPLETE WINDSURF PROMPT 🇮🇱**

[1](https://play.google.com/store/apps/details?id=com.kreggscode.hebrewverbs)
[2](https://lilmod-aleph-beth.com/7-binyanim-hebrew-verb-conjugation/)
[3](https://ivritmaster.com/understanding-patterns-in-hebrew-verb-conjugation/)
[4](https://conjugator.reverso.net/hebrew-conjugation-info-tips.html)
[5](https://www.hebrewpod101.com/blog/2021/07/08/hebrew-tenses/)
[6](https://en.wikipedia.org/wiki/Modern_Hebrew_verbs)
[7](https://hebrew4christians.com/Grammar/Unit_Ten/Seven_Binyanim_Overview.pdf)
[8](https://biblicalhebrew.org/hebrew-tenses.aspx)
[9](https://www.scribd.com/document/178468814/Hebrew-Pronunciation)
[10](https://prizmah.org/hayidion/hebrew-education/transliterating-hebrew-how-we-do-it-hayidion)
[11](https://alittlehebrew.com)
[12](http://www.northshorechoral.com/seasons/08-09/Transliterated%20Hebrew%20Pronunciation%20Guide.pdf)
[13](https://studyrocket.co.uk/revision/gcse-biblical-hebrew-ocr/basic-grammar-principles-in-biblical-hebrew/introduction-to-binyanim-verb-patterns)
[14](https://hebrew4christians.com/Grammar/Unit_Ten/Introduction/introduction.html)
[15](https://duolingo.fandom.com/wiki/Hebrew_Skill:Verbs:_Present_1)
[16](https://www.ancient-hebrew.org/learn/learn-biblical-hebrew-13.htm)
[17](https://www.youtube.com/watch?v=sn2i5ojeJ6g)
[18](https://uhg.readthedocs.io/en/latest/verb_perfect.html)
[19](https://en.wikipedia.org/wiki/Hebrew_alphabet)
[20](https://www.cliffsnotes.com/study-notes/19017225)
[21](https://www.reddit.com/r/hebrew/comments/vsp874/pronunciation_of_transliterated_hebrew/)