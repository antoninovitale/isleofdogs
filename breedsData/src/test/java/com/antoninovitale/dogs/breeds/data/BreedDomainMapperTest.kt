package com.antoninovitale.dogs.breeds.data

import com.antoninovitale.dogs.breeds.api.Breed
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class BreedDomainMapperTest {

    private val sut: BreedDomainMapper = BreedDomainMapper()

    @Test
    fun `maps breed with sub-breeds`() {
        val actual = sut.map(Breed("name", listOf("sub_breed_1", "sub_breed_2")))

        assertThat(
            actual,
            equalTo(
                BreedDomain("name", listOf("sub_breed_1", "sub_breed_2"))
            )
        )
    }

    @Test
    fun `maps breed without sub-breeds`() {
        val actual = sut.map(Breed("name", emptyList()))

        assertThat(
            actual,
            equalTo(
                BreedDomain("name", emptyList())
            )
        )
    }
}
