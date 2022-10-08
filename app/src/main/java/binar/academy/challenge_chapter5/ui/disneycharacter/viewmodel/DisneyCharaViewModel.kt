package binar.academy.challenge_chapter5.ui.disneycharacter.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import binar.academy.challenge_chapter5.data.model.DisneyCharaResponse
import binar.academy.challenge_chapter5.data.model.DisneyResponses
import binar.academy.challenge_chapter5.data.network.disney.DisneyCharaApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisneyCharaViewModel : ViewModel() {

    private val liveDataDisneyChara : MutableLiveData<DisneyResponses?> = MutableLiveData()
    private val detailDisneyCharaLiveData : MutableLiveData<DisneyCharaResponse?> = MutableLiveData()

    fun getLiveDataDisneyChara() : MutableLiveData<DisneyResponses?> = liveDataDisneyChara
    fun getLiveDataDisneyCharaDetail() : MutableLiveData<DisneyCharaResponse?> = detailDisneyCharaLiveData

    fun showDisneyCharaList(page : Int){
        DisneyCharaApiService.instance.getAllDisneyCharacters(page)
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
        DisneyCharaApiService.instance.getSingleDisneyChara(id)
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
}