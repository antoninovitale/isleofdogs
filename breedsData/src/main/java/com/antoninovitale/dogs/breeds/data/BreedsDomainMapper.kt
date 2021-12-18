package com.antoninovitale.dogs.breeds.data

import com.antoninovitale.dogs.breeds.api.Breeds
import javax.inject.Inject

class BreedsDomainMapper @Inject constructor(
    private val breedDomainMapper: BreedDomainMapper
) {

    fun map(breeds: Breeds): BreedsDomain =
        BreedsDomain(
            breeds = breeds.list.map {
                breedDomainMapper.map(it)
            }
        )
}

class BreedDomainMapper @Inject constructor() {

    fun map(breed: com.antoninovitale.dogs.breeds.api.Breed): BreedDomain =
        BreedDomain(
            name = breed.name,
            subBreeds = breed.subBreeds
        )
}
