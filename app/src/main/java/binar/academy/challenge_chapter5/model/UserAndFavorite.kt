package binar.academy.challenge_chapter5.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserAndFavorite(
    @Embedded val disneyCharaRoom: DisneyCharaRoom,
    @Relation(
        parentColumn = "username",
        entityColumn = "username"
    )
    val user: User
)
