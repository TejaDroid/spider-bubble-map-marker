package com.tejadroid.spiderview.map

import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.tejadroid.spiderview.R
import com.tejadroid.spiderview.base.BaseFragment
import com.tejadroid.spiderview.extension.hide
import com.tejadroid.spiderview.extension.show
import com.tejadroid.spiderview.extension.toastName
import com.tejadroid.spiderview.model.User
import com.tejadroid.spiderview.utils.AvatarPin
import com.tejadroid.spiderview.widget.SpiderLineView
import java.util.*

/**
 * Created by Tejas on 11/20/17.
 */
class MapFragment : BaseFragment(), OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraIdleListener,
        MapContract.View, SpiderLineView.OnSpiderItemClickListener {

    var mainView: View? = null
    lateinit var loading: RelativeLayout
    lateinit var mPresenter: MapContract.Presenter

    private lateinit var googleMap: GoogleMap
    private lateinit var peoples: MutableList<User>
    lateinit var rlPinOptionView: SpiderLineView
    lateinit var fPeopleOnMapHsv: HorizontalScrollView
    lateinit var fPeopleOnMapHsvLl: LinearLayout
    private var markerPeopleMap = HashMap<Marker, User>()
    private var clientViewMap = HashMap<User, View>()
    private var position = -1


    companion object {

        val USER_LIST = "USER_LIST"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(peoples: ArrayList<User>): MapFragment {
            val fragment = MapFragment()
            val arg = Bundle()
            arg.putParcelableArrayList(USER_LIST, peoples)
            fragment.arguments = arg
            return fragment
        }
    }

    // Inflate the layout for this fragment
    override fun layoutResId(): Int {
        return R.layout.f_map
    }

    public override fun name(): String {
        return MapFragment::class.java.simpleName
    }

    override fun onBack() {
        activity.onBackPressed()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = MapPresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (mainView == null) {
            mainView = super.onCreateView(inflater, container, savedInstanceState)
            mPresenter.onCreateView()
        }
        return mainView
    }

    private var screenWidth: Int = 0

    override fun setUpUIControl() {
        loading = mainView!!.findViewById(R.id.pb_loading)
        rlPinOptionView = mainView!!.findViewById(R.id.pin_options)
        fPeopleOnMapHsv = mainView!!.findViewById(R.id.hsv_user)
        fPeopleOnMapHsvLl = mainView!!.findViewById(R.id.hsv_ll_user)

        AvatarPinOption.newInstance().setPinLayout(rlPinOptionView)
        AvatarPinOption.newInstance().setMapItemClickListener(this)

        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x

    }

    override fun initializeMap() {
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val fragmentManager: FragmentManager = childFragmentManager

        val fragment = fragmentManager
                .findFragmentById(R.id.map)
        var mapFragment: SupportMapFragment? = null
        if (fragment != null) {
            mapFragment = fragment as SupportMapFragment
        }

        if (mapFragment == null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            fragmentTransaction.replace(R.id.map, mapFragment).commit()
        }

        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap.uiSettings.isMapToolbarEnabled = false
        this.googleMap.setOnMapClickListener(this)
        this.googleMap.setOnMarkerClickListener(this)
        this.googleMap.setOnCameraIdleListener(this)
        mPresenter.getPeopleList(arguments)
    }

    override fun setPeopleList(users: MutableList<User>) {
        this.googleMap.clear()
        peoples = users
        if (peoples.isNotEmpty()) {
            AvatarPin.newInstance().setIsdestroyed(false)
            mPresenter.showPeopleOnMapAndList(this.googleMap, fPeopleOnMapHsvLl, peoples)
        }
    }

    override fun onMapClick(latLng: LatLng?) {
        AvatarPinOption.newInstance().dismissAvatarPinOption()
    }

    private var isMarkerClick: Boolean = false;
    private var clickedPeople = User()

    override fun onMarkerClick(marker: Marker): Boolean {
        isMarkerClick = true
        AvatarPinOption.newInstance().dismissAvatarPinOption()
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.position, 12.0f))
        clickedPeople = markerPeopleMap.get(marker)!!
        AvatarPinOption.newInstance().setUserObject(clickedPeople)
        activity.toastName(clickedPeople.Name)
        rlPinOptionView.tag = marker.snippet
        val clientView = clientViewMap[clickedPeople]
        val scrollX = clientView!!.left - screenWidth / 2 + clientView.width / 2
        fPeopleOnMapHsv.smoothScrollTo(scrollX, 0)
        return false
    }


    override fun onCameraIdle() {
        if (isMarkerClick) {
            isMarkerClick = false
            val projection = this.googleMap.projection
            val screenPosition = projection.toScreenLocation(googleMap.cameraPosition.target)
            AvatarPinOption.newInstance().showAvatarPinOption(screenPosition)
        } else {
            AvatarPinOption.newInstance().dismissAvatarPinOption()
        }
    }


    override fun onSpiderItemClick(clickPosition: Int) {

    }


    override fun setHashMap(markerPeopleMap: HashMap<Marker, User>,
                            userViewMap: HashMap<User, View>) {
        this.markerPeopleMap = markerPeopleMap
        this.clientViewMap = userViewMap
    }

    override fun setGoogleMapBound(bounds: LatLngBounds) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
    }

    override fun onPeopleMarkerClick(userMarker: Marker) {
        onMarkerClick(userMarker)
    }

    override fun displayLoading() {
        if (!loading.isShown) {
            loading.show()
        }
    }

    override fun dismissLoading() {
        if (loading.isShown) {
            loading.hide()
        }
    }

    override fun onDestroyView() {
        AvatarPin.newInstance().setIsdestroyed(true)
        AvatarPinOption.newInstance().dismissAvatarPinOption()
        super.onDestroyView()
    }
}