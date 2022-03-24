package com.mikirinkode.reactivesearchexample.api

import com.mikirinkode.reactivesearchexample.model.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("mapbox.places/{query}.json")
    suspend fun getCountry(
        @Path("query") query: String,
        @Query("access_token") accessToken: String,
        @Query("autocomplete") autoComplete: Boolean = true
    ): PlaceResponse
}

// @Path digunakan untuk menampung query yang dicari user
// menggunakan path karena kita perlu mengubah isinya setiap
// melakukan pencarian