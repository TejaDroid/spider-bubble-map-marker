package com.tejadroid.spiderview.map

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.tejadroid.spiderview.R
import com.tejadroid.spiderview.extension.loadUrlWithCircleImage
import com.tejadroid.spiderview.model.User
import com.tejadroid.spiderview.utils.ActivityUtils
import com.tejadroid.spiderview.utils.AvatarPin
import java.util.*


/**
 * Created by Tejas on 11/8/17.
 */
class MapPresenter(private val mView: MapContract.View) : MapContract.Presenter {

    override fun onCreateView() {
        this.mView.setUpUIControl()
        this.mView.initializeMap()
    }

    override fun onDestroyView() {

    }


    private val markerPeopleMap = HashMap<Marker, User>()
    private val clientViewMap = HashMap<User, View>()

    override fun getPeopleList(arguments: Bundle) {
        if (arguments.containsKey(MapFragment.USER_LIST)) {
            var userList: MutableList<User> = arguments.getParcelableArrayList(MapFragment.USER_LIST)
            mView.setPeopleList(userList)
        }
    }

    private var peopleSize = 0
    private var count = 0
    private var builder = LatLngBounds.Builder()
    override fun showPeopleOnMapAndList(googleMap: GoogleMap, hsvLinearLayout: LinearLayout, peoples: MutableList<User>) {
        mView.displayLoading()
        markerPeopleMap.clear()
        clientViewMap.clear()
        hsvLinearLayout.removeAllViews()
        builder = LatLngBounds.Builder()
        count = 0
        peopleSize = peoples.size

        for (people in peoples) {

            val userLatLng = LatLng(people.Latitude, people.Longitude)
            builder.include(userLatLng)
//
            val rowView = (hsvLinearLayout.context as Activity).layoutInflater.inflate(R.layout.r_map_hsv_people_item, null)
            val ivProfile = rowView.findViewById<ImageView>(R.id.iv_avatar)

            val drawable = ivProfile.background.mutate()
            drawable.setColorFilter(Color.parseColor(people.PinColor),
                    PorterDuff.Mode.SRC_ATOP)
            ivProfile.background = drawable
            ivProfile.setPadding(ActivityUtils.dpToPx(ivProfile.context, 2), ActivityUtils.dpToPx(ivProfile.context, 2),
                    ActivityUtils.dpToPx(ivProfile.context, 2), ActivityUtils.dpToPx(ivProfile.context, 2))
            ivProfile.loadUrlWithCircleImage(people.ProfilePic)

            val tvName = rowView.findViewById<TextView>(R.id.tv_top_user_name)
            if (people.Name.contains(" ")) {
                tvName.text = people.Name.substring(0, people.Name.indexOf(" "))
            } else {
                tvName.text = people.Name
            }
            hsvLinearLayout.addView(rowView)
            showPeoplePin(googleMap, userLatLng, people, rowView)
        }
    }

    private fun showPeoplePin(googleMap: GoogleMap, latLng: LatLng, people: User,
                              clientBottomView: View) {

        AvatarPin.newInstance().getAvatarPinImage(mView.getActivity(), people.ProfilePic,
                Color.parseColor(people.PinColor),
                object : AvatarPin.PinListener {
                    override fun onReadyPin(bitmap: Bitmap) {
                        val clientMarker: Marker = googleMap.addMarker(MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        )
                        clientBottomView.setOnClickListener {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            mView.onPeopleMarkerClick(clientMarker)
                        }
                        markerPeopleMap.put(clientMarker, people)
                        clientViewMap.put(people, clientBottomView)
                        count++
                        if (count == peopleSize) {
                            mView.setHashMap(markerPeopleMap, clientViewMap)
                            mView.setGoogleMapBound(builder.build())
                            mView.dismissLoading()
                        }
                    }
                })
    }

}
