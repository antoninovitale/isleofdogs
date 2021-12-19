package com.antoninovitale.dogs.breeds

import com.antoninovitale.dogs.breeds.data.BreedDomain
import com.antoninovitale.dogs.breeds.data.BreedsDomain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class BreedsItemModelsMapperTest {

    private val sut: BreedsItemModelsMapper = BreedsItemModelsMapper()

    @Test
    fun `maps breeds and sub-breeds`() {
        val breeds: List<BreedDomain> =
            listOf(
                BreedDomain("breed1", listOf("sub_breed_1", "sub_breed_2")),
                BreedDomain("breed2", listOf("sub_breed_3", "sub_breed_4"))
            )
        val breedsDomain = BreedsDomain(breeds)

        val actual = sut.map(breedsDomain)

        assertThat(
            actual,
            equalTo(
                listOf(
                    BreedsItemModel("breed1", null),
                    BreedsItemModel("sub_breed_1", "breed1"),
                    BreedsItemModel("sub_breed_2", "breed1"),
                    BreedsItemModel("breed2", null),
                    BreedsItemModel("sub_breed_3", "breed2"),
                    BreedsItemModel("sub_breed_4", "breed2")
                )
            )
        )
    }

    @Test
    fun `maps only breeds`() {
        val breeds: List<BreedDomain> =
            listOf(
                BreedDomain("breed1", emptyList()),
                BreedDomain("breed2", emptyList())
            )
        val breedsDomain = BreedsDomain(breeds)

        val actual = sut.map(breedsDomain)

        assertThat(
            actual,
            equalTo(
                listOf(
                    BreedsItemModel("breed1", null),
                    BreedsItemModel("breed2", null)
                )
            )
        )
    }

    @Test
    fun `maps empty`() {
        val breedsDomain = BreedsDomain(emptyList())

        val actual = sut.map(breedsDomain)

        assertThat(actual, equalTo(emptyList()))
    }
}
