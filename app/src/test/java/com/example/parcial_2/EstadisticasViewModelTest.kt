package com.example.parcial_2

import app.cash.turbine.test
import com.example.parcial_2.viewmodel.EstadisticasUiState
import com.example.parcial_2.viewmodel.EstadisticasViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EstadisticasViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() { Dispatchers.setMain(testDispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `al llamar a cargarDatos, el estado pasa de Idle a Loading`() = runTest {
        val viewModel = EstadisticasViewModel()

        viewModel.uiState.test {
            assert(awaitItem() is EstadisticasUiState.Idle)

            viewModel.cargarDatos("receta_1")
            assert(awaitItem() is EstadisticasUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}