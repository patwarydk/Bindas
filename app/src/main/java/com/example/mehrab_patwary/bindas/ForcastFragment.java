package com.example.mehrab_patwary.bindas;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mehrab_patwary.bindas.Weekly.WeeklyForecast;


public class ForcastFragment extends Fragment implements WeeklyObjectPass{

    private ListView weeklyForecastList;
    private WeeklyForcastAdapter adapter;
    public ForcastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_forcast, container, false);
        weeklyForecastList = v.findViewById(R.id.weeklyForecastList);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        context = getActivity();
        ((WeatherActivity) context).setWeeklyObject(this);
    }

    @Override
    public void setWeeklyObjectpass(WeeklyForecast weeklyForecast) {

        if (weeklyForecast.equals(null)){
            Toast.makeText(getActivity(), "This city is not found in Yahoo Weather Api", Toast.LENGTH_SHORT).show();
        }else if (weeklyForecast.getQuery().getResults() !=null){
            if (weeklyForecast.getQuery().getResults().getChannel() !=null) {
                if (weeklyForecast.getQuery().getResults().getChannel().getItem() !=null) {
                    adapter = new WeeklyForcastAdapter(getActivity(), weeklyForecast.getQuery().getResults().getChannel().getItem().getForecast());
                    weeklyForecastList.setAdapter(adapter);
                }
            }
        }
    }
}