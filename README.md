# Greek Alphabets AI 🇬🇷

[![Google Play](https://img.shields.io/badge/Get%20it%20on-Google%20Play-blue?style=for-the-badge&logo=google-play)](https://play.google.com/store/apps/details?id=com.kreggscode.greekalphabets)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)
[![Android](https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android)](https://developer.android.com)

> **Master Greek with AI-powered learning, 2000+ words, quizzes, and pronunciation guides.**

Greek Alphabets AI is your comprehensive companion for mastering the beautiful Greek language. Whether you're planning a trip to Greece, studying ancient texts, or simply want to learn a new language, this app provides everything you need to become fluent in Greek.

![Greek Flag](https://raw.githubusercontent.com/kreggscode/Greek-alphabets-AI/main/assets/greek-flag.png)

## ✨ Key Features

### 📚 Comprehensive Vocabulary Library
- Access over **2,000 carefully curated Greek words and phrases**
- Organized into intuitive categories for easy learning
- Each word includes pronunciation guides, English translations, and example sentences
- Learn words in context with real-world usage examples

### 🤖 AI-Powered Learning Assistant
- Chat with an intelligent AI tutor that helps you learn Greek
- Get instant explanations, grammar tips, and learning recommendations
- Practice conversations and receive personalized feedback
- Ask questions anytime, anywhere using [Pollinations.AI](https://pollinations.ai) API

### 🔤 Alphabet Mastery
- Interactive Greek alphabet learning with pronunciation
- Visual guides for each letter (Α α, Β β, Γ γ, Δ δ, Ε ε, Ζ ζ, Η η, Θ θ, Ι ι, Κ κ, Λ λ, Μ μ, Ν ν, Ξ ξ, Ο ο, Π π, Ρ ρ, Σ σ/ς, Τ τ, Υ υ, Φ φ, Χ χ, Ψ ψ, Ω ω)
- Practice writing and recognition exercises
- Perfect for beginners starting their Greek journey

### 📝 Grammar Made Easy
- Comprehensive grammar lessons covering:
  - Basic sentence structure
  - Nouns, verbs, and adjectives
  - Word order and syntax
  - Essential grammar rules explained simply

### 📸 Smart Text Scanner
- Use your camera to scan and translate Greek text instantly
- OCR technology recognizes Greek characters
- Perfect for reading signs, menus, and documents in Greece
- Learn on-the-go while traveling

### 🌍 Cultural Immersion
- Explore Greek culture, traditions, and history
- Discover authentic Greek cuisine and recipes
- Learn about Greek customs and etiquette
- Immerse yourself in the rich Hellenic heritage

### 🎮 Interactive Quizzes
- Test your knowledge with engaging quizzes
- Track your progress and identify areas for improvement
- Multiple question types to reinforce learning
- Gamified experience makes learning fun

### 🔊 Perfect Pronunciation
- Audio pronunciation for every word
- Phonetic romanization guides
- Practice speaking with confidence
- Native-like pronunciation support

### 📖 Word Categories
- **Daily Activities & Routines** - Morning routines, work, leisure
- **Numbers, Time & Dates** - Essential for daily communication
- **Food & Dining** - Restaurant vocabulary, Greek cuisine
- **Travel & Transportation** - Airport, hotels, getting around
- **And many more organized categories**

### 🌐 Advanced Translator
- Translate between Greek and English instantly
- Support for phrases and full sentences
- Context-aware translations
- Offline capabilities for essential translations

## 🎨 Beautiful Modern Interface
- **Sleek, intuitive design** that makes learning enjoyable
- **Dark mode support** for comfortable learning
- **Smooth animations and transitions**
- **User-friendly navigation**

## 📱 Screenshots

| ![Home Screen](https://raw.githubusercontent.com/kreggscode/Greek-alphabets-AI/main/screenshots/home.png) | ![Vocabulary](https://raw.githubusercontent.com/kreggscode/Greek-alphabets-AI/main/screenshots/vocabulary.png) | ![AI Chat](https://raw.githubusercontent.com/kreggscode/Greek-alphabets-AI/main/screenshots/ai-chat.png) |
|:---:|:---:|:---:|
| **Home Screen** | **Vocabulary Learning** | **AI Learning Assistant** |

| ![Quizzes](https://raw.githubusercontent.com/kreggscode/Greek-alphabets-AI/main/screenshots/quizzes.png) | ![Scanner](https://raw.githubusercontent.com/kreggscode/Greek-alphabets-AI/main/screenshots/scanner.png) | ![Settings](https://raw.githubusercontent.com/kreggscode/Greek-alphabets-AI/main/screenshots/settings.png) |
|:---:|:---:|:---:|
| **Interactive Quizzes** | **Text Scanner** | **Settings & Preferences** |

## 🚀 Getting Started

### Prerequisites
- **Android 8.0 (API level 26)** or higher
- **Internet connection** for AI features and cloud translation
- **Camera permissions** for text scanning feature

### Installation

1. **Download from Google Play Store**
   - Visit [Greek Alphabets AI on Google Play](https://play.google.com/store/apps/details?id=com.kreggscode.greekalphabets)
   - Click "Install" and wait for download to complete

2. **Grant Permissions**
   - Allow camera access for text scanning
   - Allow microphone access for pronunciation practice
   - Allow storage access for offline content

3. **Start Learning**
   - Open the app and create your learning profile
   - Choose your skill level (Beginner, Intermediate, Advanced)
   - Begin with the alphabet lessons or jump into vocabulary

## 🏗️ Technical Architecture

### Built With Android & Kotlin 🚀
- **Language**: Kotlin
- **Framework**: Android SDK
- **Architecture**: MVVM (Model-View-ViewModel)
- **AI Integration**: [Pollinations.AI](https://pollinations.ai) API
- **Database**: Room Persistence Library
- **UI**: Material Design 3
- **Build Tool**: Gradle Kotlin DSL

### Project Structure
```
app/
├── src/main/java/com/kreggscode/greekalphabets/
│   ├── data/           # Data layer (Room, API, repositories)
│   ├── ui/             # UI layer (Activities, Fragments, ViewModels)
│   ├── domain/         # Domain layer (Use cases, models)
│   └── utils/          # Utility classes
├── src/main/res/       # Android resources
└── src/main/assets/    # Static assets
```

### Key Dependencies
- `androidx.core:core-ktx` - Kotlin extensions
- `androidx.lifecycle:lifecycle-viewmodel-ktx` - ViewModel support
- `androidx.room:room-runtime` - Local database
- `com.squareup.retrofit2:retrofit` - HTTP client
- `com.google.mlkit:text-recognition` - OCR functionality
- `com.google.android.material:material` - Material Design components

## 🤝 Contributing

We welcome contributions! Here's how you can help:

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/AmazingFeature`)
3. **Commit your changes** (`git commit -m 'Add some AmazingFeature'`)
4. **Push to the branch** (`git push origin feature/AmazingFeature`)
5. **Open a Pull Request**

### Development Setup
```bash
# Clone the repository
git clone https://github.com/kreggscode/Greek-alphabets-AI.git

# Open in Android Studio
# Build and run on emulator or device
```

### Guidelines
- Follow Kotlin coding standards
- Write meaningful commit messages
- Add tests for new features
- Update documentation as needed

## 📊 Roadmap

- [ ] **Offline Mode Enhancement** - Expand offline capabilities
- [ ] **Multi-language Support** - Add more language pairs
- [ ] **Advanced AI Features** - Personalized learning paths
- [ ] **Social Learning** - Community challenges and leaderboards
- [ ] **iOS Version** - Cross-platform availability

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Pollinations.AI** for providing the AI API
- **Google ML Kit** for OCR capabilities
- **Material Design** for beautiful UI components
- **Greek language community** for inspiration and feedback

## 📞 Support

- **Issues**: [Report bugs](https://github.com/kreggscode/Greek-alphabets-AI/issues)
- **Discussions**: [Join discussions](https://github.com/kreggscode/Greek-alphabets-AI/discussions)
- **Email**: kreggscode@gmail.com

## 🌟 Why Choose Greek Alphabets AI?

✅ **Comprehensive**: Everything you need in one app - vocabulary, grammar, culture, and more
✅ **AI-Enhanced**: Smart learning assistant adapts to your needs
✅ **Practical**: Real-world examples and scenarios
✅ **Offline-Friendly**: Learn without internet connection
✅ **Regular Updates**: Continuously expanding content library
✅ **Free to Use**: Start learning Greek today at no cost

### Perfect For:
- 🗺️ Travelers visiting Greece
- 🎓 Students studying Greek language or history
- 🌍 Language enthusiasts
- 👴 Heritage learners reconnecting with Greek roots
- 📚 Anyone interested in ancient or modern Greek

---

**Start your Greek language journey today with Greek Alphabets AI!** 🇬🇷✨

*Download now and begin mastering one of the world's most beautiful and historically significant languages.*

---

**Note**: This app requires internet connection for AI features and cloud translation. Basic vocabulary and grammar content is available offline.
