package binar.academy.challenge_chapter5.data.network.users

import binar.academy.challenge_chapter5.utils.Constant.Companion.BASE_URL_USER
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UserApiService {

    val instance : UserEndPoint by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_USER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(UserEndPoint::class.java)
    }
}