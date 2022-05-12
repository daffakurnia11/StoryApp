package me.daffakurnia.android.storyapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Stories(
    var photoUrl: String?,
    var name: String?,
    var description: String?,
) : Parcelable
