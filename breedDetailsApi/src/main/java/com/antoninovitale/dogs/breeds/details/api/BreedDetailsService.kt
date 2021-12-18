package com.antoninovitale.dogs.breeds.details.api

import retrofit2.http.GET
import retrofit2.http.Path

interface BreedDetailsService {

    @GET("breed/{breed}/images")
    suspend fun getBreedImages(@Path("breed") breed: String): BreedImages

    @GET("breed/{breed}/{sub_breed}/images")
    suspend fun getSubBreedImages(
        @Path("breed") breed: String,
        @Path("sub_breed") subBreed: String
    ): BreedImages
}