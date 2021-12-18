package com.antoninovitale.dogs.breeds

import com.antoninovitale.dogs.breeds.data.BreedsDomain
import com.antoninovitale.dogs.breeds.data.BreedsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.coroutines.ContinuationInterceptor
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class BreedsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: BreedsRepository = mockk()
    private val mapper: BreedsItemModelsMapper = mockk()

    private val sut: BreedsViewModel = BreedsViewModel(repository, mapper)

    @Test
    fun `get images for a breed`() = mainCoroutineRule.runBlockingTest {
        every { mapper.map(BreedsDomain(listOf())) } returns listOf()
        coEvery { repository.getBreeds() } returns BreedsDomain(listOf())

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
        every { mapper.map(BreedsDomain(listOf())) } returns listOf()
        coEvery { repository.getBreeds() } throws exception

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
}

@ExperimentalCoroutinesApi
class MainCoroutineRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
