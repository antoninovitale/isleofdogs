package com.antoninovitale.dogs.breeds.api

import com.antoninovitale.dogs.common.coroutines.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BreedsRemoteDataSource @Inject constructor(
    private val breedsService: BreedsService,
    private val dispatcher: DispatcherProvider
) {

    suspend fun getBreeds(): Breeds =
        withContext(dispatcher.io()) {
            breedsService.getBreeds()
        }
}

