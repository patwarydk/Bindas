package com.example.mehrab_patwary.bindas;

import android.telecom.Call;

import com.example.mehrab_patwary.bindas.Currnet.CurrentWeather;

import retrofit2.http.GET;
import retrofit2.http.Url;

public interface CurrnetApi {
    @GET()
    retrofit2.Call<CurrentWeather> getCurrentApi(@Url String urlString);

}
