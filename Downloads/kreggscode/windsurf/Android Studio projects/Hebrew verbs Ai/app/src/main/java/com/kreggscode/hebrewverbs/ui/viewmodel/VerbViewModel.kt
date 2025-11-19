package com.kreggscode.hebrewverbs.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.hebrewverbs.data.database.VerbDatabase
import com.kreggscode.hebrewverbs.data.model.Binyan
import com.kreggscode.hebrewverbs.data.model.HebrewVerb
import com.kreggscode.hebrewverbs.data.repository.VerbRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class VerbUiState(
    val verbs: List<HebrewVerb> = emptyList(),
    val selectedBinyan: Binyan? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class VerbViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: VerbRepository
    
    private val _uiState = MutableStateFlow(VerbUiState())
    val uiState: StateFlow<VerbUiState> = _uiState.asStateFlow()

    init {
        val verbDao = VerbDatabase.getDatabase(application).verbDao()
        repository = VerbRepository(verbDao)
        loadVerbs()
    }

    private fun loadVerbs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getAllVerbs().collect { verbs ->
                _uiState.value = _uiState.value.copy(
                    verbs = verbs,
                    isLoading = false
                )
            }
        }
    }

    fun filterByBinyan(binyan: Binyan?) {
        _uiState.value = _uiState.value.copy(selectedBinyan = binyan)
        viewModelScope.launch {
            if (binyan == null) {
                repository.getAllVerbs().collect { verbs ->
                    _uiState.value = _uiState.value.copy(verbs = verbs)
                }
            } else {
                repository.getVerbsByBinyan(binyan).collect { verbs ->
                    _uiState.value = _uiState.value.copy(verbs = verbs)
                }
            }
        }
    }

    fun searchVerbs(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        viewModelScope.launch {
            if (query.isBlank()) {
                loadVerbs()
            } else {
                repository.searchVerbs(query).collect { verbs ->
                    _uiState.value = _uiState.value.copy(verbs = verbs)
                }
            }
        }
    }

    fun toggleFavorite(verb: HebrewVerb) {
        viewModelScope.launch {
            repository.updateFavoriteStatus(verb.id, !verb.isFavorite)
        }
    }
}

class VerbRepository(private val verbDao: com.kreggscode.hebrewverbs.data.database.VerbDao) {
    fun getAllVerbs() = verbDao.getAllVerbs()
    fun getVerbsByBinyan(binyan: Binyan) = verbDao.getVerbsByBinyan(binyan)
    fun searchVerbs(query: String) = verbDao.searchVerbs(query)
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean) = 
        verbDao.updateFavoriteStatus(id, isFavorite)
}
