package com.antoninovitale.dogs.breeds.data

import com.antoninovitale.dogs.breeds.api.Breeds
import com.antoninovitale.dogs.breeds.api.BreedsRemoteDataSource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class BreedsRepositoryTest {

    private val remoteDataSource: BreedsRemoteDataSource = mockk()
    private val breedsDomainMapper: BreedsDomainMapper = mockk()

    private val sut: BreedsRepository = BreedsRepository(remoteDataSource, breedsDomainMapper)

    @Test
    fun `getBreeds succeeds`() = runBlockingTest {
        val breeds = Breeds(emptyList())
        coEvery { remoteDataSource.getBreeds() } returns breeds
        val expected = BreedsDomain(emptyList())
        every { breedsDomainMapper.map(breeds) } returns expected

        val actual = sut.getBreeds()

        assertEquals(expected, actual)
    }

    @Test
    fun `getBreeds fails`() = runBlockingTest {
        coEvery { remoteDataSource.getBreeds() } throws Exception()

        assertFailsWith<Exception> { sut.getBreeds() }
    }
}
