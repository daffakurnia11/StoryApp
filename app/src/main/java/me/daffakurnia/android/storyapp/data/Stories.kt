package me.daffakurnia.android.storyapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stories(
    var id: String,
    var photoUrl: String?,
    var name: String?,
    var description: String?,
    var lat: String? = null,
    var lon: String? = null
) : Parcelable
