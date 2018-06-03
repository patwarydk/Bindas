package com.example.mehrab_patwary.bindas;


import com.example.mehrab_patwary.bindas.Hourly.HourlyForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface HourApi {
    @GET()
    Call<HourlyForecast> getHourlyApi(@Url String urlString);
}
