package com.antoninovitale.dogs.breeds.details.data

import com.antoninovitale.dogs.breeds.details.api.BreedDetailsRemoteDataSource
import javax.inject.Inject

class BreedDetailsRepository @Inject constructor(
    private val remoteDataSource: BreedDetailsRemoteDataSource
) {

    suspend fun getBreedImages(breed: String): List<String> =
        remoteDataSource.getBreedImages(breed).urls

    suspend fun getSubBreedImages(breed: String, subBreed: String): List<String> =
        remoteDataSource.getSubBreedImages(breed, subBreed).urls
}
