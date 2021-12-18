package com.antoninovitale.dogs.breeds.api

import com.google.gson.annotations.SerializedName

data class Breeds(val list: List<Breed>)

data class Breed(val name: String, val subBreeds: List<String>)

data class Images(

    @SerializedName("message")
    val urls: List<String>
)
