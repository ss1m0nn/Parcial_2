package com.example.parcial_2.model.repository

import com.example.parcial_2.model.data.Receta
import com.example.parcial_2.model.data.RecetaRequest
import com.example.parcial_2.model.remote.RetrofitClient

class RecetaRepository {

    private val api = RetrofitClient.apiService

    suspend fun getRecetas(): Result<List<Receta>> {
        return try {
            val response = api.getRecetas()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "error getting recipes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun crearReceta(receta: RecetaRequest): Result<Receta> {
        return try {
            val response = api.crearReceta(receta)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "error creating recipe"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarReceta(id: String, receta: RecetaRequest): Result<Receta> {
        return try {
            val response = api.actualizarReceta(id, receta)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "error updating recipe"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarReceta(id: String): Result<Unit> {
        return try {
            val response = api.eliminarReceta(id)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "error deleting recipe"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}