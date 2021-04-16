package com.example.lesson_10.util;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Constants {
    public static final String RESPONSE_CODE_KEY = "Code";
    //location permissions
    public static final String[] LOCATION_PERMISSIONS = new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    public static final int LOCATION_SETTINGS_REQUEST_CODE = 52456;
    public static final int REQUEST_LOCATION_PERMISSIONS_REQUEST_CODE = 34;
    //location update
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    //shared preferences
    public static final String PACKAGE_NAME = "com.example.lesson8";
    public static final String PREF_KEY = PACKAGE_NAME + ".appSetting";

    public static final String CITY_CODE_KEY = PACKAGE_NAME + ".cityKey";
    public static final String LOCALISED_NAME_KEY = PACKAGE_NAME + ".localizedNameKey";
    public static final String LOCATION_COORDINATES_KEY = PACKAGE_NAME + ".locationCoordinatesKey";

}
