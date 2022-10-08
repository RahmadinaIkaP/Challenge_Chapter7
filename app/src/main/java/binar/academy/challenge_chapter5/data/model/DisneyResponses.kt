package binar.academy.challenge_chapter5.data.model


import com.google.gson.annotations.SerializedName

data class DisneyResponses(
    @SerializedName("count")
    val count: Int,
    @SerializedName("data")
    val data : List<DisneyCharaResponse>,
    @SerializedName("nextPage")
    val nextPage: String,
    @SerializedName("totalPages")
    val totalPages: Int
)