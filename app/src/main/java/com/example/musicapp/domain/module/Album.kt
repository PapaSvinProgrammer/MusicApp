package com.example.musicapp.domain.module

import android.os.Parcel
import android.os.Parcelable

data class Album (
    val id: String = "",
    val date: String = "",
    val genre: String = "",
    val image: String = "",
    val name: String = "",
    val time: String = "",
    val countMusic: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(date)
        parcel.writeString(image)
        parcel.writeString(time)
        parcel.writeString(genre)
        parcel.writeInt(countMusic)
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }
}