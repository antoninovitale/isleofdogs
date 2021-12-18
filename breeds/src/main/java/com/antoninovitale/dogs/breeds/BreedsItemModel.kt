package com.antoninovitale.dogs.breeds

/**
 * This class holds data used in the UI layer.
 *
 * @param name The name of the breed/sub-breed.
 * @param parent The name of the parent breed of a sub-breed if present.
 */
data class BreedsItemModel(val name: String, val parent: String?)
