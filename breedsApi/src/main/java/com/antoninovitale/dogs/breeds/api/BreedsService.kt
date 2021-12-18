package com.antoninovitale.dogs.breeds.api

import retrofit2.http.GET

interface BreedsService {

    @GET("breeds/list/all")
    suspend fun getBreeds(): Breeds
}