package com.piedpiper;


import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.Series;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by taabishkathawala on 11/5/17.
 */

public class SightingsGraphActivity extends AppCompatActivity {

    Date start = new Date(2015 - 1900, 1, 1);
    Date end = new Date(2017 - 1900, 1, 1);

    private DatePickerDialog datePickerDialog;
    private GraphView graph;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightingsgraph);


        graph = (GraphView) findViewById(R.id.graph);
        updateGraph();
        graph.getViewport().setScrollable(true);

        //set default start and end dates
        Button setStartButton = (Button) findViewById(R.id.graphstartdate_button_id);
        Button setEndButton = (Button) findViewById(R.id.graphenddate_button_id);

        setStartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SightingsGraphActivity.this,
                        new mDateSetListener(0), start.getYear() + 1900, start.getMonth(), start.getDate());
                dialog.show();
            }
        });
        setEndButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SightingsGraphActivity.this,
                        new mDateSetListener(1), end.getYear() + 1900, end.getMonth(), end.getDate());
                dialog.show();
            }
        });
    }


    private void updateGraph() {
        HashMap<String, Integer> points = new HashMap<>();
        List<String> xLabels = new ArrayList<>();
        for (RatSighting sighting : MainActivity.sightingsList) {
            Date date;
            try {
                date = new SimpleDateFormat("MM/dd/yy HH:mm").parse(sighting.getCreatedDate());
            } catch (ParseException e) {
                date = new Date();
            }
            if (date.compareTo(start) >= 0 && date.compareTo(end) <= 0) {
                String x = String.valueOf(date.getYear()) + "/" + String.valueOf(date.getMonth());
                xLabels.add(x);
                if (points.containsKey(x)) points.put(x, points.get(x) + 1);
                else points.put(x, 1);
            }
        }
        String[] labels = xLabels.toArray(new String[xLabels.size()]);
        Arrays.sort(labels);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(labels);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.setHorizontalScrollBarEnabled(true);
        graph.getViewport().setXAxisBoundsManual(true);


        DataPoint[] arr = new DataPoint[labels.length];
        for (int i = 1; i < labels.length; i++) {
            arr[i - 1] = new DataPoint(i, points.get(labels[i - 1]) == null ? 0 : points.get(labels[i - 1]));
        }


        //GraphView newGraph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(arr);
        graph.addSeries(series);
    }


    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        int mYear, mMonth, mDay;
        int date;
        // if date is 0, then we are updating the start date, else we are updating the end variable
        public mDateSetListener(int date) {
            this.date = date;
        }
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // getCalender();
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
//            String add = mMonth + "/" + mDay + "/" + mYear + " 00:00:00 0";
            if (date == 0) {
                start = new Date(mYear - 1900, mMonth, mDay);
            } else {
                end = new Date(mYear - 1900, mMonth, mDay);
            }
            updateGraph();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();

    }
}
