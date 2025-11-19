package com.kreggscode.greekalphabets.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GreekWord(
    val id: String,
    val category: String,
    @SerialName("greek_word")
    val word: String,
    @SerialName("romanization")
    val pronunciation: String,
    @SerialName("english_meaning")
    val englishMeaning: String,
    @SerialName("greek_sentence")
    val GreekSentence: String,
    @SerialName("sentence_romanization")
    val sentencePronunciation: String,
    @SerialName("english_sentence")
    val englishSentence: String
)
