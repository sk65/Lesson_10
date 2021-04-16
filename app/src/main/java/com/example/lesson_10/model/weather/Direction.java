package com.example.lesson_10.model.weather;

import com.google.gson.annotations.SerializedName;

public class Direction {
    @SerializedName("English")
    private String value;

    public String getValue() {
        return value;
    }
}
