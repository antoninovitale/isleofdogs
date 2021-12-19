package com.antoninovitale.dogs.breeds.details.api

import com.antoninovitale.dogs.common.coroutines.DispatcherProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class BreedDetailsRemoteDataSourceTest {

    private val breedDetailsService: BreedDetailsService = mockk()
    private val dispatcher: DispatcherProvider = mockk()

    private val sut: BreedDetailsRemoteDataSource =
        BreedDetailsRemoteDataSource(breedDetailsService, dispatcher)

    @Test
    fun `getBreedImages succeeds`() = runBlockingTest {
        coEvery { dispatcher.io() } returns TestCoroutineDispatcher()
        val breed = "breed"
        val breedImages = BreedImages(emptyList())
        coEvery { breedDetailsService.getBreedImages(breed) } returns breedImages

        val actual = sut.getBreedImages(breed)

        assertEquals(breedImages, actual)
    }

    @Test
    fun `getBreedImages fails`() = runBlockingTest {
        coEvery { dispatcher.io() } returns TestCoroutineDispatcher()
        val breed = "breed"
        coEvery { breedDetailsService.getBreedImages(breed) } throws Exception()

        assertFailsWith<Exception> { sut.getBreedImages(breed) }
    }

    @Test
    fun `getSubBreedImages succeeds`() = runBlockingTest {
        coEvery { dispatcher.io() } returns TestCoroutineDispatcher()
        val breed = "breed"
        val subBreed = "sub-breed"
        val breedImages = BreedImages(emptyList())
        coEvery { breedDetailsService.getSubBreedImages(breed, subBreed) } returns breedImages

        val actual = sut.getSubBreedImages(breed, subBreed)

        assertEquals(breedImages, actual)
    }

    @Test
    fun `getSubBreedImages fails`() = runBlockingTest {
        coEvery { dispatcher.io() } returns TestCoroutineDispatcher()
        val breed = "breed"
        val subBreed = "sub-breed"
        coEvery { breedDetailsService.getSubBreedImages(breed, subBreed) } throws Exception()

        assertFailsWith<Exception> { sut.getSubBreedImages(breed, subBreed) }
    }
}
