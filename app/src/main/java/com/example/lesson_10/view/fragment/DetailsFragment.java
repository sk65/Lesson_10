package com.example.lesson_10.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lesson_10.adapter.DetailsGridViewAdapter;
import com.example.lesson_10.adapter.DetailsRecyclerViewAdapter;
import com.example.lesson_10.databinding.FragmentDetailsBinding;
import com.example.lesson_10.model.screen.DetailsScreenModel;
import com.example.lesson_10.util.Resource;
import com.example.lesson_10.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DetailsFragment extends BaseFragment {

    private FragmentDetailsBinding binding;

    private RecyclerView recyclerView;
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.recyclerViewFragmentDetailsHourlyForecast;
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        gridView = binding.gridViewFragmentDetails;

        getWeatherViewModel()
                .getDetailsScreenModelFromNetwork()
                .observe(getViewLifecycleOwner(), resource -> {
                    if (resource != null) {
                        if (resource instanceof Resource.Success) {
                            hideNetworkProgressDialog();
                            updateDetailsScreenUI(resource.getData());
                        } else if (resource instanceof Resource.Loading) {
                            showNetworkProgressDialog();
                        } else if (resource instanceof Resource.Error) {
                            hideNetworkProgressDialog();
                            Toast.makeText(getContext(), resource.getMassage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    public void updateDetailsScreenUI(DetailsScreenModel detailsScreenModel) {
        Date currentDate = new Date();
        if (!currentDate.before(detailsScreenModel.getSunrise()) && !currentDate.after(detailsScreenModel.getSunset())) {
            double sunRiseTime = Double.parseDouble(new SimpleDateFormat("kk.mm").format(detailsScreenModel.getSunrise()));
            double sunSetTime = Double.parseDouble(new SimpleDateFormat("kk.mm").format(detailsScreenModel.getSunset()));
            double currentTime = Double.parseDouble(new SimpleDateFormat("kk.mm").format(currentDate));
            double progressBarMaxValue = sunSetTime * 60 - sunRiseTime * 60;
            binding.progressBarFragmentDetails.setMax((int) progressBarMaxValue);
            int progress = (int) (currentTime * 60 - sunRiseTime * 60);
            binding.progressBarFragmentDetails.setProgress(progress);
        } else {
            binding.progressBarFragmentDetails.setProgress(100);
        }
        binding.textViewDetailsFragmentSunriseValue.setText(" " + Utils.getCurrentHour(detailsScreenModel.getSunrise()));
        binding.textViewDetailsFragmentSunsetValue.setText(" " + Utils.getCurrentHour(detailsScreenModel.getSunset()));
        binding.textViewDetailsFragmentAirQualityIndexValue.setText(detailsScreenModel.getAirQuality());

        DetailsRecyclerViewAdapter viewAdapter = new DetailsRecyclerViewAdapter(getContext(), detailsScreenModel.getHourlyForecasts());
        recyclerView.setAdapter(viewAdapter);
        DetailsGridViewAdapter gridViewAdapter = new DetailsGridViewAdapter(getContext(), detailsScreenModel.getWeatherParamCardModels());
        gridView.setAdapter(gridViewAdapter);
    }
}
