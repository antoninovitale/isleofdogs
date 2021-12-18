package com.antoninovitale.dogs.breeds.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(ViewModelComponent::class)
@Module
object BreedsApiModule {

    private const val DOGS_API_BASE_URL = "https://dog.ceo/api/"

    @Provides
    fun provideGson(breedsDeserializer: BreedsDeserializer): Gson =
        GsonBuilder()
            .registerTypeAdapter(Breeds::class.java, breedsDeserializer)
            .create()

    @Provides
    fun provideBreedsService(gson: Gson): BreedsService {
        return Retrofit.Builder()
            .baseUrl(DOGS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                ).build()
            )
            .build()
            .create(BreedsService::class.java)
    }
}
