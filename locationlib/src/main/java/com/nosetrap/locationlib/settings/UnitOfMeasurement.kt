package com.nosetrap.locationlib.settings

import android.content.Context
import android.preference.PreferenceManager
import com.nosetrap.locationlib.R

/**
 * class used to get the default unit of measurement set in the settings
 */
class UnitOfMeasurement(private val context: Context) {

    private val defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    enum class Unit{
        METERS,
        MILES,
        KILOMETERS,
        FEET,
        YARDS
    }

        /**
         * get the unit of measurement value which is selected in the settings
         */
        fun getUnitOfMeasurement(): Unit {
            //
            val unit = defaultPrefs.getString(context.getString(R.string.key_unit_of_measurement),"meters")
            return when(unit){
                "meters" -> Unit.METERS
                "miles" -> Unit.MILES
                "kilometers" -> Unit.KILOMETERS
                "feet" -> Unit.FEET
                "yards" -> Unit.YARDS
                else -> Unit.METERS
        }
    }
}