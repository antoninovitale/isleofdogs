package com.antoninovitale.dogs.breeds.details.data

import com.antoninovitale.dogs.breeds.details.api.BreedDetailsRemoteDataSource
import com.antoninovitale.dogs.breeds.details.api.BreedImages
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class BreedDetailsRepositoryTest {

    private val remoteDataSource: BreedDetailsRemoteDataSource = mockk()

    private val sut: BreedDetailsRepository = BreedDetailsRepository(remoteDataSource)

    @Test
    fun `getBreedImages succeeds`() = runBlockingTest {
        val expected = BreedImages(emptyList())
        val breed = "breed"
        coEvery { remoteDataSource.getBreedImages(breed) } returns expected

        val actual = sut.getBreedImages(breed)

        assertEquals(expected.urls, actual)
    }

    @Test
    fun `getBreedImages fails`() = runBlockingTest {
        val breed = "breed"
        coEvery { remoteDataSource.getBreedImages(breed) } throws Exception()

        assertFailsWith<Exception> { sut.getBreedImages(breed) }
    }

    @Test
    fun `getSubBreedImages succeeds`() = runBlockingTest {
        val expected = BreedImages(emptyList())
        val breed = "breed"
        val subBreed = "sub-breed"
        coEvery { remoteDataSource.getSubBreedImages(breed, subBreed) } returns expected

        val actual = sut.getSubBreedImages(breed, subBreed)

        assertEquals(expected.urls, actual)
    }

    @Test
    fun `getSubBreedImages fails`() = runBlockingTest {
        val breed = "breed"
        val subBreed = "sub-breed"
        coEvery { remoteDataSource.getSubBreedImages(breed, subBreed) } throws Exception()

        assertFailsWith<Exception> { sut.getSubBreedImages(breed, subBreed) }
    }
}
