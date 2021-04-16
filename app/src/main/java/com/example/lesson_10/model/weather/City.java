package com.example.lesson_10.model.weather;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("Key")
    private String key;

    @SerializedName("LocalizedName")
    private String localizedName;

    public City() {
    }

    public City(String key, String localizedName) {
        this.key = key;
        this.localizedName = localizedName;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public String getKey() {
        return key;
    }
}
