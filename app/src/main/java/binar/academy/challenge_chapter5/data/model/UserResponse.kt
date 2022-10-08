package binar.academy.challenge_chapter5.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("bornDate")
    val bornDate: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("id")
    val id: String
) : Parcelable