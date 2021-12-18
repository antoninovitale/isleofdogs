package com.antoninovitale.dogs.breeds

import com.antoninovitale.dogs.breeds.data.BreedsDomain
import com.antoninovitale.dogs.breeds.data.BreedsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class BreedsViewModelTest {

    private val repository: BreedsRepository = mockk()
    private val mapper: BreedsItemModelsMapper = mockk()

    private val sut: BreedsViewModel = BreedsViewModel(repository, mapper)

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `get images for a breed`() = mainCoroutineRule.runBlockingTest {
        coEvery { repository.getBreeds() } returns BreedsDomain(listOf())
        every { mapper.map(BreedsDomain(listOf())) } returns listOf()

        sut.loadData()

        // We need to collect values from the flow in a coroutine.
        // The coroutine must be cancelled otherwise we will get an error like the one below.
        // Test finished with active jobs: ["coroutine#12":StandaloneCoroutine{Active}@e53b03]
        launch {
            sut.itemsState.collectLatest {
                assertEquals(BreedsViewModel.ItemsState.ItemsReady(emptyList()), it)
            }
        }.cancel()
    }

    @Test
    fun `error getting images for a breed`() = mainCoroutineRule.runBlockingTest {
        val exception = Exception()
        coEvery { repository.getBreeds() } throws exception
        every { mapper.map(BreedsDomain(listOf())) } returns listOf()

        sut.loadData()

        // We need to collect values from the flow in a coroutine.
        // The coroutine must be cancelled otherwise we will get an error like the one below.
        // Test finished with active jobs: ["coroutine#12":StandaloneCoroutine{Active}@e53b03]
        launch {
            sut.itemsState.collectLatest {
                assertEquals(BreedsViewModel.ItemsState.ItemsFailure(exception), it)
            }
        }.cancel()
    }

    @Test
    fun `on item click`() = mainCoroutineRule.runBlockingTest {
        val item = BreedsItemModel("breed", "parent")

        sut.onItemClicked(item)

        // We need to collect values from the flow in a coroutine.
        // The coroutine must be cancelled otherwise we will get an error like the one below.
        // Test finished with active jobs: ["coroutine#12":StandaloneCoroutine{Active}@e53b03]
        launch {
            sut.itemClickResult.collectLatest {
                assertEquals(item, it)
            }
        }.cancel()
    }
}
