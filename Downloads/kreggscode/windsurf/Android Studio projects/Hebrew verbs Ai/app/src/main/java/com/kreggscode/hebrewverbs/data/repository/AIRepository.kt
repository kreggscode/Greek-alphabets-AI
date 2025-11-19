package com.kreggscode.hebrewverbs.data.repository

import com.kreggscode.hebrewverbs.data.api.ChatRequest
import com.kreggscode.hebrewverbs.data.api.Message
import com.kreggscode.hebrewverbs.data.api.PollinationsAIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AIRepository(
    private val aiService: PollinationsAIService = PollinationsAIService.create()
) {
    private val conversationHistory = mutableListOf<Message>()
    
    // System prompt specialized for Hebrew verbs and binyanim
    private val systemPrompt = """
        You are an expert Hebrew language teacher specializing in Modern Hebrew verbs and the binyanim system.
        
        Key areas of expertise:
        - The 7 Binyanim (verb patterns): Pa'al, Nif'al, Pi'el, Pu'al, Hitpa'el, Hif'il, Huf'al
        - Trilateral root system (3-consonant roots)
        - Hebrew verb conjugation across 3 tenses (Past, Present, Future)
        - Active/passive pairs in binyanim
        - Gender and number agreement in Hebrew
        - Right-to-left Hebrew script and transliteration
        - Modern Hebrew pronunciation and usage
        
        Always provide:
        - Clear explanations with Hebrew examples
        - Transliteration for all Hebrew text
        - Cultural context when relevant
        - Practical usage examples
        - Encouragement and positive reinforcement
        
        Be concise but thorough. Use Hebrew script with transliteration.
    """.trimIndent()
    
    suspend fun sendMessage(userMessage: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Add user message to history
            val userMsg = Message(role = "user", content = userMessage)
            conversationHistory.add(userMsg)
            
            // Keep only last 5 messages for context
            val recentHistory = conversationHistory.takeLast(5)
            
            // Build messages with system prompt
            val messages = listOf(
                Message(role = "system", content = systemPrompt)
            ) + recentHistory
            
            // Create request with temperature=1.0
            val request = ChatRequest(
                model = "openai",
                messages = messages,
                temperature = 1.0, // CRITICAL: Always 1.0
                maxTokens = 1000
            )
            
            // Make API call
            val response = aiService.chat(request)
            val assistantMessage = response.choices.firstOrNull()?.message?.content
                ?: throw Exception("No response from AI")
            
            // Add assistant response to history
            conversationHistory.add(Message(role = "assistant", content = assistantMessage))
            
            Result.success(assistantMessage)
        } catch (e: Exception) {
            // Fallback response for offline mode
            Result.success(getFallbackResponse(userMessage))
        }
    }
    
    suspend fun getVerbExplanation(
        hebrewVerb: String,
        transliteration: String,
        binyan: String,
        root: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val prompt = """
            Explain the Hebrew verb $hebrewVerb ($transliteration) from the $binyan binyan:
            - Root: $root
            - What does this verb mean?
            - How does the $binyan binyan affect its meaning?
            - Show conjugation examples in different tenses
            - Provide usage examples with English translations
            - Explain any related verbs from the same root in other binyanim
        """.trimIndent()
        
        sendMessage(prompt)
    }
    
    suspend fun getBinyanExplanation(binyan: String): Result<String> = withContext(Dispatchers.IO) {
        val prompt = """
            Explain the $binyan binyan in detail:
            - What is its function (active/passive/reflexive/causative)?
            - What are its characteristic vowel patterns?
            - Which binyan is its active/passive pair (if any)?
            - Give 3 common verb examples with translations
            - Explain when to use this binyan
        """.trimIndent()
        
        sendMessage(prompt)
    }
    
    fun clearHistory() {
        conversationHistory.clear()
    }
    
    private fun getFallbackResponse(userMessage: String): String {
        return when {
            userMessage.contains("binyan", ignoreCase = true) -> {
                "The 7 binyanim are the foundation of Hebrew verbs. Each binyan represents a different pattern that affects the verb's meaning (active, passive, intensive, causative, or reflexive). Would you like to learn about a specific binyan?"
            }
            userMessage.contains("root", ignoreCase = true) -> {
                "Hebrew verbs are built from trilateral roots - typically 3 consonants that carry the core meaning. For example, the root כ-ת-ב (K-T-B) relates to writing, and appears in verbs across different binyanim."
            }
            userMessage.contains("conjugat", ignoreCase = true) -> {
                "Hebrew has 3 tenses: Past, Present, and Future. The present tense has only 4 forms (masculine/feminine, singular/plural), while past and future have 13 forms each with person, gender, and number distinctions."
            }
            else -> {
                "I'm here to help you learn Hebrew verbs! Ask me about binyanim, verb roots, conjugations, or any specific Hebrew verb. שלום!"
            }
        }
    }
}
