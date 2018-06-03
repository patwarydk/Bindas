package com.example.mehrab_patwary.bindas;


import com.example.mehrab_patwary.bindas.Weekly.WeeklyForecast;

import retrofit2.http.GET;

public interface WeeklyObjectPass {
    @GET
    void setWeeklyObjectpass(WeeklyForecast weeklyForecast);
}
