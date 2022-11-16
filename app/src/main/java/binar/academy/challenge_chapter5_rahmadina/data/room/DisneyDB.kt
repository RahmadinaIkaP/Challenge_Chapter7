package binar.academy.challenge_chapter5_rahmadina.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import binar.academy.challenge_chapter5_rahmadina.model.DisneyCharaRoom
import binar.academy.challenge_chapter5_rahmadina.model.User

@Database(entities = [DisneyCharaRoom::class, User::class], version = 1, exportSchema = false)
abstract class DisneyDB : RoomDatabase() {

    abstract fun disneyDao() : DisneyDao
}