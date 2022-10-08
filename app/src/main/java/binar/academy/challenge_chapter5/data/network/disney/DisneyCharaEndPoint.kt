package binar.academy.challenge_chapter5.data.network.disney

import binar.academy.challenge_chapter5.data.model.DisneyCharaResponse
import binar.academy.challenge_chapter5.data.model.DisneyResponses
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