package com.metrosoft.arafat.salebook.holder;

/**
 * Created by hp on 10/15/2017.
 */

public class GPSData {
    private static double latitude;
    /**
     * @return the latitude
     */
    public static double getLatitude() {
        return latitude;
    }


    public static void setLatitude(double l) {
        latitude = l;
    }

    private static double longitude;
    /**
     * @return the longitude
     */
    public static double getLongitude() {
        return longitude;
    }

    /**
     * @param d the longitude to set
     */
    public static void setLongitude(double d) {
        longitude = d;
    }
}