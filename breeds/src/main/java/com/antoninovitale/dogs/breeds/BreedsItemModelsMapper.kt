package com.antoninovitale.dogs.breeds

import com.antoninovitale.dogs.breeds.data.BreedsDomain
import javax.inject.Inject

class BreedsItemModelsMapper @Inject constructor() {

    fun map(breeds: BreedsDomain): List<BreedsItemModel> {
        val models: MutableList<BreedsItemModel> = mutableListOf()
        breeds.breeds.forEach { breed ->
            models.add(BreedsItemModel(name = breed.name, parent = null))
            models.addAll(
                breed.subBreeds.map { BreedsItemModel(name = it, parent = breed.name) }
            )
        }
        return models
    }
}
