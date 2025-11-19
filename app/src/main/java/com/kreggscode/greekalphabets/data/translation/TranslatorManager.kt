package com.kreggscode.greekalphabets.data.translation

// ML Kit Translate replaced with Cloud Translation API for 16 KB compatibility
// Cloud Translation API uses only network calls (no native libraries)
// This is 16 KB compatible and provides full translation functionality

enum class TranslationDirection {
    ENGLISH_TO_Greek,
    Greek_TO_ENGLISH
}

class TranslatorManager {

    // Using Cloud Translation API instead of ML Kit Translate
    // Cloud API is 16 KB compatible (no native libraries)
    private val cloudTranslator = CloudTranslator()

    suspend fun translate(text: String, direction: TranslationDirection): String? {
        if (text.isBlank()) return null

        // Use Cloud Translation API (16 KB compatible)
        val (source, target) = when (direction) {
            TranslationDirection.ENGLISH_TO_Greek -> "en" to "el"
            TranslationDirection.Greek_TO_ENGLISH -> "el" to "en"
        }
        
        return cloudTranslator.translate(text, source, target)
    }

    fun close() {
        cloudTranslator.close()
    }

    // private suspend fun downloadModelIfNeeded(translator: Translator) {
    //     suspendCancellableCoroutine { cont ->
    //         translator.downloadModelIfNeeded(downloadConditions)
    //             .addOnSuccessListener { cont.resume(Unit) }
    //             .addOnFailureListener { exception ->
    //                 if (cont.isActive) {
    //                     cont.resumeWithException(exception)
    //                 }
    //             }
    //         cont.invokeOnCancellation { }
    //     }
    // }
    //
    // private suspend fun translateInternal(translator: Translator, text: String): String {
    //     return suspendCancellableCoroutine { cont ->
    //         translator.translate(text)
    //             .addOnSuccessListener { result ->
    //                 cont.resume(result)
    //             }
    //             .addOnFailureListener { exception ->
    //                 if (cont.isActive) {
    //                     cont.resumeWithException(exception)
    //                 }
    //             }
    //         cont.invokeOnCancellation { }
    //     }
    // }
}

