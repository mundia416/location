package com.nosetrap.locationlib.settings

import android.content.Context
import android.preference.PreferenceManager
import com.nosetrap.locationlib.R

/**
 * class used to get the default unit of measurement set in the settings
 */
class UnitOfSpeed(private val context: Context) {

    private val defaultPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    enum class Unit{
        MPH,
        KMH
    }

        /**
         * get the unit of measurement value which is selected in the settings
         */
        fun getUnitOfSpeed(): Unit {
            //
            val unit = defaultPrefs.getString(context.getString(R.string.key_unit_of_speed),context.getString(R.string.key_kmh))
            return when(unit){
                context.getString(R.string.key_kmh) -> Unit.KMH
                context.getString(R.string.key_mph) -> Unit.MPH
                else ->  Unit.KMH
        }
    }
}