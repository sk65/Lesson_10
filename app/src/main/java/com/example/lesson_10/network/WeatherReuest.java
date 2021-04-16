package com.example.lesson_10.network;


import com.example.lesson_10.model.weather.City;
import com.example.lesson_10.model.weather.HourlyForecast;
import com.example.lesson_10.model.weather.Weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherReuest {
    @GET("locations/v1/cities/geoposition/search")
    Call<City> getCity(@Query("q") String location);

    @GET("forecasts/v1/daily/5day/{cityKod}")
    Call<Weather> getDailyForecasts(@Path("cityKod") String cityKod,
                                    @Query("details") boolean details,
                                    @Query("metric") boolean metric);

    @GET("forecasts/v1/hourly/12hour/{cityKod}")
    Call<List<HourlyForecast>> getHourlyForecasts(@Path("cityKod") String cityKod,
                                                  @Query("details") boolean details,
                                                  @Query("metric") boolean metric);


}
