package binar.academy.challenge_chapter5_rahmadina.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import binar.academy.challenge_chapter5_rahmadina.model.DisneyCharaResponse
import binar.academy.challenge_chapter5_rahmadina.model.DisneyResponses
import binar.academy.challenge_chapter5_rahmadina.data.network.DisneyCharaEndPoint
import binar.academy.challenge_chapter5_rahmadina.data.room.DisneyRepository
import binar.academy.challenge_chapter5_rahmadina.model.DisneyCharaRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DisneyCharaViewModel @Inject constructor(private val disneyApi : DisneyCharaEndPoint, private val repository: DisneyRepository) : ViewModel() {

    private val liveDataDisneyChara : MutableLiveData<DisneyResponses?> = MutableLiveData()
    private val detailDisneyCharaLiveData : MutableLiveData<DisneyCharaResponse?> = MutableLiveData()
    private val liveDataFavDisneyChar : MutableLiveData<List<DisneyCharaRoom>> = MutableLiveData()

    fun getLiveDataDisneyChara() : MutableLiveData<DisneyResponses?> = liveDataDisneyChara
    fun getLiveDataDisneyCharaDetail() : MutableLiveData<DisneyCharaResponse?> = detailDisneyCharaLiveData
    fun getAllFavDisneyObserver() : MutableLiveData<List<DisneyCharaRoom>> = liveDataFavDisneyChar

    fun showDisneyCharaList(page : Int){
        disneyApi.getAllDisneyCharacters(page)
            .enqueue(object : Callback<DisneyResponses>{
                override fun onResponse(
                    call: Call<DisneyResponses>,
                    response: Response<DisneyResponses>
                ) {
                    if (response.isSuccessful){
                        liveDataDisneyChara.postValue(response.body())
                    }else{
                        liveDataDisneyChara.postValue(null)
                        Log.d("notSuccess", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<DisneyResponses>, t: Throwable) {
                    liveDataDisneyChara.postValue(null)
                    Log.d("Failed",t.message.toString())
                }

            })
    }

    fun showDetailDisneyChara(id : Int){
        disneyApi.getSingleDisneyChara(id)
            .enqueue(object : Callback<DisneyCharaResponse>{
                override fun onResponse(
                    call: Call<DisneyCharaResponse>,
                    response: Response<DisneyCharaResponse>
                ) {
                    if (response.isSuccessful){
                        detailDisneyCharaLiveData.postValue(response.body())
                    }else{
                        detailDisneyCharaLiveData.postValue(null)
                        Log.d("notSuccess", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<DisneyCharaResponse>, t: Throwable) {
                    detailDisneyCharaLiveData.postValue(null)
                    Log.d("Failed", t.message.toString())
                }

            })
    }

    fun getAllFavDisney(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllFavDisney()
            liveDataFavDisneyChar.postValue(repository.getAllFavDisney())
        }
    }

    suspend fun checkFavDisney(id : Int) = repository.checkFavDisney(id)

    fun insertFavDisney(disney : DisneyCharaRoom){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavDisneyChar(disney)
        }
    }

    fun deleteFavDisney(disney: DisneyCharaRoom){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavDisneyChar(disney)
        }
    }
}