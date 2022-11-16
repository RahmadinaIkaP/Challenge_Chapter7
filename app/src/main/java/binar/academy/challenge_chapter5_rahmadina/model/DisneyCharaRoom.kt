package binar.academy.challenge_chapter5_rahmadina.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "disneyChar")
data class DisneyCharaRoom(
    @PrimaryKey
    val id : Int,
    val name : String,
    val imageUrl : String,
    // tadinya mau munculin favorit berdasarkan user, udah bisa tiba2 error ga jelas :(
    val username : String
) : Parcelable