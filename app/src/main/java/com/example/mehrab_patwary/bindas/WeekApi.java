package com.example.mehrab_patwary.bindas;


import com.example.mehrab_patwary.bindas.Weekly.WeeklyForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeekApi {
    @GET
    Call<WeeklyForecast> setWeekApi(@Url String urlString);
}
