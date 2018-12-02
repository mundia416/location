package com.nosetrap.locationlib

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.preference.PreferenceManager
import androidx.annotation.RequiresPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions


class MapManager(private val activity: Activity) {
    companion object {
        const val DEFAULT_MAP_ZOOM = 15f
        private val MAX_TILT = 55f
    }

    /**
     * the google map object, this should be set on onMapConnected()
     */
    var map: GoogleMap? = null

    private val defaultPrefs = PreferenceManager.getDefaultSharedPreferences(activity)
    private val locationManager = LocationManager(activity)

    /**
     * zoom the map to the users location with an animated camera
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun zoomToMyLocation(tilt:Boolean = true){
         val latLng = locationManager.getLastKnownLocation()

        try {
            zoomToLocation(latLng!!, tilt)
        }catch (e:Exception){}
    }


            /**
     * initialises the map object to use default_style map settings.
     */
     @SuppressLint("MissingPermission")
     fun initDefault() {
                forceZoomToMyLocation(false)
                zoomToMyLocation(true)

                activity.runOnUiThread {
                    map?.uiSettings?.setAllGesturesEnabled(true)
                    map?.uiSettings?.isCompassEnabled = false
                    map?.uiSettings?.isMyLocationButtonEnabled = false
                    map?.uiSettings?.isZoomControlsEnabled = false
                    map?.isMyLocationEnabled = true
                    map?.uiSettings?.isMapToolbarEnabled = false
                }

                setMapStyle()
            }

    /**
     *
     */
    private fun setMapStyle() {
        //set the mapstyle depending on the style chosen from the settings
        try {
            var jsonResource = 0

            val chosenStyle = defaultPrefs.getString(activity.getString(R.string.key_map_style), activity.getString(R.string.key_default))

            //@WARNING dont change any of these strings
            when (chosenStyle) {
                activity.getString(R.string.key_shades_of_gray) -> jsonResource = R.raw.shades_of_gray
                activity.getString(R.string.key_caro) -> jsonResource = R.raw.caro
                activity.getString(R.string.key_subtle_grayscale) -> jsonResource = R.raw.subtle_gray_scale
                activity.getString(R.string.key_ultra_light) -> jsonResource = R.raw.ultra_light
                activity.getString(R.string.key_sutter_green) -> jsonResource = R.raw.sutter_green
                activity.getString(R.string.key_bayside) -> jsonResource = R.raw.bayside
                activity.getString(R.string.key_gleeson) -> jsonResource = R.raw.gleeson
                activity.getString(R.string.key_super_simple) -> jsonResource = R.raw.super_simple
                activity.getString(R.string.key_crazy) -> jsonResource = R.raw.crazy
                activity.getString(R.string.key_default) -> jsonResource = R.raw.default_style
            }

            //style
            if (chosenStyle != activity.getString(R.string.key_satellite) && chosenStyle != activity.getString(R.string.key_3d)) {
                val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(activity, jsonResource)
                activity.runOnUiThread {
                    map?.setMapStyle(mapStyleOptions)
                    map?.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
            }

            //satellite
            if (chosenStyle == activity.getString(R.string.key_satellite)) {
                activity.runOnUiThread { map?.mapType = GoogleMap.MAP_TYPE_SATELLITE }
            }

            //3d
            if (chosenStyle == activity.getString(R.string.key_3d)) {
                activity.runOnUiThread {
                    map?.mapType = GoogleMap.MAP_TYPE_NORMAL
                    map?.isBuildingsEnabled = true
                }
            } else {
                activity.runOnUiThread { map?.isBuildingsEnabled = false }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * zoom to my location without animating
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun forceZoomToMyLocation(tilt:Boolean = true){
        val latlng = locationManager.getLastKnownLocation()
                try {
                    forceZoomToLocation(latlng!!, tilt)
                }catch (e:Exception){}
    }

    /**
     * zoom to a location without animating
     */
    fun forceZoomToLocation(location: LatLng,tilt:Boolean = true) {
        val builder = CameraPosition.Builder()
        if(tilt) {
            builder.tilt(MAX_TILT)
        }

        builder.target(location)
                .zoom(DEFAULT_MAP_ZOOM)
                .bearing(0f)

        val cameraPosition = builder.build()

        val cameraUpdate =CameraUpdateFactory.newCameraPosition(cameraPosition)

        activity.runOnUiThread { map?.moveCamera(cameraUpdate) }
    }

    /**
     *     * zoom to a location with animation
     */
    fun zoomToLocation(location: LatLng,tilt:Boolean = true) {
        val builder = CameraPosition.Builder()
        if(tilt) {
            builder.tilt(MAX_TILT)
        }

        builder.target(location)
                .zoom(DEFAULT_MAP_ZOOM)
                .bearing(0f)

        val cameraPosition = builder.build()

        val cameraUpdate =CameraUpdateFactory.newCameraPosition(cameraPosition)

        activity.runOnUiThread { map?.animateCamera(cameraUpdate) }
    }


}