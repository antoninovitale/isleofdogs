package com.antoninovitale.dogs.breeds.data

import com.antoninovitale.dogs.breeds.api.BreedsRemoteDataSource
import javax.inject.Inject

// TODO Needs tests
class BreedsRepository @Inject constructor(
    private val remoteDataSource: BreedsRemoteDataSource,
    private val breedsDomainMapper: BreedsDomainMapper
) {

    suspend fun getBreeds(): BreedsDomain =
        breedsDomainMapper.map(remoteDataSource.getBreeds())
}
