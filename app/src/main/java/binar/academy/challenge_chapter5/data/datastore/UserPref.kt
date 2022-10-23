package binar.academy.challenge_chapter5.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "user")
class UserPref(private val context : Context) {

    val USERNAME = stringPreferencesKey("username")
    val ID_USER = stringPreferencesKey("id")
    val STATUS_LOGIN = booleanPreferencesKey("isLogin")

    // function for save data
    suspend fun saveData(id : String, username : String, status: Boolean){
        context.dataStore.edit {
            it[ID_USER] = id
            it[USERNAME] = username
            it[STATUS_LOGIN] = status
        }
    }

    suspend fun saveUpdatedUsername(editedUsername: String){
        context.dataStore.edit {
            it[USERNAME] = editedUsername
        }
    }

    // variable for read data
    val username : Flow<String> = context.dataStore.data
        .map {
            it[USERNAME] ?: "Undefined"
        }

    val idUser : Flow<String> = context.dataStore.data
        .map {
            it[ID_USER] ?: "Undefined"
        }

    val statusLogin : Flow<Boolean> = context.dataStore.data
        .map {
            it[STATUS_LOGIN] ?: false
        }

    // function for logout
    suspend fun logoutUser(){
        context.dataStore.edit {
            it.remove(STATUS_LOGIN)
        }
    }

}