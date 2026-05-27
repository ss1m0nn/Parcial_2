package com.example.parcial_2

import app.cash.turbine.test
import com.example.parcial_2.viewmodel.AgregarRecetaViewModel
import com.example.parcial_2.viewmodel.AgregarUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AgregarRecetaViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() { Dispatchers.setMain(testDispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `guardarReceta emite Loading al iniciar`() = runTest {
        val viewModel = AgregarRecetaViewModel()

        viewModel.uiState.test {
            viewModel.guardarReceta("1", "Nombre", "url", listOf(), listOf(), "Cat", 10, 2)

            assert(awaitItem() is AgregarUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }
}