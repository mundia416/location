package com.nosetrap.locationlib

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import androidx.annotation.RequiresPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions


class MapManager(private val context: Context) {
    companion object {
        const val DEFAULT_MAP_ZOOM = 15f
        private val MAX_TILT = 55f
    }

    /**
     * the google map object, this should be set on onMapConnected()
     */
    var map: GoogleMap? = null

    private val defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val locationManager = LocationManager(context)

    /**
     * zoom the map to the users location with an animated camera
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun zoomToMyLocation(tilt:Boolean = true){
           locationManager.getLastKnownLocationAsync(object : LocationManager.LocationListener{
               override fun lastKnownLocation(latLng: LatLng?) {
                   try {
                   zoomToLocation(latLng!!, tilt)
                   }catch (e:Exception){}
               }
           })



    }


            /**
     * initialises the map object to use default map settings.
     */
     @SuppressLint("MissingPermission")
     fun initDefault() {
                forceZoomToMyLocation(false)
                zoomToMyLocation(true)
                map?.uiSettings?.setAllGesturesEnabled(true)
                map?.uiSettings?.isCompassEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                map?.uiSettings?.isZoomControlsEnabled = false
                map?.isMyLocationEnabled = true
                map?.isBuildingsEnabled = false
                map?.uiSettings?.isMapToolbarEnabled = false

                setMapStyle()
            }

    private fun setMapStyle(){
        //set the mapstyle depending on the style chosen from the settings
        try {
            var jsonResource = 0

            val chosenStyle = defaultPrefs.getString(context.getString(R.string.key_map_style), context.getString(R.string.key_default))

            //@WARNING dont change any of these strings
            when (chosenStyle) {
                context.getString(R.string.key_shades_of_gray)-> jsonResource = R.raw.shades_of_gray
                context.getString(R.string.key_caro) -> jsonResource = R.raw.caro
                context.getString(R.string.key_subtle_grayscale) -> jsonResource = R.raw.subtle_gray_scale
                context.getString(R.string.key_ultra_light) -> jsonResource = R.raw.ultra_light
                context.getString(R.string.key_sutter_green) -> jsonResource = R.raw.sutter_green
                context.getString(R.string.key_bayside) -> jsonResource = R.raw.bayside
                context.getString(R.string.key_gleeson) -> jsonResource = R.raw.gleeson
                context.getString(R.string.key_super_simple) -> jsonResource = R.raw.super_simple
                context.getString(R.string.key_crazy) -> jsonResource = R.raw.crazy
            }

            if (chosenStyle != context.getString(R.string.key_default)) {
                map?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, jsonResource))
            }
            //if the chosen style is satellite
            if(chosenStyle == context.getString(R.string.key_satellite)){
                map?.mapType = GoogleMap.MAP_TYPE_HYBRID
            }else{
                map?.mapType = GoogleMap.MAP_TYPE_NORMAL
            }

            if(chosenStyle == context.getString(R.string.key_3d)){
                map?.isBuildingsEnabled = true
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    /**
     * zoom to my location without animating
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun forceZoomToMyLocation(tilt:Boolean = true){
        locationManager.getLastKnownLocationAsync(object : LocationManager.LocationListener{
            override fun lastKnownLocation(latLng: LatLng?) {
                try {
                    forceZoomToLocation(latLng!!, tilt)
                }catch (e:Exception){}
            }
        })
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

        map?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
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

        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }


}