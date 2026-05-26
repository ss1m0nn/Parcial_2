package com.example.parcial_2.model.usecase

import com.example.parcial_2.model.data.Opinion
import com.example.parcial_2.model.repository.OpinionRepository

class GetOpinionesUseCase {
    private val repository = OpinionRepository()

    suspend operator fun invoke(recetaId: String): Result<List<Opinion>> {
        return repository.getOpinionesPorReceta(recetaId)
    }
}
