package com.antoninovitale.dogs.breeds.data

import com.antoninovitale.dogs.breeds.api.Breed
import com.antoninovitale.dogs.breeds.api.Breeds
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class BreedsDomainMapperTest {

    private val breedDomainMapper: BreedDomainMapper = mockk()

    private val sut: BreedsDomainMapper = BreedsDomainMapper(breedDomainMapper)

    @Test
    fun `maps breeds`() {
        val breeds: List<Breed> =
            listOf(
                Breed("breed1", listOf("sub_breed_1", "sub_breed_2")),
                Breed("breed2", listOf("sub_breed_3", "sub_breed_4"))
            )
        breeds.forEach {
            every { breedDomainMapper.map(it) } returns BreedDomain(it.name, it.subBreeds)
        }

        val actual = sut.map(Breeds(breeds))

        assertThat(
            actual,
            equalTo(
                BreedsDomain(
                    listOf(
                        BreedDomain("breed1", listOf("sub_breed_1", "sub_breed_2")),
                        BreedDomain("breed2", listOf("sub_breed_3", "sub_breed_4"))
                    )
                )
            )
        )
    }

    @Test
    fun `maps empty`() {
        val actual = sut.map(Breeds(emptyList()))

        assertThat(actual, equalTo(BreedsDomain(emptyList())))
    }
}
