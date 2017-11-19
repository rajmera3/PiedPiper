package com.piedpiper;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by taabishkathawala on 11/5/17.
 * Activity to handle graph information
 */

public class SightingsGraphActivity extends AppCompatActivity {

    private final int offset = 1900;
    private Date start = new Date(2015 - offset, 1, 1);
    private Date end = new Date(2017 - offset, 1, 1);

    private DatePickerDialog datePickerDialog;
    private GraphView graph;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sightingsgraph);


        graph = findViewById(R.id.graph);
        updateGraph();
        graph.getViewport().setScrollable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setLabelsSpace(5);
        graph.getGridLabelRenderer().setNumHorizontalLabels(5);
//        graph.setHorizontalScrollBarEnabled(true);
//        graph.getViewport().setMinX(0);
//        graph.getViewport().setMaxX(4);

        //set default start and end dates
        Button setStartButton = findViewById(R.id.graphstartdate_button_id);
        Button setEndButton = findViewById(R.id.graphenddate_button_id);

        setStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SightingsGraphActivity.this,
                        new mDateSetListener(0), start.getYear() + offset, start.getMonth(),
                        start.getDate());
                dialog.show();
            }
        });
        setEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(SightingsGraphActivity.this,
                        new mDateSetListener(1), end.getYear() + offset, end.getMonth(),
                        end.getDate());
                dialog.show();
            }
        });
    }

    /**
     * Updates Graph Points
     */
    private void updateGraph() {
        HashMap<String, Integer> points = new HashMap<>();
        List<String> xLabels = new ArrayList<>();
        Log.d("sunwoo", "ok really is " + MainActivity.sightingsList.size());
        for (RatSighting sighting : MainActivity.sightingsList) {
            Date date;
            try {
                date = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.US).
                        parse(sighting.getCreatedDate());
            } catch (ParseException e) {
                date = new Date();
            }
            if ((date.compareTo(start) >= 0) && (date.compareTo(end) <= 0)) {
                String x = String.valueOf(date.getYear() + offset) + "/" +
                        String.valueOf(date.getMonth() + 1);
                if (points.containsKey(x)) {
                    points.put(x, points.get(x) + 1);
                } else {
                    points.put(x, 1);
                    xLabels.add(x);
                }
            }
        }
        if (start.compareTo(end) >= 0) {
            // start has to be before end
            AlertDialog alertDialog = new AlertDialog.Builder(SightingsGraphActivity.this).create();
            alertDialog.setTitle("Incorrect date range");
            alertDialog.setMessage("Start date must be before end date");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
        if (xLabels.size() < 2) {
            xLabels.add("");
            xLabels.add("");
        }
        Log.d("sunwoo", "" + xLabels.size());
        String[] labels = xLabels.toArray(new String[xLabels.size()]);
        Arrays.sort(labels);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(labels);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        //graph.setHorizontalScrollBarEnabled(true);
        //graph.getViewport().setXAxisBoundsManual(true);


        DataPoint[] arr = new DataPoint[labels.length];
        for (int i = 1; i < labels.length + 1; i++) {
            arr[i - 1] = new DataPoint(i,
                    (points.get(labels[i - 1]) == null) ? 0 : points.get(labels[i - 1]));
        }



        GraphView newGraph = findViewById(R.id.graph);
        //BarGraphSeries<DataPoint> series = new BarGraphSeries<>(arr);
        //newGraph.addSeries(series);
        newGraph.addSeries(new BarGraphSeries<>(arr));
        graph = newGraph;
    }


    class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        int mYear;
        int mMonth;
        int mDay;
        final int date;
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
                start = new Date(mYear - offset, mMonth, mDay);
            } else {
                end = new Date(mYear - offset, mMonth, mDay);
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
