package com.nosetrap.locationlib


import android.Manifest
import android.content.Context
import android.location.Criteria
import android.location.Geocoder
import android.location.LocationManager
import android.os.Handler
import android.provider.Settings
import androidx.annotation.RequiresPermission
import com.google.android.gms.maps.model.LatLng

/**
 */
class LocationManager(private val context: Context) {
    companion object {
        //the different types of location modes
        const val LOCATION_MODE_BATTERY_SAVING = Settings.Secure.LOCATION_MODE_BATTERY_SAVING
        const val LOCATION_MODE_HIGH_ACCURACY = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY
        const val LOCATION_MODE_OFF = Settings.Secure.LOCATION_MODE_OFF
        const val LOCATION_MODE_SENSORS_ONLY = Settings.Secure.LOCATION_MODE_SENSORS_ONLY
    }

    private val androidLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager


    /**
     * detect whether location has been enabled
     * @return
     */
    fun isLocationEnabled(): Boolean {
        var isEnabled = false
        try {
            if (androidLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    androidLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                isEnabled = true
            }
        } catch (e: Exception) {
        }

        return isEnabled
    }

    /**
     * returns the location mode that the device is currently in
     * @return
     */
    fun getLocationMode(): Int {
        return try {
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            LOCATION_MODE_OFF
        }

    }

        /**
     * get the name of a location
     * this is done in a background thread
     */
    fun getLocationName(latLng: LatLng): String? {
        return try {
            val geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

            var locationName = addresses[0].subLocality

            if (locationName == null) {
                locationName = addresses[0].locality
            } else {
                if (locationName.isEmpty()) {
                    locationName = addresses[0].locality
                }
            }

            locationName
        } catch (e: Exception) {
            null
        }
    }

    /**
     * get the users last known location
     * this is executed on the main thread
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getLastKnownLocation(): LatLng? {
        return try {
            val criteria = Criteria()
            criteria.accuracy = Criteria.ACCURACY_FINE
            var location = androidLocationManager.getLastKnownLocation(androidLocationManager
                    .getBestProvider(criteria, true))

            if (location == null) {
                location = androidLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }

            LatLng(location.latitude, location.longitude)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * listener interface used when getting the last known location
     */
    interface LocationListener{
        fun lastKnownLocation(latLng: LatLng?)
    }

    /**
     * listener interface used when getting the name of a location
     */
    interface LocationNameListener{
        fun locationName(name: String?)
    }
}