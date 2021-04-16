package com.example.lesson_10.model;

public class WeatherParamCardModel {

    private final String valueDescription;
    private final String value;

    public WeatherParamCardModel(String valueDescription, String value) {
        this.valueDescription = valueDescription;
        this.value = value;
    }

    public String getValueDescription() {
        return valueDescription;
    }

    public String getValue() {
        return value;
    }
}
