package com.kreggscode.greekalphabets.data.ai

import android.content.Context
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.net.URLEncoder

@Serializable
data class Message(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class AIRequest(
    val model: String = "openai",
    val messages: List<AIMessage>,
    val temperature: Float = 1.0f,
    @kotlinx.serialization.SerialName("max_tokens")
    val maxTokens: Int = 1000,
    val stream: Boolean = false
)

@Serializable
data class AIMessage(
    val role: String,
    val content: String
)

@Serializable
data class AIResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val message: AIResponseMessage,
    val index: Int,
    @kotlinx.serialization.SerialName("finish_reason")
    val finishReason: String? = null
)

@Serializable
data class AIResponseMessage(
    val role: String,
    val content: String
)

class AIService(private val context: Context) {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        
        install(HttpTimeout) {
            requestTimeoutMillis = 120000
            connectTimeoutMillis = 120000
            socketTimeoutMillis = 120000
        }
    }
    
    private val systemPrompt = """You are a Greek language tutor. CRITICAL RULES:
        
        1. RESPOND IN THE SAME LANGUAGE AS THE USER
           - English question → English answer
           - Spanish question → Spanish answer  
           - Russian question → Russian answer
           - Greek question → Greek answer
        
        2. NEVER respond in Greek unless the user writes in Greek
        
        3. Format: Explanation in user's language + Greek examples with romanization
           Example: Γεια σας (Yasas - hello)
        
        4. Structure:
           - Explain concepts in user's language
           - Show Greek words with romanization
           - Translate meanings to user's language
        
        ENGLISH example:
        "The word γράφω (grafo) means 'to write'.
        
        **Present tense:**
        • γράφω (grafo) - I write
        • γράφεις (grafeis) - you write
        • γράφει (grafei) - he/she writes"
        
        SPANISH example:
        "El verbo γράφω (grafo) significa 'escribir'.
        
        **Tiempo presente:**
        • γράφω (grafo) - yo escribo
        • γράφεις (grafeis) - tú escribes
        • γράφει (grafei) - él/ella escribe"
        
        NEVER write full explanations in Greek. Greek is only for examples."""
    
    suspend fun getResponse(userMessage: String, conversationHistory: List<Message>): String {
        return withContext(Dispatchers.IO) {
            try {
                // Use POST method with JSON body as per Pollinations.AI documentation
                return@withContext getOpenAIResponse(userMessage)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext getFallbackResponse(userMessage)
            }
        }
    }
    
    private suspend fun getOpenAIResponse(userMessage: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val url = "https://text.pollinations.ai/openai"
                
                val requestBody = AIRequest(
                    model = "openai",
                    messages = listOf(
                        AIMessage(
                            role = "system",
                            content = systemPrompt
                        ),
                        AIMessage(
                            role = "user",
                            content = userMessage
                        )
                    ),
                    temperature = 1.0f,
                    maxTokens = 1500,
                    stream = false
                )
                
                println("🔗 AI Request URL: $url")
                println("📤 Request body: model=${requestBody.model}, temp=${requestBody.temperature}")
                
                val response: HttpResponse = client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(requestBody)
                }
                
                println("📥 AI Response Status: ${response.status}")
                
                if (response.status.isSuccess()) {
                    val aiResponse = response.bodyAsText()
                    println("✅ Raw response: ${aiResponse.take(200)}...")
                    
                    // Parse the JSON response
                    val json = Json { ignoreUnknownKeys = true }
                    val parsedResponse = json.decodeFromString<AIResponse>(aiResponse)
                    
                    val content = parsedResponse.choices.firstOrNull()?.message?.content
                    if (content.isNullOrBlank()) {
                        println("⚠️ WARNING: Response content is empty!")
                        return@withContext "I received an empty response. Please try again."
                    }
                    
                    println("✅ AI Response SUCCESS: ${content.take(100)}...")
                    return@withContext content
                } else {
                    val errorBody = response.bodyAsText()
                    println("❌ AI Request failed with status: ${response.status}")
                    println("❌ Error body: $errorBody")
                    return@withContext "Connection error: ${response.status}. Please check your internet connection and try again."
                }
            } catch (e: Exception) {
                println("❌ AI Request exception: ${e.javaClass.simpleName} - ${e.message}")
                e.printStackTrace()
                return@withContext "Network error: ${e.message}. Please check your internet connection and try again."
            }
        }
    }
    
    private fun getFallbackResponse(userMessage: String): String {
        val lowercaseMessage = userMessage.lowercase()
        
        // Check if this is a word explanation request - don't give generic response
        if (lowercaseMessage.contains("explain the Greek word") || 
            lowercaseMessage.contains("detailed meaning") ||
            lowercaseMessage.contains("conjugation tips")) {
            return "I apologize, but I'm currently unable to provide a detailed explanation for this specific word. " +
                   "Please check your internet connection and try again. The AI service will provide comprehensive " +
                   "information about this word including usage patterns, conjugation tips, and example sentences."
        }
        
        return when {
            lowercaseMessage.contains("hello") || lowercaseMessage.contains("hi") || 
            lowercaseMessage.contains("γεια") || lowercaseMessage.contains("yasas") -> {
                "Γεια σας! (Yasas) - Hello! 👋\n\n" +
                "I'm your Greek language tutor. I can help you learn Greek words, grammar, and conversation. " +
                "What would you like to practice today?"
            }
            
            lowercaseMessage.contains("conjugate") && (lowercaseMessage.contains("γράφω") || lowercaseMessage.contains("grafo")) -> {
                "Let me explain how to conjugate γράφω (grafo - to write):\n\n" +
                "**Present Tense:**\n" +
                "• εγώ (I): γράφω (grafo) - I write\n" +
                "• εσύ (you): γράφεις (grafeis) - you write\n" +
                "• αυτός/αυτή (he/she): γράφει (grafei) - he/she writes\n" +
                "• εμείς (we): γράφουμε (grafoume) - we write\n\n" +
                "**Past Tense (Aorist):**\n" +
                "• έγραψα (egrapsa) - I wrote\n" +
                "• έγραψες (egrapses) - you wrote\n" +
                "• έγραψε (egrapse) - he/she wrote\n\n" +
                "**Future Tense:**\n" +
                "• θα γράψω (tha grapso) - I will write\n" +
                "• θα γράψεις (tha grapseis) - you will write\n\n" +
                "Practice sentence: Γράφω ένα γράμμα (Grapso ena gramma) - I write a letter 📚"
            }
            
            lowercaseMessage.contains("word") || lowercaseMessage.contains("λέξη") -> {
                "Here are some essential Greek words to learn:\n\n" +
                "📚 **Daily Actions:**\n" +
                "• τρώω (troo) - to eat\n" +
                "• πίνω (pino) - to drink\n" +
                "• κοιμάμαι (kimame) - to sleep\n" +
                "• ξυπνάω (xipnao) - to wake up\n" +
                "• διαβάζω (diavazo) - to study/read\n" +
                "• πηγαίνω (pigeno) - to go\n\n" +
                "💡 **Tip:** Most Greek verbs end in -ω (-o) in their infinitive form. " +
                "To conjugate them, you change the ending based on person and tense!\n\n" +
                "Would you like to practice conjugating any of these words?"
            }
            
            lowercaseMessage.contains("grammar") || lowercaseMessage.contains("γραμματική") -> {
                "Greek grammar has some unique features! Here are key points:\n\n" +
                "📝 **Word Order:** Greek typically follows Subject-Verb-Object (SVO)\n" +
                "Example: Εγώ τρώω ένα μήλο (Ego troo ena milo)\n" +
                "I eat an apple\n\n" +
                "📝 **Cases:** Greek uses four cases for nouns and adjectives\n" +
                "• Nominative (ο, η, το) - subject\n" +
                "• Genitive (του, της, των) - possession\n" +
                "• Accusative (τον, τη, το) - object\n" +
                "• Vocative - addressing someone\n\n" +
                "📝 **Gender:** All nouns have one of three genders\n" +
                "• Masculine (ο)\n" +
                "• Feminine (η)\n" +
                "• Neuter (το)\n\n" +
                "Which aspect would you like to explore more? 😊"
            }
            
            lowercaseMessage.contains("thank") || lowercaseMessage.contains("ευχαριστώ") || 
            lowercaseMessage.contains("efharisto") -> {
                "Παρακαλώ! (Parakalo) - You're welcome! 😊\n\n" +
                "Other ways to say thank you in Greek:\n" +
                "• Ευχαριστώ πολύ (Efharisto poli) - Thank you very much\n" +
                "• Σας ευχαριστώ (Sas efharisto) - Thank you (formal)\n" +
                "• Σ' ευχαριστώ (S' efharisto) - Thank you (casual)\n" +
                "• Ευχαριστώ (Efharisto) - Thanks\n\n" +
                "Keep practicing, you're doing great! Καλή τύχη! (Kali tyhi - good luck!)"
            }
            
            else -> {
                "That's an interesting question about Greek! While I'm currently offline, " +
                "I can help you with:\n\n" +
                "• Greek verb conjugations (γράφω, είμαι, έχω)\n" +
                "• Basic grammar rules (cases, gender, articles)\n" +
                "• Common phrases and expressions (Γεια σας, Ευχαριστώ)\n" +
                "• Greek alphabet (Α-Ω) basics\n" +
                "• Pronunciation tips\n\n" +
                "Please try asking about any of these topics, or check your internet connection " +
                "for more detailed AI-powered responses! 📚✨"
            }
        }
    }
    
    fun onDestroy() {
        client.close()
    }
}
