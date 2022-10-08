package binar.academy.challenge_chapter5.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class User(
    val username : String,
    val fullname : String? = null,
    val bornDate : String? = null,
    val address : String? = null
) : Parcelable
