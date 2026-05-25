package com.example.parcial_2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial_2.model.data.RecetaRequest
import com.example.parcial_2.model.usecase.ActualizarRecetaUseCase
import com.example.parcial_2.model.usecase.CrearRecetaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AgregarUiState {
    object Idle : AgregarUiState()
    object Loading : AgregarUiState()
    object Success : AgregarUiState()
    data class Error(val message: String) : AgregarUiState()
}

class AgregarRecetaViewModel : ViewModel() {

    private val crearRecetaUseCase = CrearRecetaUseCase()
    private val actualizarRecetaUseCase = ActualizarRecetaUseCase()

    private val _uiState = MutableStateFlow<AgregarUiState>(AgregarUiState.Idle)
    val uiState: StateFlow<AgregarUiState> = _uiState

    fun guardarReceta(
        id: String?,
        nombre: String,
        imagen: String,
        ingredientes: List<String>,
        pasos: List<String>,
        categoria: String,
        tiempoPreparacion: Int,
        porciones: Int
    ) {
        viewModelScope.launch {
            _uiState.value = AgregarUiState.Loading
            val request = RecetaRequest(
                nombre = nombre,
                imagen = imagen,
                ingredientes = ingredientes,
                pasos = pasos,
                categoria = categoria,
                tiempoPreparacion = tiempoPreparacion,
                porciones = porciones
            )
            val result = if (id == null) {
                crearRecetaUseCase(request)
            } else {
                actualizarRecetaUseCase(id, request)
            }
            _uiState.value = result.fold(
                onSuccess = { AgregarUiState.Success },
                onFailure = { AgregarUiState.Error(it.message ?: "error saving recipe") }
            )
        }
    }
}