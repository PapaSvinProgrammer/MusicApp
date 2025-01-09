package com.example.musicapp.domain.module

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Music(
    @DocumentId val id: String? = null,
    val albumId: String? = null,
    val albumName: String? = null,
    val extraGroup: Map<String, Map<String, String>>? = null,
    val group: String? = null,
    val groupId: String? = null,
    val imageGroup: String? = null,
    val imageHigh: String? = null,
    val imageLow: String? = null,
    val movieUrl: String? = null,
    val name: String? = null,
    val time: String? = null,
    val url: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        HashMap<String, Map<String, String>>(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(albumId)
        parcel.writeString(albumName)
        parcel.writeString(group)
        parcel.writeString(groupId)
        parcel.writeString(imageGroup)
        parcel.writeString(imageHigh)
        parcel.writeString(imageLow)
        parcel.writeString(movieUrl)
        parcel.writeString(name)
        parcel.writeString(time)
        parcel.writeString(url)
        parcel.writeMap(extraGroup)
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