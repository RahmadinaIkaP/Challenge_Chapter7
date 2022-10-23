package binar.academy.challenge_chapter5.data.room

import androidx.lifecycle.LiveData
import binar.academy.challenge_chapter5.model.DisneyCharaRoom
import binar.academy.challenge_chapter5.model.UserAndFavorite
import javax.inject.Inject

class DisneyRepository @Inject constructor(private val disneyDao: DisneyDao){

    fun getAllFavDisney() : List<DisneyCharaRoom> = disneyDao.getAllFavDisney()

    suspend fun checkFavDisney(id : Int) = disneyDao.checkFavDisney(id)

    suspend fun insertFavDisneyChar(disney : DisneyCharaRoom) = disneyDao.insertFavDisneyChar(disney)

    suspend fun deleteFavDisneyChar(disney: DisneyCharaRoom) = disneyDao.deleteFavDisneyChar(disney)
}