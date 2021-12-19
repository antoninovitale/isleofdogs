package com.antoninovitale.dogs.breeds.api

import com.antoninovitale.dogs.common.coroutines.DispatcherProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class BreedsRemoteDataSourceTest {

    private val breedsService: BreedsService = mockk()
    private val dispatcher: DispatcherProvider = mockk()

    private val sut: BreedsRemoteDataSource =
        BreedsRemoteDataSource(breedsService, dispatcher)

    @Test
    fun `getBreeds succeeds`() = runBlockingTest {
        val breeds = Breeds(emptyList())
        coEvery { dispatcher.io() } returns TestCoroutineDispatcher()
        coEvery { breedsService.getBreeds() } returns breeds

        val actual = sut.getBreeds()

        assertEquals(breeds, actual)
    }

    @Test
    fun `getBreeds fails`() = runBlockingTest {
        coEvery { dispatcher.io() } returns TestCoroutineDispatcher()
        coEvery { breedsService.getBreeds() } throws Exception()

        assertFailsWith<Exception> { sut.getBreeds() }
    }
}
