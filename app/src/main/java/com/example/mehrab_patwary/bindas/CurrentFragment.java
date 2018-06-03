package com.example.mehrab_patwary.bindas;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mehrab_patwary.bindas.Currnet.CurrentWeather;

import com.example.mehrab_patwary.bindas.Hourly.HourlyForecast;
import com.squareup.picasso.Picasso;


public class CurrentFragment extends Fragment implements CurrentWeatherInterface,HourInterface {

    private static final String HOUR_ICON_BASE_URL = "http://openweathermap.org/img/w/";

    private TextView cityTV,dateTime,tempText,cloude,cloudePercent,sunriseTV,sunsetTV;
    private GridView gridView;
    private ImageView iconImage;

    private TextView hourDeg1,hourDeg2,hourDeg3,hourDeg4,hourTime1,hourTime2,hourTime3,hourTime4;
    private ImageView hourIcon1,hourIcon2,hourIcon3,hourIcon4;

    public CurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_current, container, false);
        cityTV = v.findViewById(R.id.cityTextView);
        dateTime = v.findViewById(R.id.dateTime);
        tempText = v.findViewById(R.id.tempText);
        cloude = v.findViewById(R.id.cloude);
        cloudePercent = v.findViewById(R.id.cloudePercent);
        iconImage = v.findViewById(R.id.iconImage);
        sunriseTV = v.findViewById(R.id.sunrise);
        sunsetTV = v.findViewById(R.id.sunset);

        //hourly ids find
        hourDeg1 = v.findViewById(R.id.hourDeg1);
        hourDeg2 = v.findViewById(R.id.hourDeg2);
        hourDeg3 = v.findViewById(R.id.hourDeg3);
        hourDeg4 = v.findViewById(R.id.hourDeg4);
        hourTime1 = v.findViewById(R.id.hourTime1);
        hourTime2 = v.findViewById(R.id.hourTime2);
        hourTime3 = v.findViewById(R.id.hourTime3);
        hourTime4 = v.findViewById(R.id.hourTime4);

        //hour Icons
        hourIcon1 = v.findViewById(R.id.hourIcon1);
        hourIcon2 = v.findViewById(R.id.hourIcon2);
        hourIcon3 = v.findViewById(R.id.hourIcon3);
        hourIcon4 = v.findViewById(R.id.hourIcon4);

        return v;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = getActivity();
        ((WeatherActivity)context).pasObjectCurrentWeather(this);
        ((WeatherActivity)context).setHourInterface(this);

    }



    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void pasObjectWeather(CurrentWeather currentWeather) {

        cityTV.setText(currentWeather.getName().toUpperCase().toString());

        dateTime.setText(currentWeather.getDt());

        tempText.setText(currentWeather.getMain().getTemp());
        String cloudperc = currentWeather.getClouds().getAll().toString()+"%";
        cloudePercent.setText(cloudperc);
        cloude.setText(currentWeather.getWeather().get(0).getMain().toUpperCase());

        switch (currentWeather.getWeather().get(0).getIcon().toString()){
            case "01d":
                iconImage.setImageResource(R.drawable.o1d);
                break;
            case "02d":
                iconImage.setImageResource(R.drawable.o2d);
                break;
            case "03d":
                iconImage.setImageResource(R.drawable.o3d);
                break;
            case "04d":
                iconImage.setImageResource(R.drawable.onedrive);
                break;
            case "09d":
                iconImage.setImageResource(R.drawable.o9d);
                break;
            case "10d":
                iconImage.setImageResource(R.drawable.o10d);
                break;
            case "13d":
                iconImage.setImageResource(R.drawable.o13d);
                break;
            case "50d":
                iconImage.setImageResource(R.drawable.o50d);
                break;
            case "01n":
                iconImage.setImageResource(R.drawable.o1n);
                break;
            case "02n":
                iconImage.setImageResource(R.drawable.o2n);
                break;
            case "03n":
                iconImage.setImageResource(R.drawable.o3n);
                break;
            case "04n":
                iconImage.setImageResource(R.drawable.o4n);
                break;
            case "09n":
                iconImage.setImageResource(R.drawable.o9n);
                break;
            case "10n":
                iconImage.setImageResource(R.drawable.o10n);
                break;
            case "11n":
                iconImage.setImageResource(R.drawable.o11n);
                break;
             case "13n":
                iconImage.setImageResource(R.drawable.o13n);
                break;
             case "50n":
                iconImage.setImageResource(R.drawable.o50n);
                break;
        }


        sunriseTV.setText(currentWeather.getSys().getSunrise());


        sunsetTV.setText(currentWeather.getSys().getSunset());
    }

    @Override
    public void hourObjectPass(HourlyForecast hourlyForcast) {




        hourDeg1.setText(hourlyForcast.getList().get(0).getMain().getTemp());
        hourDeg2.setText(hourlyForcast.getList().get(1).getMain().getTemp());
        hourDeg3.setText(hourlyForcast.getList().get(2).getMain().getTemp());
        hourDeg4.setText(hourlyForcast.getList().get(3).getMain().getTemp());

        String icon1 = HOUR_ICON_BASE_URL+hourlyForcast.getList().get(0).getWeather().get(0).getIcon()+".png";
        Picasso.get().load(icon1).into(hourIcon1);

        String icon2 = HOUR_ICON_BASE_URL+hourlyForcast.getList().get(1).getWeather().get(0).getIcon()+".png";
        Picasso.get().load(icon2).into(hourIcon2);

        String icon3 = HOUR_ICON_BASE_URL+hourlyForcast.getList().get(2).getWeather().get(0).getIcon()+".png";
        Picasso.get().load(icon3).into(hourIcon3);

        String icon4 = HOUR_ICON_BASE_URL+hourlyForcast.getList().get(4).getWeather().get(0).getIcon()+".png";
        Picasso.get().load(icon4).into(hourIcon4);

        hourTime1.setText(hourlyForcast.getList().get(0).getDt());

        hourTime2.setText(hourlyForcast.getList().get(1).getDt());

        hourTime3.setText(hourlyForcast.getList().get(2).getDt());

        hourTime4.setText(hourlyForcast.getList().get(3).getDt());


    }


}
