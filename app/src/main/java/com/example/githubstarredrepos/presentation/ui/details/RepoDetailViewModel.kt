package com.example.githubstarredrepos.presentation.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubstarredrepos.domain.model.Repository
import com.example.githubstarredrepos.domain.usecase.GetRepoDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoDetailViewModel @Inject constructor(
    private val getRepoDetailsUseCase: GetRepoDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<DetailState>(DetailState.Loading)
    val state: StateFlow<DetailState> = _state

    sealed class DetailState {
        object Loading : DetailState()
        data class Success(val repo: Repository) : DetailState()
        data class Error(val message: String) : DetailState()
    }

    init {
        // Récupère les arguments passés par l'Activity (owner et name)
        val owner = savedStateHandle.get<String>("owner")
        val name = savedStateHandle.get<String>("name")

        if (owner != null && name != null) {
            fetchDetails(owner, name)
        }
    }

    private fun fetchDetails(owner: String, name: String) {
        viewModelScope.launch {
            _state.value = DetailState.Loading
            val result = getRepoDetailsUseCase(owner, name)

            result.fold(
                onSuccess = { _state.value = DetailState.Success(it) },
                onFailure = { _state.value = DetailState.Error(it.message ?: "Erreur") }
            )
        }
    }
}