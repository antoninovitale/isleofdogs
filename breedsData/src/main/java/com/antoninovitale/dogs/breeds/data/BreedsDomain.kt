package com.antoninovitale.dogs.breeds.data

class BreedsDomain(val breeds: List<BreedDomain>)

data class BreedDomain(val name: String, val subBreeds: List<String>)
