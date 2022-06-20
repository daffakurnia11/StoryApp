package me.daffakurnia.android.storyapp.api

import me.daffakurnia.android.storyapp.response.AddStoryResponse
import me.daffakurnia.android.storyapp.response.LoginResponse
import me.daffakurnia.android.storyapp.response.RegisterResponse
import me.daffakurnia.android.storyapp.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @HeaderMap token: Map<String, String>
    ): Call<StoriesResponse>

    @GET("stories")
    fun getMapStories(
        @HeaderMap token: Map<String, String>,
        @Query("location") location: Int = 1
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @HeaderMap token: Map<String, String>
    ): Call<AddStoryResponse>
}