package binar.academy.challenge_chapter5.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import binar.academy.challenge_chapter5.model.DisneyCharaRoom
import binar.academy.challenge_chapter5.model.User

@Database(entities = [DisneyCharaRoom::class, User::class], version = 1, exportSchema = false)
abstract class DisneyDB : RoomDatabase() {

    abstract fun disneyDao() : DisneyDao
}