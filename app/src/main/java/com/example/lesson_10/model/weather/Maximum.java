package com.example.lesson_10.model.weather;

import com.google.gson.annotations.SerializedName;

public class Maximum {
    @SerializedName("Value")
    private double value;

    public Maximum() {
    }

    public Maximum(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}