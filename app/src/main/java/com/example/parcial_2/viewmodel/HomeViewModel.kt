package com.example.parcial_2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial_2.model.data.Receta
import com.example.parcial_2.model.usecase.EliminarRecetaUseCase
import com.example.parcial_2.model.usecase.GetRecetasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val recetas: List<Receta>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel : ViewModel() {

    private val getRecetasUseCase = GetRecetasUseCase()
    private val eliminarRecetaUseCase = EliminarRecetaUseCase()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    // holds the recipe to edit
    var recetaSeleccionada: Receta? by mutableStateOf(null)
        private set

    init {
        cargarRecetas()
    }

    fun cargarRecetas() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val result = getRecetasUseCase()
            _uiState.value = result.fold(
                onSuccess = { HomeUiState.Success(it) },
                onFailure = { HomeUiState.Error(it.message ?: "error loading recipes") }
            )
        }
    }

    fun seleccionarReceta(receta: Receta) {
        recetaSeleccionada = receta
    }

    fun eliminarReceta(id: String) {
        viewModelScope.launch {
            val result = eliminarRecetaUseCase(id)
            result.onSuccess { cargarRecetas() }
        }
    }
}