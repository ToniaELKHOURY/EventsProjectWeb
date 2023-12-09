/*
    EventService.kt

    Cette interface, EventService, définit les méthodes qui correspondent aux opérations d'API
    disponibles pour les événements. Ces méthodes utilisent les annotations Retrofit pour décrire
    les types de requêtes HTTP, les paramètres, les corps de requête, les chemins, etc.
*/

package com.example.events_frontend_project

import android.telecom.Call
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {

    @POST("events/")
    fun getList(@Body event: Event): retrofit2.Call<Event>;

    @GET("events/")
    fun findMapsInfo(@Query("page") page: Int,
                     @Query("rows") rows: Int,
                     @Query("offset") offset: Int,
                     @Query("sortBy") sortBy: String,
                     @Query("fav") favorites: Int,
                     @Query("geoloc") geoloc: Int,
    ): retrofit2.Call<List<MapsInfo>>;

    @GET("events/")
    fun findAll(@Query("page") page: Int,
                @Query("rows") rows: Int,
                @Query("offset") offset: Int,
                @Query("sortBy") sortBy: String,
                @Query("fav") favorites: Int,
                @Query("geoloc") geoloc: Int,
                @Query("refresh") refresh: Int,
    ): retrofit2.Call<ResponseReturned>;

    @GET("events/{id}")
    fun findOneById(@Path("id") id: String) : retrofit2.Call<Event>;

    @FormUrlEncoded
    @POST("events/search/title")
    fun searchByTitle(@Field("title") title:String): retrofit2.Call<ArrayList<Event>>;

    @PUT("events/{id}")
    fun toggleFavorite(@Path("id") id: String): retrofit2.Call<JSONObject>;

    @PATCH("/{id}")
    fun update(@Path("id") id: String, @Body updateEventDto:Event);

    @DELETE("events/{id}")
    fun remove(@Path("id") id: String);

}