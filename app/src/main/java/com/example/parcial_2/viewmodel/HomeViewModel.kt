package com.example.parcial_2.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial_2.model.data.Opinion
import com.example.parcial_2.model.data.OpinionRequest
import com.example.parcial_2.model.data.Receta
import com.example.parcial_2.model.usecase.CrearOpinionUseCase
import com.example.parcial_2.model.usecase.EliminarRecetaUseCase
import com.example.parcial_2.model.usecase.GetOpinionesUseCase
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
    private val crearOpinionUseCase = CrearOpinionUseCase()
    private val getOpinionesUseCase = GetOpinionesUseCase()

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    // Receta seleccionada y sus opiniones
    var recetaSeleccionada: Receta? by mutableStateOf(null)
        private set

    var opinionesSeleccionada by mutableStateOf<List<Opinion>>(emptyList())
        private set

    // Cálculos dinámicos
    val puntuacionPromedioDinamica: Double
        get() = if (opinionesSeleccionada.isNotEmpty()) {
            opinionesSeleccionada.map { it.calificacion }.average()
        } else recetaSeleccionada?.puntuacionPromedio ?: 0.0

    val totalPreparacionesDinamica: Int
        get() = opinionesSeleccionada.sumOf { it.vecesPreparada }

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
        cargarOpinionesDeReceta(receta.id)
    }

    private fun cargarOpinionesDeReceta(id: String) {
        viewModelScope.launch {
            val result = getOpinionesUseCase(id)
            opinionesSeleccionada = result.getOrDefault(emptyList())
        }
    }

    fun eliminarReceta(id: String) {
        viewModelScope.launch {
            val result = eliminarRecetaUseCase(id)
            result.onSuccess { cargarRecetas() }
        }
    }

    fun guardarOpinion(
        porciones: Int,
        puntuacion: Double,
        nota: String,
        veces: Int,
        onSuccess: () -> Unit
    ) {
        val recetaId = recetaSeleccionada?.id ?: return
        
        val request = OpinionRequest(
            recetaId = recetaId,
            nombreComensal = "Usuario App",
            comentario = nota,
            calificacion = puntuacion,
            porciones = porciones,
            vecesPreparada = veces
        )

        viewModelScope.launch {
            val result = crearOpinionUseCase(request)
            result.onSuccess {
                cargarOpinionesDeReceta(recetaId) // Recargar opiniones para actualizar cálculos
                cargarRecetas() // Recargar lista general
                onSuccess()
            }
        }
    }
}