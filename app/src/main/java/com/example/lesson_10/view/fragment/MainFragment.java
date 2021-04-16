package com.example.lesson_10.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lesson_10.adapter.MainRecyclerViewAdapter;
import com.example.lesson_10.databinding.FragmentMainBinding;
import com.example.lesson_10.model.screen.MainScreenModel;
import com.example.lesson_10.util.Resource;
import com.example.lesson_10.view.MainActivity;

import java.util.LinkedList;


public class MainFragment extends BaseFragment {
    private FragmentMainBinding binding;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.recyclerViewMainFragment;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MainRecyclerViewAdapter viewAdapter = new MainRecyclerViewAdapter(getContext(), new LinkedList<>());
        recyclerView.setAdapter(viewAdapter);
        binding.buttonMainFragmentDetails.setOnClickListener((v) ->
                ((MainActivity) requireActivity()).getViewPager().setCurrentItem(1));

        getWeatherViewModel()
                .getMainScreenModelNetwork()
                .observe(getViewLifecycleOwner(), resource -> {
                    if (resource != null) {
                        if (resource instanceof Resource.Success) {
                            hideNetworkProgressDialog();
                            updateMainScreenUI(resource.getData());
                        } else if (resource instanceof Resource.Loading) {
                            showNetworkProgressDialog();
                        } else if (resource instanceof Resource.Error) {
                            hideNetworkProgressDialog();
                            Toast.makeText(getContext(), resource.getMassage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateMainScreenUI(MainScreenModel mainScreenModel) {
        binding.textViewMainFragmentTodayTemperature.setText(mainScreenModel.getTodayTemperature());
        binding.textViewMainFragmentTodayDescription.setText(mainScreenModel.getTodayDescription());
        binding.textViewMainFragmentTodayAirQualityIndex.setText(mainScreenModel.getTodayAiQualityIndex());
        binding.textViewMainFragmentCityHolder.setText(mainScreenModel.getCity());
        MainRecyclerViewAdapter viewAdapter = new MainRecyclerViewAdapter(getContext(), mainScreenModel.getCards());
        recyclerView.setAdapter(viewAdapter);
    }

}