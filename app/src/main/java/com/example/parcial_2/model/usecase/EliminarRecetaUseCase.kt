package com.example.parcial_2.model.usecase

import com.example.parcial_2.model.repository.RecetaRepository

class EliminarRecetaUseCase {

    private val repository = RecetaRepository()

    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.eliminarReceta(id)
    }
}