package com.nosetrap.locationlib


import android.content.Context
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast

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
}