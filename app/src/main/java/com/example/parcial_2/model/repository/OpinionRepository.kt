package com.example.parcial_2.model.repository

import com.example.parcial_2.model.data.Opinion
import com.example.parcial_2.model.data.OpinionRequest
import com.example.parcial_2.model.remote.RetrofitClient

class OpinionRepository {

    private val api = RetrofitClient.apiService

    suspend fun getOpinionesPorReceta(recetaId: String): Result<List<Opinion>> {
        return try {
            val response = api.getOpinionesPorReceta(recetaId)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al obtener opiniones"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearOpinion(opinion: OpinionRequest): Result<Opinion> {
        return try {
            val response = api.crearOpinion(opinion)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Error al guardar la opinión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
