package com.example.simpletaskmanagerui.network

import com.example.simpletaskmanagerui.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String
)

interface TaskApiService {

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<String>

    @GET("/tasks")
    suspend fun getTasks(@Header("Authorization") token: String): Response<List<Task>>

    @POST("/tasks")
    suspend fun addTask(@Header("Authorization") token: String, @Body task: Task
    ): Response<Unit>

    @PUT("/tasks")
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Body task: Task
    ): Response<Unit>

    @DELETE("/tasks/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>
}
