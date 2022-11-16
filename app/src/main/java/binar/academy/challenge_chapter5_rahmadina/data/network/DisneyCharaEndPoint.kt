package binar.academy.challenge_chapter5_rahmadina.data.network

import binar.academy.challenge_chapter5_rahmadina.model.DisneyCharaResponse
import binar.academy.challenge_chapter5_rahmadina.model.DisneyResponses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DisneyCharaEndPoint {

    @GET("characters")
    fun getAllDisneyCharacters(
        @Query("page") page : Int,
    ) : Call<DisneyResponses>

    @GET("characters/{id}")
    fun getSingleDisneyChara(@Path("id") id : Int) : Call<DisneyCharaResponse>
}