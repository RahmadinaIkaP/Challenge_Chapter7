package binar.academy.challenge_chapter5_rahmadina.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DisneyCharaResponse(
    @SerializedName("allies")
    val allies: List<String>,
    @SerializedName("enemies")
    val enemies: List<String>,
    @SerializedName("films")
    val films: List<String>,
    @SerializedName("_id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("parkAttractions")
    val parkAttractions: List<String>,
    @SerializedName("shortFilms")
    val shortFilms: List<String>,
    @SerializedName("tvShows")
    val tvShows: List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("videoGames")
    val videoGames: List<String>
) : Parcelable