package com.example.lesson_10.model.weather;

import com.google.gson.annotations.SerializedName;

public class Ceiling {
    @SerializedName("Value")
    private int value;

    public int getValue() {
        return value;
    }
}
