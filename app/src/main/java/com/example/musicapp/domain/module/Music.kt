package com.example.musicapp.domain.module

import android.os.Parcel
import android.os.Parcelable

data class Music(
    var id: String = "",
    var group: String = "",
    var name: String = "",
    var album: String = "",
    var url: String = "",
    var imageLow: String = "",
    var imageHigh: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(group)
        parcel.writeString(name)
        parcel.writeString(album)
        parcel.writeString(url)
        parcel.writeString(imageLow)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Music> {
        override fun createFromParcel(parcel: Parcel): Music {
            return Music(parcel)
        }

        override fun newArray(size: Int): Array<Music?> {
            return arrayOfNulls(size)
        }
    }

}