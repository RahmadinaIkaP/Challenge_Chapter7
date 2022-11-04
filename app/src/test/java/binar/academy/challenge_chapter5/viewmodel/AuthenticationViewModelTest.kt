package binar.academy.challenge_chapter5.viewmodel

import binar.academy.challenge_chapter5.data.network.UserEndPoint
import binar.academy.challenge_chapter5.model.UserResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Call

class AuthenticationViewModelTest{
    lateinit var service : UserEndPoint

    @Before
    fun setUp(){
        service = mockk()
    }

    @Test
    fun getUsersTest() : Unit = runBlocking {

        val responseUsers = mockk<Call<List<UserResponse>>>()

        every {
            runBlocking {
                service.getUsers()
            }
        } returns responseUsers

        val result = service.getUsers()

        verify {
            runBlocking { service.getUsers() }
        }
        assertEquals(result, responseUsers)
    }

    @Test
    fun getCurrentDetailUserTest(){
        val responseCurrentUser = mockk<Call<UserResponse>>()

        every {
            service.getCurrentUser("1")
        }returns  responseCurrentUser

        val result = service.getCurrentUser("1")

        verify {
            service.getCurrentUser("1")
        }
        assertEquals(result, responseCurrentUser)
    }

    @Test
    fun postDataTest(){
        val responseAddData = mockk<Call<UserResponse>>()

        every {
            service.register(UserResponse("", "user3", "use3@gmail.com", "123", "User 3 For testing", "2022-01-01","Test", ""))
        }returns responseAddData

        val result = service.register(UserResponse("", "user3", "use3@gmail.com", "123", "User 3 For testing", "2022-01-01","Test", ""))

        verify {
            service.register(UserResponse("", "user3", "use3@gmail.com", "123", "User 3 For testing", "2022-01-01","Test", ""))
        }
        assertEquals(result, responseAddData)
    }

}