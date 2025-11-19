package com.kreggscode.greekalphabets.data.translation

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Pollinations.AI Translation Service - 16 KB compatible (no native libraries)
 * 
 * Uses Pollinations.AI for fast, reliable translation
 * - Fast response times
 * - No API key required (free tier)
 * - 16 KB compatible (cloud-based, no native libraries)
 * - Temperature set to 1.0 for consistent, natural translations
 */
class CloudTranslator {
    
    private val client = HttpClient(Android) {
        engine {
            connectTimeout = 20_000
            socketTimeout = 20_000
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = false
            })
        }
        expectSuccess = false // Don't throw on non-2xx responses
    }
    
    // Pollinations.AI Text Generation API (OpenAI compatible)
    private val pollinationsUrl = "https://text.pollinations.ai/openai"
    
    // Pollinations.AI API response models (OpenAI compatible)
    @Serializable
    data class PollinationsResponse(
        val choices: List<PollinationsChoice>
    )
    
    @Serializable
    data class PollinationsChoice(
        val message: PollinationsMessage
    )
    
    @Serializable
    data class PollinationsMessage(
        val content: String
    )
    
    @Serializable
    data class PollinationsRequest(
        val model: String = "openai",
        val messages: List<PollinationsRequestMessage>,
        val temperature: Double = 1.0,
        val max_tokens: Int = 500
    )
    
    @Serializable
    data class PollinationsRequestMessage(
        val role: String,
        val content: String
    )
    
    suspend fun translate(
        text: String,
        sourceLanguage: String = "en",
        targetLanguage: String = "sv"
    ): String? = withContext(Dispatchers.IO) {
        if (text.isBlank()) {
            Log.w("CloudTranslator", "Blank text provided")
            return@withContext null
        }
        
        return@withContext try {
            Log.d("CloudTranslator", "Translating: '$text' from $sourceLanguage to $targetLanguage using Pollinations.AI")
            
            val translated = translateWithPollinations(text, sourceLanguage, targetLanguage)
            if (translated != null) {
                Log.i("CloudTranslator", "Pollinations.AI translation successful: '$text' -> '$translated'")
                return@withContext translated
            }
            
            Log.e("CloudTranslator", "Translation failed")
            null
        } catch (e: java.net.UnknownHostException) {
            Log.e("CloudTranslator", "Network error: No internet connection", e)
            null
        } catch (e: java.net.SocketTimeoutException) {
            Log.e("CloudTranslator", "Network error: Request timeout", e)
            null
        } catch (e: Exception) {
            Log.e("CloudTranslator", "Translation failed", e)
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Translate using Pollinations.AI (fast, reliable, no API key required)
     */
    private suspend fun translateWithPollinations(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String? {
        return try {
            // Get language names for better translation context
            val sourceLangName = getLanguageName(sourceLanguage)
            val targetLangName = getLanguageName(targetLanguage)
            
            // Create translation prompt
            val systemMessage = "You are a professional translator. Translate the given text accurately and naturally. Only return the translation, nothing else."
            val userMessage = "Translate the following text from $sourceLangName to $targetLangName:\n\n$text"
            
            val request = PollinationsRequest(
                model = "openai",
                messages = listOf(
                    PollinationsRequestMessage(role = "system", content = systemMessage),
                    PollinationsRequestMessage(role = "user", content = userMessage)
                ),
                temperature = 1.0, // User requested temperature = 1.0
                max_tokens = 500
            )
            
            Log.d("CloudTranslator", "Sending request to Pollinations.AI: $sourceLangName -> $targetLangName")
            
            val response = client.post(pollinationsUrl) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            
            Log.d("CloudTranslator", "Pollinations.AI response status: ${response.status}")
            
            if (response.status == HttpStatusCode.OK) {
                val responseBody = response.bodyAsText()
                Log.d("CloudTranslator", "Pollinations.AI response body: $responseBody")
                
                try {
                    val jsonResponse = Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }.decodeFromString<PollinationsResponse>(responseBody)
                    
                    val translated = jsonResponse.choices.firstOrNull()?.message?.content?.trim()
                    
                    if (translated.isNullOrBlank()) {
                        Log.w("CloudTranslator", "Pollinations.AI returned empty translation")
                        null
                    } else if (translated == text) {
                        Log.w("CloudTranslator", "Pollinations.AI returned same text as input")
                        null
                    } else {
                        Log.d("CloudTranslator", "Pollinations.AI translation: '$translated'")
                        translated
                    }
                } catch (e: Exception) {
                    Log.e("CloudTranslator", "Failed to parse Pollinations.AI JSON response", e)
                    Log.e("CloudTranslator", "Response body was: $responseBody")
                    null
                }
            } else {
                val errorBody = response.bodyAsText()
                Log.e("CloudTranslator", "Pollinations.AI HTTP error: ${response.status}, body: $errorBody")
                null
            }
        } catch (e: java.net.UnknownHostException) {
            Log.e("CloudTranslator", "Pollinations.AI: No internet connection", e)
            null
        } catch (e: java.net.SocketTimeoutException) {
            Log.e("CloudTranslator", "Pollinations.AI: Request timeout", e)
            null
        } catch (e: Exception) {
            Log.e("CloudTranslator", "Pollinations.AI translation failed", e)
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Get human-readable language name for better translation context
     */
    private fun getLanguageName(code: String): String {
        return when (code.lowercase()) {
            "en" -> "English"
            "sv" -> "Greek"
            "es" -> "Spanish"
            "fr" -> "French"
            "de" -> "German"
            "it" -> "Italian"
            "pt" -> "Portuguese"
            "ru" -> "Russian"
            "ja" -> "Japanese"
            "ko" -> "Korean"
            "zh" -> "Chinese"
            "ar" -> "Arabic"
            "hi" -> "Hindi"
            "nl" -> "Dutch"
            "pl" -> "Polish"
            "tr" -> "Turkish"
            "vi" -> "Vietnamese"
            "th" -> "Thai"
            "id" -> "Indonesian"
            "cs" -> "Czech"
            "da" -> "Danish"
            "fi" -> "Finnish"
            "no" -> "Norwegian"
            "ro" -> "Romanian"
            "hu" -> "Hungarian"
            "el" -> "Greek"
            "he" -> "Hebrew"
            "uk" -> "Ukrainian"
            "bg" -> "Bulgarian"
            "hr" -> "Croatian"
            "sk" -> "Slovak"
            "sl" -> "Slovenian"
            "et" -> "Estonian"
            "lv" -> "Latvian"
            "lt" -> "Lithuanian"
            else -> code.uppercase()
        }
    }
    
    fun close() {
        client.close()
    }
}

