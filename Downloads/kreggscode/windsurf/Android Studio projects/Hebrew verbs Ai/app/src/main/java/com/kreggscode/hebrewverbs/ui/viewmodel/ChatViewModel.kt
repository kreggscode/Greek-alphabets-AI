package com.kreggscode.hebrewverbs.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.hebrewverbs.data.repository.AIRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChatViewModel(
    private val aiRepository: AIRepository = AIRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        // Add welcome message
        addMessage(
            ChatMessage(
                content = "שלום! I'm your Hebrew verbs AI assistant. Ask me anything about Hebrew verbs, binyanim, conjugations, or the Hebrew language!",
                isUser = false
            )
        )
    }

    fun sendMessage(message: String) {
        if (message.isBlank()) return

        // Add user message
        addMessage(ChatMessage(content = message, isUser = true))

        // Set loading state
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            aiRepository.sendMessage(message).fold(
                onSuccess = { response ->
                    addMessage(ChatMessage(content = response, isUser = false))
                    _uiState.value = _uiState.value.copy(isLoading = false)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    fun askAboutVerb(hebrew: String, transliteration: String, binyan: String, root: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            aiRepository.getVerbExplanation(hebrew, transliteration, binyan, root).fold(
                onSuccess = { response ->
                    addMessage(ChatMessage(content = response, isUser = false))
                    _uiState.value = _uiState.value.copy(isLoading = false)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    fun askAboutBinyan(binyan: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            aiRepository.getBinyanExplanation(binyan).fold(
                onSuccess = { response ->
                    addMessage(ChatMessage(content = response, isUser = false))
                    _uiState.value = _uiState.value.copy(isLoading = false)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    private fun addMessage(message: ChatMessage) {
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + message
        )
    }

    fun clearChat() {
        _uiState.value = ChatUiState()
        aiRepository.clearHistory()
        // Re-add welcome message
        addMessage(
            ChatMessage(
                content = "שלום! I'm your Hebrew verbs AI assistant. Ask me anything about Hebrew verbs, binyanim, conjugations, or the Hebrew language!",
                isUser = false
            )
        )
    }
}
