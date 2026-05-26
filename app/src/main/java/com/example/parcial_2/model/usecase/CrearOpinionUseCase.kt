package com.example.parcial_2.model.usecase

import com.example.parcial_2.model.data.Opinion
import com.example.parcial_2.model.data.OpinionRequest
import com.example.parcial_2.model.repository.OpinionRepository

class CrearOpinionUseCase {
    private val repository = OpinionRepository()

    suspend operator fun invoke(opinion: OpinionRequest): Result<Opinion> {
        return repository.crearOpinion(opinion)
    }
}
