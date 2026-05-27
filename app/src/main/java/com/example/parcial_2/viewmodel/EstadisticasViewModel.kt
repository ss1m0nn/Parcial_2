package com.example.parcial_2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial_2.model.data.StatsResponse
import com.example.parcial_2.model.repository.RecetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class EstadisticasUiState {
    object Idle : EstadisticasUiState()
    object Loading : EstadisticasUiState()
    data class Success(val data: StatsResponse) : EstadisticasUiState()
    data class Error(val message: String) : EstadisticasUiState()
}

class EstadisticasViewModel : ViewModel() {

    private val repository = RecetaRepository()

    // 1. Definición correcta del MutableStateFlow (privado y mutable)
    private val _uiState = MutableStateFlow<EstadisticasUiState>(EstadisticasUiState.Idle)

    // 2. Definición correcta del StateFlow (público e inmutable)
    val uiState: StateFlow<EstadisticasUiState> = _uiState.asStateFlow()

    fun cargarDatos(id: String) {
        viewModelScope.launch {
            _uiState.value = EstadisticasUiState.Loading
            val result = repository.obtenerEstadisticas(id)

            result.fold(
                onSuccess = { data ->
                    _uiState.value = EstadisticasUiState.Success(data)
                },
                onFailure = { error ->
                    _uiState.value = EstadisticasUiState.Error(error.message ?: "Error desconocido")
                }
            )
        }
    }
}