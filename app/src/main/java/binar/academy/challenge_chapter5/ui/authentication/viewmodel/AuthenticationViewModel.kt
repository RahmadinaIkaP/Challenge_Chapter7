package binar.academy.challenge_chapter5.ui.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import binar.academy.challenge_chapter5.data.model.User
import binar.academy.challenge_chapter5.data.model.UserResponse
import binar.academy.challenge_chapter5.data.network.users.UserApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AuthenticationViewModel() :ViewModel() {

    private val loginLiveData : MutableLiveData<List<UserResponse>?> = MutableLiveData()
    private val registerLiveData : MutableLiveData<UserResponse?> = MutableLiveData()
    private val editUserLiveData : MutableLiveData<UserResponse?> = MutableLiveData()
    private val currentUserLiveData : MutableLiveData<UserResponse?> = MutableLiveData()

    fun loginObserver() : MutableLiveData<List<UserResponse>?> = loginLiveData
    fun registerObserver() : MutableLiveData<UserResponse?> = registerLiveData
    fun editUserObserver() : MutableLiveData<UserResponse?> = editUserLiveData
    fun currentUserObserver() : MutableLiveData<UserResponse?> = currentUserLiveData

    fun login(){
        UserApiService.instance.getUsers()
            .enqueue(object : Callback<List<UserResponse>>{
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    if (response.isSuccessful){
                        loginLiveData.postValue(response.body())
                    }else{
                        loginLiveData.postValue(null)
                        Log.d("notSuccess", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    loginLiveData.postValue(null)
                    Log.d("Failed", t.message.toString())
                }

            })
    }

    fun register(
        username : String, email : String, password : String,
        fullname : String, bornDate : String, address : String){

        val dateNow = Calendar.getInstance().time

        UserApiService.instance.register(
            UserResponse(dateNow.toString(), username, email, password, fullname, bornDate, address, ""))
            .enqueue(object : Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful){
                        registerLiveData.postValue(response.body())
                    }else{
                        registerLiveData.postValue(null)
                        Log.d("notSuccess", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    registerLiveData.postValue(null)
                    Log.d("Failed", t.message.toString())
                }

            })
    }

    fun getCurrentUser(id: String){
        UserApiService.instance.getCurrentUser(id)
            .enqueue(object : Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful){
                        currentUserLiveData.postValue(response.body())
                    }else{
                        currentUserLiveData.postValue(null)
                        Log.d("notSuccess", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    currentUserLiveData.postValue(null)
                    Log.d("Failed", t.message.toString())
                }

            })
    }

    fun editUserProfile(id : String, username : String, fullname: String, bornDate: String, address: String){
        UserApiService.instance.editUserProfile(id, User(username, fullname, bornDate, address))
            .enqueue(object : Callback<UserResponse>{
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful){
                        editUserLiveData.postValue(response.body())
                    }else{
                        editUserLiveData.postValue(null)
                        Log.d("notSuccess", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    editUserLiveData.postValue(null)
                    Log.d("Failed", t.message.toString())
                }

            })
    }
}