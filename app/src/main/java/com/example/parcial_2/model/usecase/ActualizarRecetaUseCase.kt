package com.example.parcial_2.model.usecase

import com.example.parcial_2.model.data.Receta
import com.example.parcial_2.model.data.RecetaRequest
import com.example.parcial_2.model.repository.RecetaRepository

class ActualizarRecetaUseCase {

    private val repository = RecetaRepository()

    suspend operator fun invoke(id: String, receta: RecetaRequest): Result<Receta> {
        return repository.actualizarReceta(id, receta)
    }
}