package com.example.lesson_10.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.lesson_10.model.screen.DetailsScreenModel;
import com.example.lesson_10.model.screen.MainScreenModel;
import com.example.lesson_10.repostory.WeatherRepository;
import com.example.lesson_10.util.Resource;

public class WeatherViewModel extends ViewModel {
    private final WeatherRepository weatherRepository;

    public WeatherViewModel() {
        weatherRepository = new WeatherRepository();
    }

    public void updateNetwork() {
        weatherRepository.updateNetwork();
    }

    public void updateCity() {
        weatherRepository.updateCity();
    }

    public LiveData<Resource<DetailsScreenModel>> getDetailsScreenModelFromNetwork() {
        return weatherRepository.getDetailsScreenModelFromNetwork();
    }

    public LiveData<Resource<DetailsScreenModel>> getDetailsScreenModelFromDB() {
        return weatherRepository.getDetailsScreenModelFromDB();
    }

    public LiveData<Resource<MainScreenModel>> getMainScreenModelFromDB() {
        return weatherRepository.getMainScreenModelFromDB();
    }

    public LiveData<Resource<MainScreenModel>> getMainScreenModelNetwork() {
        return weatherRepository.getMainScreenModelFromNetwork();
    }

    public void saveMainScreenModel(MainScreenModel mainScreenModel) {
        weatherRepository.saveMainScreenModel(mainScreenModel);
    }

    public void saveMainScreenModel(DetailsScreenModel detailsScreenModel) {
        weatherRepository.saveDetailsScreenModel(detailsScreenModel);
    }
}
