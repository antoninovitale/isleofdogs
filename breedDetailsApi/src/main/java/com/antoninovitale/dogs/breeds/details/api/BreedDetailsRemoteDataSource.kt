package com.antoninovitale.dogs.breeds.details.api

import com.antoninovitale.dogs.common.coroutines.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

// TODO Needs tests
class BreedDetailsRemoteDataSource @Inject constructor(
    private val breedDetailsService: BreedDetailsService,
    private val dispatcher: DispatcherProvider
) {

    suspend fun getBreedImages(breed: String): BreedImages =
        withContext(dispatcher.io()) {
            breedDetailsService.getBreedImages(breed)
        }

    suspend fun getSubBreedImages(breed: String, subBreed: String): BreedImages =
        withContext(dispatcher.io()) {
            breedDetailsService.getSubBreedImages(breed, subBreed)
        }
}
