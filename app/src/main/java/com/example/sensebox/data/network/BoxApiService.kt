package com.example.sensebox.data.network

import com.example.sensebox.data.model.Box
import com.example.sensebox.data.model.BoxDetail
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BoxApiService  {

    @GET("boxes")
    suspend fun getBoxes(
        @Query("near") near: String = "$BREMEN_LONGITUDE,$BREMEN_LATITUDE",
        @Query("maxDistance") maxDistance : Int,
    ) : List<Box>

    @GET("boxes/{id}")
    suspend fun getBoxDetail(
       @Path("id") id: String
    ) : BoxDetail


    companion object {
        const val BREMEN_LONGITUDE = "8.8071646";
        const val BREMEN_LATITUDE = "53.0758196";
        private const val BASE_URL = "https://api.opensensemap.org/"

        fun create () : BoxApiService {
             val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val resp = chain.proceed(chain.request())
                    println(resp)
                    resp
                }.build()

            val contentType = "application/json".toMediaType()
            val json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory(contentType))
                .build()
                .create(BoxApiService::class.java)
        }
    }
}