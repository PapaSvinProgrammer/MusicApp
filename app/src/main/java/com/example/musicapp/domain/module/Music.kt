package com.example.musicapp.domain.module

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Music(
    @DocumentId var id: String? = null,
    var albumId: String? = null,
    var albumName: String? = null,
    var extraGroup: Map<String, Map<String, String>>? = null,
    var group: String? = null,
    var groupId: String? = null,
    var imageGroup: String? = null,
    var imageHigh: String? = null,
    var imageLow: String? = null,
    var movieUrl: String? = null,
    var name: String? = null,
    var time: String? = null,
    var url: String? = null
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