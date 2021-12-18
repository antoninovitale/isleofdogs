package com.antoninovitale.dogs.breeds.details

import com.antoninovitale.dogs.breeds.details.data.BreedDetailsRepository
import io.mockk.coEvery
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
class BreedDetailsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository: BreedDetailsRepository = mockk()

    private val sut: BreedDetailsViewModel = BreedDetailsViewModel(repository)

    @Test
    fun `get images for a breed`() = mainCoroutineRule.runBlockingTest {
        coEvery { repository.getBreedImages("test") } returns listOf()

        sut.loadData("test", null)

        // We need to collect values from the flow in a coroutine.
        // The coroutine must be cancelled otherwise we will get an error like the one below.
        // Test finished with active jobs: ["coroutine#12":StandaloneCoroutine{Active}@e53b03]
        launch {
            sut.itemsState.collectLatest {
                assertEquals(BreedDetailsViewModel.ItemsState.ItemsReady(emptyList()), it)
            }
        }.cancel()
    }

    @Test
    fun `error getting images for a breed`() = mainCoroutineRule.runBlockingTest {
        val exception = Exception()
        coEvery { repository.getBreedImages("test") } throws exception

        sut.loadData("test", null)

        // We need to collect values from the flow in a coroutine.
        // The coroutine must be cancelled otherwise we will get an error like the one below.
        // Test finished with active jobs: ["coroutine#12":StandaloneCoroutine{Active}@e53b03]
        launch {
            sut.itemsState.collectLatest {
                assertEquals(BreedDetailsViewModel.ItemsState.ItemsFailure(exception), it)
            }
        }.cancel()
    }

    @Test
    fun `get images for a sub-breed`() = mainCoroutineRule.runBlockingTest {
        val breed = "test"
        val parent = "parent"
        coEvery { repository.getSubBreedImages(parent, breed) } returns listOf()

        sut.loadData(breed, parent)

        launch {
            sut.itemsState.collectLatest {
                assertEquals(BreedDetailsViewModel.ItemsState.ItemsReady(emptyList()), it)
            }
        }.cancel()
    }

    @Test
    fun `error getting images for a sub-breed`() = mainCoroutineRule.runBlockingTest {
        val breed = "test"
        val parent = "parent"
        val exception = Exception()
        coEvery { repository.getSubBreedImages(parent, breed) } throws exception

        sut.loadData(breed, parent)

        launch {
            sut.itemsState.collectLatest {
                assertEquals(BreedDetailsViewModel.ItemsState.ItemsFailure(exception), it)
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
