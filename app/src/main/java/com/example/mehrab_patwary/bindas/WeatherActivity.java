package com.example.mehrab_patwary.bindas;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mehrab_patwary.bindas.Currnet.CurrentWeather;
import com.example.mehrab_patwary.bindas.Hourly.HourlyForecast;
import com.example.mehrab_patwary.bindas.Weekly.WeeklyForecast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final String TAG = "WeatherActivity";
    public static final String CURRENT_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String YAHOO_BASE_URL = "https://query.yahooapis.com/v1/public/";
    public static final String GEOCODE_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/";

    private CurrnetApi currnetApi;
    private HourApi hourApi;
    private WeekApi weekApi;
    public static String units = "metric";
    private static String CurrentCity;

//    private double lat, lon;
    private CurrentWeatherInterface currentWeatherInterface;
    private HourInterface hourInterface;
    private WeeklyObjectPass weeklyObjectPass;


    private FusedLocationProviderClient client;
    private LocationRequest request;
    private LocationCallback callback;
    private boolean runStop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        tabLayout = findViewById(R.id.tabLayoutId);
        viewPager = findViewById(R.id.viewPagerId);


        Intent intent = getIntent();
        try {
            if (intent.getAction().equals(Intent.ACTION_SEARCH)){
                String query = intent.getStringExtra(SearchManager.QUERY);
                runStop = false;
                if (query !=null) {
                    searchByeCity(query,units);
                }
                else {
                    Toast.makeText(this, "Type City Correctly", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            client = LocationServices.getFusedLocationProviderClient(this);
            request = new LocationRequest();

            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setInterval(10000);
            request.setFastestInterval(5000);
            getDeviceCurrentLocation();


            tabLayout.addTab(tabLayout.newTab().setText("Current"));
            tabLayout.addTab(tabLayout.newTab().setText("10 DAYS FORCAST"));

            WeatherPagerAdapter adapter = new WeatherPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
    public void pasObjectCurrentWeather(CurrentWeatherInterface currentWeatherInterface){
        this.currentWeatherInterface = currentWeatherInterface;
    }

    public void setWeeklyObject(WeeklyObjectPass weeklyObjectPass){
        this.weeklyObjectPass = weeklyObjectPass;
    }
    public void setHourInterface(HourInterface hourInterface){
        this.hourInterface = hourInterface;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getDeviceCurrentLocation();
        }
    }
    public void getDeviceCurrentLocation(){
        try{
            if(checkLocationPermission()){
                client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        //getWeatherData();

                        if (runStop){
                            appLocation(lat,lon);
                        }
                        else {
                            Toast.makeText(WeatherActivity.this, "Search Called", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }else{
                checkLocationPermission();
            }
        }catch (Exception e){

        }

    }


    private class WeatherPagerAdapter extends FragmentPagerAdapter {
        private int tabCount;
        public WeatherPagerAdapter(FragmentManager fm, int tabCount  ) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new CurrentFragment();
                case 1:
                    return new ForcastFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }


    public void appLocation(double lat,double lon){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CURRENT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        hourApi = retrofit.create(HourApi.class);
        String apiKey = getString(R.string.weather_api_key);

        String customUrl1 = String.format("forecast?lat=%f&lon=%f&units=%s&appid=%s",lat,lon,units,apiKey);

        Call<HourlyForecast> called =  hourApi.getHourlyApi(customUrl1);
        called.enqueue(new Callback<HourlyForecast>() {
            @Override
            public void onResponse(Call<HourlyForecast> call, Response<HourlyForecast> response) {
                if (response.isSuccessful()){
                    try {
                        HourlyForecast hourlyForecast = response.body();
                        week( hourlyForecast.getCity().getName());
                        hourInterface.hourObjectPass(hourlyForecast);
                    }catch (Exception e){
                        Log.d(TAG, "onResponse: "+e.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<HourlyForecast> call, Throwable t) {
                Log.e("tipu", "onFailure: "+t.toString());
                Toast.makeText(WeatherActivity.this, ""+t.toString(), Toast.LENGTH_LONG).show();
            }
        });


        currnetApi = retrofit.create(CurrnetApi.class);
        String customUrl = String.format("weather?lat=%f&lon=%f&units=%s&appid=%s",lat,lon,units,apiKey);

        final Call<CurrentWeather> currentWeatherCall = currnetApi.getCurrentApi(customUrl);

        currentWeatherCall.enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                if(response.code() == 200){
                    try {
                        final CurrentWeather currentWeather = response.body();
                        CurrentCity = currentWeather.getName();
                        currentWeatherInterface.pasObjectWeather(currentWeather);
                    }catch (Exception e){
                        Log.d(TAG, "onResponse: "+e.getMessage());
                    }

                    }
            }

            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
            }
        });



    }

    private void week(String Cit){
        if (Cit !=""){
            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl(YAHOO_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            weekApi = retrofit1.create(WeekApi.class);
            String url = "yql?q=select * from weather.forecast where woeid in (select woeid from geo.places(1) where text%3D\"c%2C"+Cit+"\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
            final Call<WeeklyForecast> weeklyForecastCall = weekApi.setWeekApi(url);
            weeklyForecastCall.enqueue(new Callback<WeeklyForecast>() {
                @Override
                public void onResponse(Call<WeeklyForecast> call, Response<WeeklyForecast> response) {
                    if (response.isSuccessful()){
                        try {
                            WeeklyForecast weeklyForecast = response.body();
                            weeklyObjectPass.setWeeklyObjectpass(weeklyForecast);
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: "+e.getMessage());
                        }

                    }
                    else {
                        Toast.makeText(WeatherActivity.this, ""+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<WeeklyForecast> call, Throwable t) {
                    Toast.makeText(WeatherActivity.this, ""+t.toString(), Toast.LENGTH_LONG).show();
                }
            });

        }
    }




    public boolean checkLocationPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},11);
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.search_menu_id).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.farenheit_menu:
                units = "fahrenheit";
                searchByeCity(CurrentCity,units);
                break;
            case R.id.celcius_menu:
                units ="metric";
                searchByeCity(CurrentCity,units);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchByeCity(String city, String units){
        if (city !=null){
            String apiKey = getString(R.string.weather_api_key);

            String customUrl1 = String.format("forecast?q=%s&units=%s&appid=%s",city,units,apiKey);
            String customUrl = String.format("weather?q=%s&units=%s&appid=%s",city,units,apiKey);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CURRENT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            hourApi = retrofit.create(HourApi.class);


            Call<HourlyForecast> called =  hourApi.getHourlyApi(customUrl1);
            called.enqueue(new Callback<HourlyForecast>() {
                @Override
                public void onResponse(Call<HourlyForecast> call, Response<HourlyForecast> response) {
                    if (response.isSuccessful()){
                        try {
                            HourlyForecast hourlyForecast = response.body();
                            hourInterface.hourObjectPass(hourlyForecast);
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: "+e.getMessage());
                        }

                    }
                }

                @Override
                public void onFailure(Call<HourlyForecast> call, Throwable t) {
                    Log.e("tipu", "onFailure: "+t.toString());
                    Toast.makeText(WeatherActivity.this, ""+t.toString(), Toast.LENGTH_LONG).show();
                }
            });


            currnetApi = retrofit.create(CurrnetApi.class);


            final Call<CurrentWeather> currentWeatherCall = currnetApi.getCurrentApi(customUrl);

            currentWeatherCall.enqueue(new Callback<CurrentWeather>() {
                @Override
                public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                    if(response.code() == 200){
                        try {

                            final CurrentWeather currentWeather = response.body();
                            currentWeatherInterface.pasObjectWeather(currentWeather);
                            CurrentCity = currentWeather.getName();
                        }catch (Exception e){

                        }

                    }
                }

                @Override
                public void onFailure(Call<CurrentWeather> call, Throwable t) {
                }
            });

            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl(YAHOO_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            weekApi = retrofit1.create(WeekApi.class);
            String url = "yql?q=select * from weather.forecast where woeid in (select woeid from geo.places(1) where text%3D\"nome%2C"+city+"\")&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
            final Call<WeeklyForecast> weeklyForecastCall = weekApi.setWeekApi(url);
            weeklyForecastCall.enqueue(new Callback<WeeklyForecast>() {
                @Override
                public void onResponse(Call<WeeklyForecast> call, Response<WeeklyForecast> response) {
                    if (response.isSuccessful()){
                        try {
                            WeeklyForecast weeklyForecast = response.body();
                            if (weeklyForecast !=null){
                                weeklyObjectPass.setWeeklyObjectpass(weeklyForecast);
                            }
                        }catch (Exception e){
                            Log.d(TAG, "onResponse: "+e.getMessage());
                        }

                    }
                    else {
                        Toast.makeText(WeatherActivity.this, ""+response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<WeeklyForecast> call, Throwable t) {
                    Toast.makeText(WeatherActivity.this, ""+t.toString(), Toast.LENGTH_LONG).show();
                }
            });

        }

    }

}
