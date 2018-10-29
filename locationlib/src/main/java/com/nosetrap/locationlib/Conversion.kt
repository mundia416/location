package com.nosetrap.locationlib

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat

/**
 * class used to convert from 1 unit of measurement to another
 * also converts from latlng to location
 * a
 *
 */
class Conversion {
    companion object {

        fun metersToMiles(meters: Float) : Float{
            return  meters/1609.34f
        }

        fun metersToKilometers(meters: Float) : Float{
            return meters/1000f
        }

        fun metersToFeet(meters: Float) : Float{
            return meters/0.3048f
        }

        fun metersToYards(meters: Float) : Float{
            return meters/0.9144f
        }

        fun milesToMeters(miles: Float) : Float{
            return  miles * 1609.34f
        }

        fun kilometersToMeters(kilometers: Float) : Float{
            return kilometers * 1000f
        }

        fun feetToMeters(feet: Float) : Float{
            return feet * 0.3048f
        }

        fun yardsToMeters(yards: Float) : Float{
            return yards * 0.9144f
        }

        /**
         * convert from latlng to Location object
         */
        fun latLngToLocation(latLng: LatLng): Location{
            val location = Location("")
            location.latitude = latLng.latitude
            location.longitude = latLng.longitude

            return location
        }


        /**
         * convert from location to latlng object
         */
        fun locationToLatLng(location: Location): LatLng{
           return LatLng(location.latitude,location.longitude)
        }

        /**
         *
         */
        fun metersPerSecondToKilometersPerHour(value: Float): Float{
            return value * 3.6f
        }

        /**
         *
         */
        fun metersPerSecondToMilesPerHour(value: Float): Float{
            return value * 2.237f
        }

        /**
         *
         */
        fun KilometersPerHourToMetersPerSecond(value: Float): Float{
            return value / 3.6f
        }

        /**
         *
         */
        fun KilometersPerHourToMilesPerHour(value: Float): Float{
            return value / 1.609f
        }

        /**
         *
         */
        fun milesPerHourToKilometersPerHour(value: Float): Float{
            return value * 1.609f

        }

        /**
         *
         */
        fun milesPerHourToMetersPerSecond(value: Float): Float{
            return value /  2.237f
        }
    }
}