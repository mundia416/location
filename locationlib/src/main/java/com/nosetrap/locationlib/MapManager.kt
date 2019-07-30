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

    var defaultMapZoom = DEFAULT_MAP_ZOOM


    private val defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val locationManager = LocationManager(context)

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

                    map?.uiSettings?.setAllGesturesEnabled(true)
                    map?.uiSettings?.isCompassEnabled = false
                    map?.uiSettings?.isMyLocationButtonEnabled = false
                    map?.uiSettings?.isZoomControlsEnabled = false
                    map?.isMyLocationEnabled = true
                    map?.uiSettings?.isMapToolbarEnabled = false

                setMapStyle()
            }

    fun setMapStyle(mapStyle: MapStyle){
        val key = when(mapStyle){
            MapStyle.DEFAULT -> R.string.key_default
            MapStyle.SATELITE -> R.string.key_satellite
            MapStyle.STYLE_3D -> R.string.key_3d
            MapStyle.SUBTLE_GRAYSCALE -> R.string.key_subtle_grayscale
            MapStyle.ULTRA_LIGHT -> R.string.key_ultra_light
            MapStyle.SHUTTER_GREEN ->  R.string.key_sutter_green
            MapStyle.BAYSIDE -> R.string.key_bayside
            MapStyle.GLEESON -> R.string.key_gleeson
            MapStyle.SUPER_SIMPLE ->  R.string.key_super_simple
            MapStyle.CRAZY -> R.string.key_crazy
            MapStyle.SHADES_OF_GRAY -> R.string.key_shades_of_gray
            MapStyle.CARO -> R.string.key_caro
        }

        defaultPrefs.edit().putString(context.getString(R.string.key_map_style),context.getString(key)).commit()
        setMapStyle()

    }

    /**
     *
     */
    fun getMapStyle(): MapStyle{
        val chosenStyle = defaultPrefs.getString(context.getString(R.string.key_map_style), context.getString(R.string.key_default))

        //@WARNING dont change any of these strings
       return when (chosenStyle) {
            context.getString(R.string.key_shades_of_gray) -> MapStyle.SHADES_OF_GRAY
            context.getString(R.string.key_caro) -> MapStyle.CARO
            context.getString(R.string.key_subtle_grayscale) -> MapStyle.SUBTLE_GRAYSCALE
            context.getString(R.string.key_ultra_light) -> MapStyle.ULTRA_LIGHT
            context.getString(R.string.key_sutter_green) -> MapStyle.SHUTTER_GREEN
            context.getString(R.string.key_bayside) -> MapStyle.BAYSIDE
            context.getString(R.string.key_gleeson) -> MapStyle.GLEESON
            context.getString(R.string.key_super_simple) -> MapStyle.SUPER_SIMPLE
            context.getString(R.string.key_crazy) -> MapStyle.CRAZY
            context.getString(R.string.key_default) -> MapStyle.DEFAULT
           else -> MapStyle.DEFAULT
        }
    }


    /**
     *
     */
    private fun setMapStyle() {
        //set the mapstyle depending on the style chosen from the settings
        try {
            var jsonResource = 0

            val chosenStyle = defaultPrefs.getString(context.getString(R.string.key_map_style), context.getString(R.string.key_default))

            //@WARNING dont change any of these strings
            when (chosenStyle) {
                context.getString(R.string.key_shades_of_gray) -> jsonResource = R.raw.shades_of_gray
                context.getString(R.string.key_caro) -> jsonResource = R.raw.caro
                context.getString(R.string.key_subtle_grayscale) -> jsonResource = R.raw.subtle_gray_scale
                context.getString(R.string.key_ultra_light) -> jsonResource = R.raw.ultra_light
                context.getString(R.string.key_sutter_green) -> jsonResource = R.raw.sutter_green
                context.getString(R.string.key_bayside) -> jsonResource = R.raw.bayside
                context.getString(R.string.key_gleeson) -> jsonResource = R.raw.gleeson
                context.getString(R.string.key_super_simple) -> jsonResource = R.raw.super_simple
                context.getString(R.string.key_crazy) -> jsonResource = R.raw.crazy
                context.getString(R.string.key_default) -> jsonResource = R.raw.default_style
            }

            //style
            if (chosenStyle != context.getString(R.string.key_satellite) && chosenStyle != context.getString(R.string.key_3d)) {
                val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, jsonResource)
                    map?.setMapStyle(mapStyleOptions)
                    map?.mapType = GoogleMap.MAP_TYPE_NORMAL
            }

            //satellite
            if (chosenStyle == context.getString(R.string.key_satellite)) {
                 map?.mapType = GoogleMap.MAP_TYPE_HYBRID
            }

            //3d
            if (chosenStyle == context.getString(R.string.key_3d)) {
                    map?.mapType = GoogleMap.MAP_TYPE_NORMAL
                    map?.isBuildingsEnabled = true
            } else {
                 map?.isBuildingsEnabled = false
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
                .zoom(defaultMapZoom)
                .bearing(0f)

        val cameraPosition = builder.build()

        val cameraUpdate =CameraUpdateFactory.newCameraPosition(cameraPosition)

         map?.moveCamera(cameraUpdate)
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
                .zoom(defaultMapZoom)
                .bearing(0f)

        val cameraPosition = builder.build()

        val cameraUpdate =CameraUpdateFactory.newCameraPosition(cameraPosition)

         map?.animateCamera(cameraUpdate)
    }


}