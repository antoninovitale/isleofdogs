package com.antoninovitale.dogs.breeds.details.api

import com.google.gson.annotations.SerializedName

data class BreedImages(

    @SerializedName("message")
    val urls: List<String>
)
