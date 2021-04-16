package com.example.lesson_10.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.lesson_10.BuildConfig;
import com.example.lesson_10.R;
import com.example.lesson_10.util.SharedPreferencesManager;
import com.example.lesson_10.adapter.SectionsPagerAdapter;
import com.example.lesson_10.databinding.ActivityMainBinding;
import com.example.lesson_10.util.DialogUtils;
import com.example.lesson_10.util.Resource;
import com.example.lesson_10.util.Utils;
import com.example.lesson_10.viewmodel.LocationViewModel;
import com.example.lesson_10.viewmodel.WeatherViewModel;
import com.google.android.material.snackbar.Snackbar;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.example.lesson_10.util.Constants.LOCATION_PERMISSIONS;
import static com.example.lesson_10.util.Constants.LOCATION_SETTINGS_REQUEST_CODE;
import static com.example.lesson_10.util.Constants.REQUEST_LOCATION_PERMISSIONS_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private AlertDialog.Builder locationSettingsDialog;
    private ProgressDialog locationProgressDialog;

    private LocationViewModel locationViewModel;
    private WeatherViewModel weatherViewModel;

    private  ViewPager viewPager;
    private SwipeRefreshLayout refreshLayout;
    private boolean refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        weatherViewModel =  new ViewModelProvider(this).get(WeatherViewModel.class);

        setContentView(binding.getRoot());
        initUI();

        locationViewModel.getLocation().observe(this, resource -> {
            if (resource != null) {
                if (resource instanceof Resource.Success) {
                    locationProgressDialog.dismiss();
                    SharedPreferencesManager
                            .getInstance()
                            .putLocationCoordinates(
                                    Utils.getLocationString(resource.getData()));
                } else if (resource instanceof Resource.Loading) {
                    locationProgressDialog.show();
                } else if (resource instanceof Resource.Error) {
                    locationProgressDialog.dismiss();
                    Toast.makeText(this,
                            resource.getMassage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (savedInstanceState == null) {
            updateWeatherData();
        } else {
            //TODO
            // presenter.refreshUI();
        }
    }

    private void updateWeatherData() {
        if (checkPermissions()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(NETWORK_PROVIDER) ||
                    locationManager.isProviderEnabled(GPS_PROVIDER)) {
                if (!refresh) {
                    locationViewModel.updateLocation();
                } else {
                    weatherViewModel.updateNetwork();
                    refresh = false;
                }
            } else {
                locationSettingsDialog.show();
            }
        } else {
            requestPermissions();
        }
    }

    private void initUI() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        locationProgressDialog = DialogUtils.buildLocationProgressDialog(this);
        locationSettingsDialog = DialogUtils.buildLocationSettingsDialog(this);
        locationProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_cancel),
                (dialogInterface, i) -> locationViewModel.removeLocationUpdate());

        // build refreshLayout
        refreshLayout = binding.srlMainActivityMainContainer;
        refreshLayout.setOnRefreshListener(() -> {
            refresh = true;
            updateWeatherData();
            refreshLayout.setRefreshing(false);
        });

        // build viewPager
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPager = binding.viewPagerMainActivity;
        viewPager.setAdapter(sectionsPagerAdapter);
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            updateWeatherData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateWeatherData();
            } else {
                Snackbar.make(
                        findViewById(R.id.srl_mainActivity_mainContainer),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, view -> startAppSettings())
                        .show();
            }
        }
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                LOCATION_PERMISSIONS,
                REQUEST_LOCATION_PERMISSIONS_REQUEST_CODE);
    }

    public void startAppSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Snackbar.make(
                    findViewById(R.id.srl_mainActivity_mainContainer),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, view -> requestLocationPermissions())
                    .show();
        } else {
            requestLocationPermissions();
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }
}