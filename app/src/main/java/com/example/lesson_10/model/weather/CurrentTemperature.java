package com.example.lesson_10.model.weather;

import com.google.gson.annotations.SerializedName;

public class CurrentTemperature {
    @SerializedName("Value")
    private double value;

    public double getValue() {
        return value;
    }
}
