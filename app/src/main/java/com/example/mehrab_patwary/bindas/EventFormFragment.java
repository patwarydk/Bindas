package com.example.mehrab_patwary.bindas;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFormFragment extends Fragment {
    private EditText cDate, sDate, eDate, allDate;
    private Calendar calendar;
    private int year, month, day;
    private String date, createDate;


    public EventFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event_form, container, false);
        cDate = v.findViewById(R.id.createDate);
        sDate = v.findViewById(R.id.startDate);
        eDate = v.findViewById(R.id.endDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month= calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
// Date picker event listner start **********************************************
        cDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    calendar.set(i,i1,i2);
                                    date = sdf.format(calendar.getTime());
                                    Calendar c = Calendar.getInstance();
                                    createDate = sdf.format(c.getTime());
                                    cDate.setText(date);
                                }
                            }, year, month, day);
                    datePickerDialog.show();
                }
            });

        sDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                calendar.set(i,i1,i2);
                                date = sdf.format(calendar.getTime());
                                Calendar c = Calendar.getInstance();
                                createDate = sdf.format(c.getTime());
                                cDate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        eDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                calendar.set(i,i1,i2);
                                date = sdf.format(calendar.getTime());
                                Calendar c = Calendar.getInstance();
                                createDate = sdf.format(c.getTime());
                                cDate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
// Date picker event listner end *****************************************************
        return v;
    }

}
