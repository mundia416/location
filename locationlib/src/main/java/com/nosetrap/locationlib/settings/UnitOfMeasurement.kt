package com.nosetrap.locationlib.settings

import android.content.Context
import android.preference.PreferenceManager
import com.nosetrap.locationlib.R

/**
 * class used to get the default_style unit of measurement set in the settings
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
            val unit = defaultPrefs.getString(context.getString(R.string.key_unit_of_measurement),context.getString(R.string.key_kilometers))
            return when(unit){
                context.getString(R.string.key_meters) -> Unit.METERS
                context.getString(R.string.key_miles) -> Unit.MILES
                context.getString(R.string.key_kilometers)-> Unit.KILOMETERS
                context.getString(R.string.key_feet) -> Unit.FEET
                context.getString(R.string.key_yards) -> Unit.YARDS
                else -> Unit.KILOMETERS
        }
    }
}