package com.example.lesson_10.model.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    @SerializedName("DailyForecasts")
    private List<DailyForecast> dailyForecasts;

    public List<DailyForecast> getDailyForecasts() {
        return dailyForecasts;
    }
}
