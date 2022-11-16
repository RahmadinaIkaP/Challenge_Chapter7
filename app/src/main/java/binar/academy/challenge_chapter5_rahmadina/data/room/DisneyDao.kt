package binar.academy.challenge_chapter5_rahmadina.data.room

import androidx.room.*
import binar.academy.challenge_chapter5_rahmadina.model.DisneyCharaRoom

@Dao
interface DisneyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavDisneyChar(disney : DisneyCharaRoom)

    @Query("SELECT * FROM disneyChar")
    fun getAllFavDisney() : List<DisneyCharaRoom>

    @Query("SELECT count(*) FROM disneyChar WHERE id =:id")
    suspend fun checkFavDisney(id : Int) : Int

    @Delete
    suspend fun deleteFavDisneyChar(disney: DisneyCharaRoom)
}