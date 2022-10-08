package binar.academy.challenge_chapter5.data.network.disney

import binar.academy.challenge_chapter5.data.network.users.UserEndPoint
import binar.academy.challenge_chapter5.utils.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DisneyCharaApiService {

    val instance : DisneyCharaEndPoint by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL_DISNEY)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(DisneyCharaEndPoint::class.java)
    }
}