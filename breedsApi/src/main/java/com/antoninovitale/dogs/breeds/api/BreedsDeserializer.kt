package com.antoninovitale.dogs.breeds.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import javax.inject.Inject

class BreedsDeserializer @Inject constructor() : JsonDeserializer<Breeds> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Breeds =
        try {
            val message = json.asJsonObject.get("message").asJsonObject
            val breeds = message.keySet().map { breed ->
                Breed(
                    name = breed,
                    subBreeds = message.get(breed).asJsonArray.map { it.asString }
                )
            }
            Breeds(breeds)
        } catch (ex: Exception) {
            throw JsonParseException("Breeds cannot be parsed correctly: $json", ex)
        }
}
