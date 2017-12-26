package com.tejadroid.spiderview.map

import android.os.Bundle
import android.widget.LinearLayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.tejadroid.spiderview.base.BasePresenter
import com.tejadroid.spiderview.base.BaseView
import com.tejadroid.spiderview.model.User
import java.util.*

/**
 * Created by Tejas on 11/8/17.
 */
interface MapContract {


    interface View : BaseView {
        fun setUpUIControl()
        fun initializeMap()
        fun setPeopleList(users: MutableList<User>)
        fun onPeopleMarkerClick(userMarker: Marker)
        fun setHashMap(markerPeopleMap: HashMap<Marker, User>, userViewMap: HashMap<User,
                android.view.View>)

        fun setGoogleMapBound(bounds: LatLngBounds)

        fun displayLoading()

        fun dismissLoading()
    }


    interface Presenter : BasePresenter {
        fun showPeopleOnMapAndList(googleMap: GoogleMap, hsvLinearLayout: LinearLayout, peoples: MutableList<User>)
        fun getPeopleList(arguments: Bundle)
    }

}
