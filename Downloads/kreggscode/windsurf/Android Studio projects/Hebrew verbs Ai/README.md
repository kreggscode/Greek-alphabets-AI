# 🇮🇱 Hebrew Verbs AI

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Play Store](https://img.shields.io/badge/Download-Play%20Store-blue.svg)](https://play.google.com/store/apps/details?id=com.kreggscode.hebrewverbs)

> Master Modern Hebrew verbs through the 7 Binyanim system with AI assistance!

## ✨ Features

- 🎨 **Stunning Glassmorphism UI** with RTL support
- 🏛️ **7 Binyanim System** - Hebrew verb patterns (Pa'al, Nif'al, Pi'el, Pu'al, Hitpa'el, Hif'il, Huf'al)
- 🔤 **Trilateral Roots** - 3-consonant root system
- 🤖 **AI-Powered Explanations** - Pollinations AI with temperature=1.0
- 📷 **Object Scanner** - ML Kit integration
- 🗣️ **Hebrew TTS** - Pronunciation support
- 📚 **100+ Common Verbs** across all binyanim
- ⏱️ **Only 3 Tenses** - Simpler than Romance languages!
- 💬 **AI Chat Assistant** - Specialized Hebrew teacher
- 🎯 **Interactive Quizzes** - Practice and master

## 🎯 Hebrew Binyanim (7 Verb Patterns)

### Simple
- **Pa'al** (פָּעַל) - Simple active ↔ **Nif'al** (נִפְעַל) - Simple passive

### Intensive
- **Pi'el** (פִּעֵל) - Intensive active ↔ **Pu'al** (פֻּעַל) - Intensive passive

### Causative
- **Hif'il** (הִפְעִיל) - Causative active ↔ **Huf'al** (הֻפְעַל) - Causative passive

### Reflexive
- **Hitpa'el** (הִתְפַּעֵל) - Reflexive/reciprocal actions

## 🏗️ Tech Stack

**Core Technologies:**
- Kotlin 100%
- Jetpack Compose - Modern UI
- MVVM + Clean Architecture
- Room Database with KSP
- Pollinations AI (temperature=1.0)
- ML Kit - Object detection
- CameraX - Scanner
- Hebrew TTS - Pronunciation
- RTL Support - Full bidirectional text

**Key Libraries:**
- Material Design 3
- Retrofit + OkHttp
- Kotlin Coroutines
- DataStore - Preferences
- Navigation Compose

## 📱 Screens

1. **Home** - Stats, quick actions, learning categories
2. **Binyanim** - Master the 7 verb patterns
3. **Verbs** - Browse 100+ common verbs
4. **Scanner** - Real-time object detection with Hebrew verbs
5. **AI Chat** - Ask anything about Hebrew verbs
6. **Conjugation** - Full verb conjugation tables
7. **Quiz** - Interactive practice

## 🤖 AI Integration

**Pollinations AI Configuration:**
- Base URL: `https://text.pollinations.ai/`
- Temperature: **1.0** (CRITICAL - Always 1.0)
- Model: OpenAI
- Specialized system prompt for Hebrew teaching
- Conversation history (last 5 messages)
- Fallback responses for offline mode

**AI Features:**
- Verb explanations across all binyanim
- Binyan system teaching
- Conjugation help
- Cultural context
- Usage examples
- Interactive Q&A

## 🎨 Design System

**Color Palette:**
- Primary: Indigo (#6366F1)
- Secondary: Purple (#8B5CF6)
- Tertiary: Pink (#EC4899)
- Israeli Blue accents

**Premium Components:**
- GlassmorphicCard - Frosted glass effect
- AnimatedGradientButton - Spring physics
- PremiumCard - Gradient borders
- PulsingIcon - AI feature indicators
- StatCard - Metrics display
- AnimatedProgressBar - Smooth animations

**Features:**
- Edge-to-edge design
- Dark/Light mode toggle
- Transparent system bars
- RTL layout support
- 20dp rounded corners
- Smooth transitions

## 📖 Hebrew Verb Structure

### Three Tenses
1. **Past (עָבָר)** - 13 forms with person/gender/number
2. **Present (הוֹוֶה)** - 4 forms (masc/fem, sg/pl)
3. **Future (עָתִיד)** - 13 forms with person/gender/number

### Trilateral Roots
Most Hebrew verbs built from 3-consonant roots:
- כ-ת-ב (K-T-B) - "writing"
- ד-ב-ר (D-B-R) - "speaking"
- ה-ל-כ (H-L-K) - "walking"

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 26+

### Build & Run
```bash
git clone https://github.com/kreggscode/Hebrew-Verbs-Ai.git
cd Hebrew-Verbs-Ai
./gradlew build
```

### Important Notes
- Uses **KSP** instead of KAPT (Java 17+ compatibility)
- Pollinations AI requires internet connection
- TTS requires Hebrew language pack
- Scanner requires camera permission

## 📦 Project Structure

```
app/
├── data/
│   ├── api/           # Pollinations AI service
│   ├── database/      # Room database
│   ├── model/         # Data models
│   └── repository/    # Data repositories
├── ui/
│   ├── components/    # Reusable UI components
│   ├── screens/       # App screens
│   ├── theme/         # Material theme
│   └── viewmodel/     # ViewModels
└── MainActivity.kt    # Entry point
```

## 🔧 Configuration

**Build Configuration:**
- Min SDK: 26
- Target SDK: 34
- Kotlin: 1.9.20
- KSP: 1.9.20-1.0.14
- Compose: 1.5.4

**API Configuration:**
```kotlin
// Pollinations AI - Temperature MUST be 1.0
ChatRequest(
    model = "openai",
    messages = messages,
    temperature = 1.0,  // CRITICAL
    maxTokens = 1000
)
```

## 👨‍💻 Developer

**Kregg**
- Email: kreg9da@gmail.com
- GitHub: [@kreggscode](https://github.com/kreggscode)
- Play Store: [Hebrew Verbs AI](https://play.google.com/store/apps/details?id=com.kreggscode.hebrewverbs)

## 📄 License

This project is licensed under the MIT License.

## 🙏 Acknowledgments

- Pollinations AI for free AI API
- Google ML Kit for object detection
- Material Design 3 for beautiful UI
- Hebrew language teachers worldwide

## 🌟 Support

If you find this app helpful, please:
- ⭐ Star this repository
- 📱 Rate on Play Store
- 🐛 Report bugs via Issues
- 💡 Suggest features

---

**Made with ❤️ and Kotlin | עשוי באהבה וקוטלין**

**Share Link:** https://play.google.com/store/apps/details?id=com.kreggscode.hebrewverbs
