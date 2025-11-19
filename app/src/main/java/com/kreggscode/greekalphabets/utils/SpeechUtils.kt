package com.kreggscode.greekalphabets.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.Locale

fun speakText(
    tts: TextToSpeech?,
    locale: Locale,
    text: String,
    context: Context,
    isTtsReady: Boolean,
    onAvailabilityChecked: (Boolean) -> Unit = {}
) {
    if (text.isBlank()) {
        Toast.makeText(context, "Nothing to speak yet.", Toast.LENGTH_SHORT).show()
        return
    }

    val engine = tts
    if (engine == null || !isTtsReady) {
        Toast.makeText(context, "Speech engine not ready. Try again in a moment.", Toast.LENGTH_SHORT).show()
        return
    }

    val availability = engine.isLanguageAvailable(locale)
    val isAvailable = availability >= TextToSpeech.LANG_AVAILABLE
    onAvailabilityChecked(isAvailable)

    if (!isAvailable) {
        Toast.makeText(
            context,
            "Voice data for ${locale.displayLanguage} is missing. Please install it from device settings.",
            Toast.LENGTH_LONG
        ).show()
        return
    }

    val langResult = engine.setLanguage(locale)
    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
        Toast.makeText(
            context,
            "${locale.displayLanguage} voice not supported on this device.",
            Toast.LENGTH_SHORT
        ).show()
        return
    }

    engine.stop()
    val speakResult = engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, locale.toLanguageTag())
    if (speakResult == TextToSpeech.ERROR) {
        Toast.makeText(
            context,
            "Unable to play audio. Please try again.",
            Toast.LENGTH_SHORT
        ).show()
    }
}

