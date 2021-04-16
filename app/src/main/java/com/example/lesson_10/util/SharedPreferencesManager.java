package com.example.lesson_10.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.example.lesson_10.util.Constants.CITY_CODE_KEY;
import static com.example.lesson_10.util.Constants.LOCALISED_NAME_KEY;
import static com.example.lesson_10.util.Constants.LOCATION_COORDINATES_KEY;
import static com.example.lesson_10.util.Constants.PREF_KEY;

public class SharedPreferencesManager {


    private final SharedPreferences sharedPreferences;
    private static SharedPreferencesManager instance;
    private final SharedPreferences.Editor editor;


    @SuppressLint("CommitPrefEdits")
    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
    }

    public static SharedPreferencesManager getInstance() {
        return instance;
    }

    public void putCityCode(String cityCode) {
        editor.putString(CITY_CODE_KEY, cityCode).apply();
    }

    public String getCityCode() {
        return sharedPreferences.getString(CITY_CODE_KEY, null);
    }

    public void putLocalizedName(String localisedName) {
        editor.putString(LOCALISED_NAME_KEY, localisedName).apply();
    }

    public String getLocalizedName() {
        return sharedPreferences.getString(LOCALISED_NAME_KEY, null);
    }

    public String getLocationCoordinates() {
        return sharedPreferences.getString(LOCATION_COORDINATES_KEY, null);
    }

    public void putLocationCoordinates(String locationCoordinates) {
        editor.putString(LOCATION_COORDINATES_KEY, locationCoordinates).apply();
    }
}
