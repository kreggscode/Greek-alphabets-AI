package com.kreggscode.greekalphabets.data.repository

import android.content.Context
import android.util.Log
import com.kreggscode.greekalphabets.data.models.GreekWord
import com.kreggscode.greekalphabets.utils.containsGreek
import com.kreggscode.greekalphabets.utils.pronounceGreek
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.contentOrNull
import java.nio.charset.StandardCharsets

class WordRepository(private val context: Context) {
    private val json = Json { 
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
    private var cachedVerbs: List<GreekWord>? = null
    private var cachedCategories: List<String>? = null
    
    // Optimized: Load JSON once and cache
    suspend fun getAllVerbs(): List<GreekWord> = withContext(Dispatchers.IO) {
        if (cachedVerbs == null) {
            // Use buffered input stream for faster reading
            val inputStream = context.assets.open("greek_words.json")
            val jsonString = inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
            cachedVerbs = parseVerbsSafely(jsonString)
        }
        cachedVerbs ?: emptyList()
    }
    
    // Optimized: Cache categories separately for faster access
    suspend fun getCategories(): List<String> = withContext(Dispatchers.IO) {
        if (cachedCategories == null) {
            cachedCategories = getAllVerbs()
                .map { it.category }
                .distinct()
                .filter { it.isNotBlank() && it.lowercase() != "category" }
                .sorted()
        }
        cachedCategories ?: emptyList()
    }
    
    suspend fun getVerbsByCategory(category: String): List<GreekWord> = withContext(Dispatchers.IO) {
        getAllVerbs().filter { it.category == category }
    }
    
    suspend fun searchVerbs(query: String): List<GreekWord> = withContext(Dispatchers.IO) {
        getAllVerbs().filter {
            it.word.contains(query, ignoreCase = true) ||
            it.pronunciation.contains(query, ignoreCase = true) ||
            it.englishMeaning.contains(query, ignoreCase = true) ||
            it.GreekSentence.contains(query, ignoreCase = true) ||
            it.englishSentence.contains(query, ignoreCase = true)
        }
    }
    
    suspend fun getVerbById(id: String): GreekWord? = withContext(Dispatchers.IO) {
        getAllVerbs().find { it.id == id }
    }
    
    suspend fun getRandomVerbs(count: Int): List<GreekWord> = withContext(Dispatchers.IO) {
        getAllVerbs().shuffled().take(count)
    }
    
    suspend fun getFavoriteVerbs(favoriteIds: Set<String>): List<GreekWord> = withContext(Dispatchers.IO) {
        getAllVerbs().filter { it.id in favoriteIds }
    }

    private fun parseVerbsSafely(jsonString: String): List<GreekWord> {
        return try {
            val elements = json.parseToJsonElement(jsonString).jsonArray
            val words = mutableListOf<GreekWord>()
            var skippedCount = 0

            elements.forEachIndexed { index, element ->
                val obj = element.jsonObject
                val decoded = runCatching {
                    json.decodeFromJsonElement(GreekWord.serializer(), obj)
                }.getOrNull()

                when {
                    decoded != null -> words.add(decoded)
                    else -> {
                        val fallback = convertFlexibleVerb(obj)
                        if (fallback != null) {
                            words.add(fallback)
                        } else {
                            skippedCount++
                            if (skippedCount <= 5) {
                                Log.w(
                                    "WordRepository",
                                    "Skipping malformed word entry at index $index: ${obj.keys}"
                                )
                            }
                        }
                    }
                }
            }

            Log.i(
                "WordRepository",
                "Loaded ${words.size} words successfully (skipped $skippedCount malformed entries out of ${elements.size} total)"
            )
            words
        } catch (e: Exception) {
            Log.e("WordRepository", "Failed to parse words JSON", e)
            emptyList()
        }
    }

    private fun convertFlexibleVerb(obj: JsonObject): GreekWord? {
        val id = obj["id"]?.jsonPrimitive?.contentOrNull ?: return null
        val category = obj["category"]?.jsonPrimitive?.contentOrNull ?: ""

        val word = obj["greek_word"]?.jsonPrimitive?.contentOrNull
            ?: obj["Greek_word"]?.jsonPrimitive?.contentOrNull
            ?: obj["word"]?.jsonPrimitive?.contentOrNull
            ?: obj["Greek"]?.jsonPrimitive?.contentOrNull
            ?: return null

        val verbRomanization = obj["romanization"]?.jsonPrimitive?.contentOrNull
            ?: obj["verb_romanization"]?.jsonPrimitive?.contentOrNull
            ?: if (containsGreek(word)) pronounceGreek(word) else ""

        val englishMeaning = obj["english_meaning"]?.jsonPrimitive?.contentOrNull
            ?: obj["english"]?.jsonPrimitive?.contentOrNull
            ?: ""

        val GreekSentence = obj["greek_sentence"]?.jsonPrimitive?.contentOrNull
            ?: obj["Greek_sentence"]?.jsonPrimitive?.contentOrNull
            ?: obj["korean_sentence"]?.jsonPrimitive?.contentOrNull
            ?: obj["exampleKor"]?.jsonPrimitive?.contentOrNull
            ?: ""

        val koreanSentenceRomanization =
            obj["sentence_romanization"]?.jsonPrimitive?.contentOrNull
                ?: obj["korean_sentence_romanization"]?.jsonPrimitive?.contentOrNull
                ?: obj["exampleRom"]?.jsonPrimitive?.contentOrNull
                ?: if (containsGreek(GreekSentence)) pronounceGreek(GreekSentence) else ""

        val englishSentence = obj["english_sentence"]?.jsonPrimitive?.contentOrNull
            ?: obj["exampleEng"]?.jsonPrimitive?.contentOrNull
            ?: ""

        return try {
            GreekWord(
                id = id,
                category = category,
                word = word,
                pronunciation = verbRomanization,
                englishMeaning = englishMeaning,
                GreekSentence = GreekSentence,
                sentencePronunciation = koreanSentenceRomanization,
                englishSentence = englishSentence
            )
        } catch (e: Exception) {
            Log.w("WordRepository", "Failed to map legacy word $id: ${e.message}")
            null
        }
    }
}
