package com.tejadroid.spiderview.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Tejas on 12/18/17.
 */
class User() : Parcelable {

    var Id = 0
    var Name = ""
    var EmailId = ""
    var PhoneNumber = ""
    var Address = ""
    var ProfilePic = ""
    var Latitude = 0.0
    var Longitude = 0.0
    var Distance = 0.0
    var PinColor = "#e62a26"

    constructor(parcel: Parcel) : this() {
        Id = parcel.readInt()
        Name = parcel.readString()
        ProfilePic = parcel.readString()
        EmailId = parcel.readString()
        PhoneNumber = parcel.readString()
        Address = parcel.readString()
        Latitude = parcel.readDouble()
        Longitude = parcel.readDouble()
        Distance = parcel.readDouble()
        PinColor = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(Id)
        parcel.writeString(Name)
        parcel.writeString(ProfilePic)
        parcel.writeString(EmailId)
        parcel.writeString(PhoneNumber)
        parcel.writeString(Address)
        parcel.writeDouble(Latitude)
        parcel.writeDouble(Longitude)
        parcel.writeDouble(Distance)
        parcel.writeString(PinColor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


    fun getDirection(): Direction {
        val direction = Direction()
        direction.Name = Name
        direction.Address = Address
        direction.Latitude = Latitude
        direction.Longitude = Longitude
        direction.Logo = ProfilePic
        direction.PinColor = PinColor
        return direction
    }
}