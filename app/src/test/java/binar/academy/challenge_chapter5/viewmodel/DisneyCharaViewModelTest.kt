package binar.academy.challenge_chapter5.viewmodel

import binar.academy.challenge_chapter5.data.network.DisneyCharaEndPoint
import binar.academy.challenge_chapter5.model.DisneyCharaResponse
import binar.academy.challenge_chapter5.model.DisneyResponses
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Call

class DisneyCharaViewModelTest{

    lateinit var service : DisneyCharaEndPoint

    @Before
    fun setUp(){
        service = mockk()
    }

    @Test
    fun getDisneyDataTest() : Unit = runBlocking {

        val responAllDisney = mockk<Call<DisneyResponses>>()

        every {
            runBlocking {
                service.getAllDisneyCharacters(5)
            }
        } returns responAllDisney

        val result = service.getAllDisneyCharacters(5)

        verify {
            runBlocking { service.getAllDisneyCharacters(5) }
        }
        assertEquals(result, responAllDisney)
    }

    @Test
    fun getDetailDisneyTest(){
        val responDetailDisney = mockk<Call<DisneyCharaResponse>>()

        every {
            service.getSingleDisneyChara(2)
        } returns responDetailDisney

        val result = service.getSingleDisneyChara(2)

        verify {
            service.getSingleDisneyChara(2)
        }
        assertEquals(result, responDetailDisney)
    }
}