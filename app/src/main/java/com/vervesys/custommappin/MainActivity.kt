package com.vervesys.custommappin

import android.os.Bundle
import com.google.gson.Gson
import com.tejadroid.spiderview.map.MapFragment
import com.vervesys.custommappin.base.BaseActivity
import com.vervesys.custommappin.model.UserListResponse

class MainActivity : BaseActivity() {

    override fun layoutResId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jsonString = "{\"Status\":\"Success\",\"Message\":\"Successfully.\",\"Data\":[{\"Id\":69,\"Name\":\"Tejas Chauhan\",\"EmailId\":\"tejas@gmail.local\",\"PhoneNumber\":\"9898989898\",\"Address\":\"New York\",\"CountryName\":\"USA\",\"ProfilePic\":\"http://180.211.107.52:8081/Images/Employee/Employee69.jpg\",\"PinColor\":\"#662d91\",\"Latitude\":40.712775,\"Longitude\":-74.005973}]}"
        val userListResponse = Gson().fromJson<UserListResponse>(jsonString, UserListResponse::class.java)

        val fragment = MapFragment.newInstance(ArrayList(userListResponse.Data))
        showFragment(R.id.fragment_container, fragment)

    }

}
