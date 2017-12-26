package com.tejadroid.spiderview.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Tejas on 12/18/17.
 */
class Direction() : Parcelable {
    var Name: String = ""
    var Logo: String = ""
    var Address: String = ""
    var Latitude: Double = 0.0
    var Longitude: Double = 0.0
    var PinColor = "#e62a26"
    var IsClient = false

    constructor(parcel: Parcel) : this() {
        Name = parcel.readString()
        Logo = parcel.readString()
        Address = parcel.readString()
        Latitude = parcel.readDouble()
        Longitude = parcel.readDouble()
        PinColor = parcel.readString()
        IsClient = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Name)
        parcel.writeString(Logo)
        parcel.writeString(Address)
        parcel.writeDouble(Latitude)
        parcel.writeDouble(Longitude)
        parcel.writeString(PinColor)
        parcel.writeByte(if (IsClient) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Direction> {
        override fun createFromParcel(parcel: Parcel): Direction {
            return Direction(parcel)
        }

        override fun newArray(size: Int): Array<Direction?> {
            return arrayOfNulls(size)
        }
    }

}