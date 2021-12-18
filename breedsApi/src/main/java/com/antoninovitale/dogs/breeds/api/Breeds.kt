package com.antoninovitale.dogs.breeds.api

data class Breeds(val list: List<Breed>)

data class Breed(val name: String, val subBreeds: List<String>)
