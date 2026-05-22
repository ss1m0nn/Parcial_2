package com.example.parcial_2.model.usecase

import com.example.parcial_2.model.data.Receta
import com.example.parcial_2.model.repository.RecetaRepository

class GetRecetasUseCase {

    private val repository = RecetaRepository()

    suspend operator fun invoke(): Result<List<Receta>> {
        return repository.getRecetas()
    }
}