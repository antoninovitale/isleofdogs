package com.antoninovitale.dogs.breeds.details.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BreedDetailsGson

@InstallIn(ViewModelComponent::class)
@Module
object BreedDetailsApiModule {

    private const val DOGS_API_BASE_URL = "https://dog.ceo/api/"

    @Provides
    @BreedDetailsGson
    fun provideGson(): Gson =
        GsonBuilder().create()

    @Provides
    fun provideBreedDetailsService(@BreedDetailsGson gson: Gson): BreedDetailsService =
        Retrofit.Builder()
            .baseUrl(DOGS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                ).build()
            )
            .build()
            .create(BreedDetailsService::class.java)
}
