package binar.academy.challenge_chapter5.di

import android.content.Context
import androidx.room.Room
import binar.academy.challenge_chapter5.data.network.DisneyCharaEndPoint
import binar.academy.challenge_chapter5.data.network.UserEndPoint
import binar.academy.challenge_chapter5.data.room.DisneyDB
import binar.academy.challenge_chapter5.utils.Constant.Companion.BASE_URL_DISNEY
import binar.academy.challenge_chapter5.utils.Constant.Companion.BASE_URL_USER
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private  val logging : HttpLoggingInterceptor
        get(){
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    // For retrofit
    @Provides
    @Singleton
    @Named("Disney")
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL_DISNEY)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("User")
    fun provideUserRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL_USER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideDisneyCharApi(@Named("Disney") retrofit: Retrofit) : DisneyCharaEndPoint =
        retrofit.create(DisneyCharaEndPoint::class.java)

    @Provides
    @Singleton
    fun provideUserApi(@Named("User") retrofit: Retrofit) : UserEndPoint =
        retrofit.create(UserEndPoint::class.java)

    // For room database
    @Provides
    @Singleton
    fun provideDisneyDatabase(@ApplicationContext context : Context) =
        Room.databaseBuilder(
            context,
            DisneyDB::class.java,
            "disney.db"
        ).build()

    @Provides
    @Singleton
    fun provideDisneyDao(db : DisneyDB) = db.disneyDao()
}