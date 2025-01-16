package com.example.musicapp.domain.module

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Album (
    @DocumentId var id: String = "",
    var date: String = "",
    var genre: String = "",
    var image: String = "",
    var name: String = "",
    var time: String = "",
    var groupId: String = "",
    var groupName: String = "",
    var countMusic: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
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
        parcel.writeString(groupId)
        parcel.writeString(groupName)
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