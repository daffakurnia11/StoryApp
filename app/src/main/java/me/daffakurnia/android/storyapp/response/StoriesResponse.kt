package me.daffakurnia.android.storyapp.response

import com.google.gson.annotations.SerializedName

data class StoriesResponse(

    @field:SerializedName("listStory")
    val listStory: List<StoriesResponseItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

