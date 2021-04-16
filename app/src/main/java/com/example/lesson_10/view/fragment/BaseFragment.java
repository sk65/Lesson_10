package com.example.lesson_10.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.lesson_10.util.DialogUtils;
import com.example.lesson_10.viewmodel.WeatherViewModel;

public abstract class BaseFragment extends Fragment {
    private WeatherViewModel weatherViewModel;
    private ProgressDialog networkProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkProgressDialog = DialogUtils.buildNetworkProgressDialog(getContext());
        weatherViewModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
    }

    public WeatherViewModel getWeatherViewModel() {
        return weatherViewModel;
    }

    public void showNetworkProgressDialog() {
        networkProgressDialog.show();
    }

    public void hideNetworkProgressDialog() {
        networkProgressDialog.dismiss();
    }
}
