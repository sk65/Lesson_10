package com.example.lesson_10.repostory;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.lesson_10.util.App;
import com.example.lesson_10.R;
import com.example.lesson_10.util.SharedPreferencesManager;
import com.example.lesson_10.model.WeatherParamCardModel;
import com.example.lesson_10.network.RetrofitClient;
import com.example.lesson_10.network.WeatherReuest;
import com.example.lesson_10.model.screen.DetailsScreenModel;
import com.example.lesson_10.model.screen.MainScreenModel;
import com.example.lesson_10.model.weather.City;
import com.example.lesson_10.model.weather.DailyForecast;
import com.example.lesson_10.model.weather.HourlyForecast;
import com.example.lesson_10.model.weather.Weather;
import com.example.lesson_10.util.Resource;
import com.example.lesson_10.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.lesson_10.model.enams.AirAndPollenNames.AIR_QUALITY;
import static com.example.lesson_10.util.Constants.CITY_CODE_KEY;
import static com.example.lesson_10.util.Constants.LOCALISED_NAME_KEY;
import static com.example.lesson_10.util.Constants.LOCATION_COORDINATES_KEY;
import static com.example.lesson_10.util.Constants.RESPONSE_CODE_KEY;

public class WeatherRepository implements SharedPreferences.OnSharedPreferenceChangeListener {
    private List<HourlyForecast> hourlyForecasts;
    private List<DailyForecast> dailyForecasts;
    private MutableLiveData<Resource<DetailsScreenModel>> detailsScreenModelLiveData;
    private MutableLiveData<Resource<MainScreenModel>> mainScreenModelLiveData;
    private String locationCoordinates;
    private City city;
    private String localizedName;
    private String cityKey;
    private final WeatherReuest weatherReuest;
    private Context context;

    public WeatherRepository() {
        detailsScreenModelLiveData = new MutableLiveData<>(); //TODO !add model from db to constructor
        mainScreenModelLiveData = new MutableLiveData<>();
        SharedPreferencesManager.getInstance().registerOnSharedPreferenceChangeListener(this);
        this.locationCoordinates = SharedPreferencesManager.getInstance().getLocationCoordinates();
        this.context = App.contextWeakReference.get();
        this.weatherReuest = RetrofitClient.getInstance().create(WeatherReuest.class);
    }

    public LiveData<Resource<DetailsScreenModel>> getDetailsScreenModelFromNetwork() {
        return detailsScreenModelLiveData;
    }

    public LiveData<Resource<MainScreenModel>> getMainScreenModelFromNetwork() {
        return mainScreenModelLiveData;
    }

    public void updateNetwork() {
        if (cityKey != null && localizedName != null) {
            city = new City(cityKey, localizedName);
            detailsScreenModelLiveData.setValue(new Resource.Loading<>());
            mainScreenModelLiveData.setValue(new Resource.Loading<>());
            updateDailyForecasts();
            updateHourlyForecasts();
        } else {
            detailsScreenModelLiveData.setValue(new Resource.Error<>(null, "You shod update city"));
            mainScreenModelLiveData.setValue(new Resource.Error<>(null, "You shod update city"));
        }
    }

    public void updateCity() {
        detailsScreenModelLiveData.setValue(new Resource.Loading<>());
        mainScreenModelLiveData.setValue(new Resource.Loading<>());

        Call<City> call = weatherReuest.getCity(locationCoordinates);
        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(@NotNull Call<City> call, @NotNull Response<City> response) {
                if (response.isSuccessful() && response.body() != null) {
                    city = response.body();
                    SharedPreferencesManager.getInstance().putCityCode(city.getKey());
                    SharedPreferencesManager.getInstance().putLocalizedName(city.getLocalizedName());
                    updateDailyForecasts();
                    updateHourlyForecasts();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        detailsScreenModelLiveData.setValue(new Resource.Error<>(null, jsonObject.getString(RESPONSE_CODE_KEY)));
                        mainScreenModelLiveData.setValue(new Resource.Error<>(null, jsonObject.getString(RESPONSE_CODE_KEY)));

                    } catch (IOException | JSONException e) {
                        detailsScreenModelLiveData.setValue(new Resource.Error<>(null, e.getLocalizedMessage()));
                        mainScreenModelLiveData.setValue(new Resource.Error<>(null, e.getLocalizedMessage()));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<City> call, @NotNull Throwable t) {
                detailsScreenModelLiveData.setValue(new Resource.Error<>(null, t.getLocalizedMessage()));
                mainScreenModelLiveData.setValue(new Resource.Error<>(null, t.getLocalizedMessage()));
            }
        });
    }

    private void updateDailyForecasts() {
        Call<Weather> forecastsCall =
                weatherReuest.getDailyForecasts(city.getKey(), true, true);
        forecastsCall.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(@NotNull Call<Weather> call, @NotNull Response<Weather> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dailyForecasts = response.body().getDailyForecasts();
                    updateLifeData();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        detailsScreenModelLiveData.setValue(new Resource.Error<>(null, jsonObject.getString(RESPONSE_CODE_KEY)));
                        mainScreenModelLiveData.setValue(new Resource.Error<>(null, jsonObject.getString(RESPONSE_CODE_KEY)));
                    } catch (IOException | JSONException e) {
                        detailsScreenModelLiveData.setValue(new Resource.Error<>(null, e.getLocalizedMessage()));
                        mainScreenModelLiveData.setValue(new Resource.Error<>(null, e.getLocalizedMessage()));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Weather> call, @NotNull Throwable t) {
                detailsScreenModelLiveData.setValue(new Resource.Error<>(null, t.getLocalizedMessage()));
                mainScreenModelLiveData.setValue(new Resource.Error<>(null, t.getLocalizedMessage()));
            }
        });
    }

    private void updateHourlyForecasts() {
        Call<List<HourlyForecast>> forecastCall =
                weatherReuest.getHourlyForecasts(city.getKey(), true, true);
        forecastCall.enqueue(new Callback<List<HourlyForecast>>() {
            @Override
            public void onResponse(@NotNull Call<List<HourlyForecast>> call, @NotNull Response<List<HourlyForecast>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    hourlyForecasts = response.body();
                    updateLifeData();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        detailsScreenModelLiveData.setValue(new Resource.Error<>(null, jsonObject.getString(RESPONSE_CODE_KEY)));
                        mainScreenModelLiveData.setValue(new Resource.Error<>(null, jsonObject.getString(RESPONSE_CODE_KEY)));
                    } catch (IOException | JSONException e) {
                        detailsScreenModelLiveData.setValue(new Resource.Error<>(null, e.getLocalizedMessage()));
                        mainScreenModelLiveData.setValue(new Resource.Error<>(null, e.getLocalizedMessage()));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<HourlyForecast>> call, @NotNull Throwable t) {
                detailsScreenModelLiveData.setValue(new Resource.Error<>(null, t.getLocalizedMessage()));
                mainScreenModelLiveData.setValue(new Resource.Error<>(null, t.getLocalizedMessage()));
            }
        });
    }

    public void updateLifeData() {
        if (dailyForecasts != null && hourlyForecasts != null) {
//            MainScreenModel mainScreenModel = new MainScreenModel(
//                    city.localizedName,
//                    String.valueOf(Math.round(hourlyForecasts.get(0).getTemperature().getValue())),
//                    hourlyForecasts.get(0).getIconPhrase(),
//                    Utils.getAirAndPollenValue(AIR_QUALITY.getName(), dailyForecasts.get(0).getAirAndPollen()),
//                    dailyForecasts
//            );

//            List<WeatherParamCardModel> cardModels = new ArrayList<>();
//            cardModels.add(new WeatherParamCardModel(context.getString(R.string.felt),
//                    Math.round(hourlyForecasts.get(0).getRealFeelTemperature().getValue()) + context.getString(R.string.celsius_degree)));
//            cardModels.add(new WeatherParamCardModel(context.getString(R.string.humidity),
//                    context.getString(R.string.humidity_value)));
//            cardModels.add(new WeatherParamCardModel(context.getString(R.string.chance_of_rain),
//                    hourlyForecasts.get(0).getRainProbability() + context.getString(R.string.percent)));
//            cardModels.add(new WeatherParamCardModel(context.getString(R.string.pressure),
//                    hourlyForecasts.get(0).getCeiling().getValue() + context.getString(R.string.pressure_value)));
//            cardModels.add(new WeatherParamCardModel(context.getString(R.string.wind_speed),
//                    hourlyForecasts.get(0).getWind().getSpeed().getValue()
//                            + context.getString(R.string.wind_speed_measuring)));
//            cardModels.add(new WeatherParamCardModel(context.getString(R.string.UV_index),
//                    String.valueOf(hourlyForecasts.get(0).getuVIndex())));
//
//            DetailsScreenModel detailsScreenModel = new DetailsScreenModel(
//                    cardModels,
//                    hourlyForecasts,
//                    dailyForecasts.get(0).getSun().getRise(),
//                    dailyForecasts.get(0).getSun().getSet(),
//                    Utils.getAirAndPollenValue(AIR_QUALITY.getName(), dailyForecasts.get(0).getAirAndPollen())
//            );
            MainScreenModel mainScreenModel = createMainScreenModel();
            DetailsScreenModel detailsScreenModel = createDetailsScreenModel();
            saveDetailsScreenModel(detailsScreenModel);
            saveMainScreenModel(mainScreenModel);

            mainScreenModelLiveData.setValue(new Resource.Success<>(mainScreenModel));
            detailsScreenModelLiveData.setValue(new Resource.Success<>(detailsScreenModel));
        }

    }

    private DetailsScreenModel createDetailsScreenModel() {
        List<WeatherParamCardModel> cardModels = new ArrayList<>();
        cardModels.add(new WeatherParamCardModel(context.getString(R.string.felt),
                Math.round(hourlyForecasts.get(0).getRealFeelTemperature().getValue()) + context.getString(R.string.celsius_degree)));
        cardModels.add(new WeatherParamCardModel(context.getString(R.string.humidity),
                context.getString(R.string.humidity_value)));
        cardModels.add(new WeatherParamCardModel(context.getString(R.string.chance_of_rain),
                hourlyForecasts.get(0).getRainProbability() + context.getString(R.string.percent)));
        cardModels.add(new WeatherParamCardModel(context.getString(R.string.pressure),
                hourlyForecasts.get(0).getCeiling().getValue() + context.getString(R.string.pressure_value)));
        cardModels.add(new WeatherParamCardModel(context.getString(R.string.wind_speed),
                hourlyForecasts.get(0).getWind().getSpeed().getValue()
                        + context.getString(R.string.wind_speed_measuring)));
        cardModels.add(new WeatherParamCardModel(context.getString(R.string.UV_index),
                String.valueOf(hourlyForecasts.get(0).getuVIndex())));
        return new DetailsScreenModel(
                cardModels,
                hourlyForecasts,
                dailyForecasts.get(0).getSun().getRise(),
                dailyForecasts.get(0).getSun().getSet(),
                Utils.getAirAndPollenValue(AIR_QUALITY.getName(), dailyForecasts.get(0).getAirAndPollen())
        );
    }

    private MainScreenModel createMainScreenModel() {
        return new MainScreenModel(
                city.getLocalizedName(),
                String.valueOf(Math.round(hourlyForecasts.get(0).getTemperature().getValue())),
                hourlyForecasts.get(0).getIconPhrase(),
                Utils.getAirAndPollenValue(AIR_QUALITY.getName(), dailyForecasts.get(0).getAirAndPollen()),
                dailyForecasts
        );
    }

    public void saveMainScreenModel(MainScreenModel mainScreenModel) {
        //TODO
    }

    public void saveDetailsScreenModel(DetailsScreenModel detailsScreenModel) {
        //TODO
    }

    public LiveData<Resource<DetailsScreenModel>> getDetailsScreenModelFromDB() {
        //TODO
        return null;
    }

    public LiveData<Resource<MainScreenModel>> getMainScreenModelFromDB() {
        //TODO
        return null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case LOCATION_COORDINATES_KEY:
                updateCity();
                break;
            case LOCALISED_NAME_KEY:
                localizedName = sharedPreferences.getString(key, null);
                break;
            case CITY_CODE_KEY:
                cityKey = sharedPreferences.getString(key, null);
        }
    }
}
