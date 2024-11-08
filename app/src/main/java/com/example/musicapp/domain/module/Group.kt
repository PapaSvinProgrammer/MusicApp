package com.example.musicapp.domain.module

import android.os.Parcel
import android.os.Parcelable

data class Group (
    var name: String?,
    var albums: ArrayList<String>?,
    var compound: ArrayList<String>?,
    var genre: String?,
    var country: String?,
    var musics: ArrayList<String>?,
    var year: String?,
    var image: String?,
    var isFavorite: Boolean? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(country)
        parcel.writeString(year)
        parcel.writeString(image)
        parcel.writeValue(isFavorite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Group> {
        override fun createFromParcel(parcel: Parcel): Group {
            return Group(parcel)
        }

        override fun newArray(size: Int): Array<Group?> {
            return arrayOfNulls(size)
        }
    }
}