package com.example.lesson_10.model.weather;

import com.google.gson.annotations.SerializedName;

public class Day {
    @SerializedName("Icon")
    private int icon;

    @SerializedName("IconPhrase")
    private String iconPhrase;

    @SerializedName("Wind")
    public Wind wind;

    @SerializedName("RainProbability")
    public int rainProbability;

    public int getIcon() {
        return icon;
    }

    public String getIconPhrase() {
        return iconPhrase;
    }

    public int getRainProbability() {
        return rainProbability;
    }

    public Wind getWind() {
        return wind;
    }
}
