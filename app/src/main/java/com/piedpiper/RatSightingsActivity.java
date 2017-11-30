package com.piedpiper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by pbokey on 10/9/17.
 * Activity to handle the rat sighting information displayed
 */

public class RatSightingsActivity extends AppCompatActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratsightings);
        MainActivity.updateSightingList();
        list = findViewById(R.id.RatSightingListView);
        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Object o = list.getItemAtPosition(i);
                //RatSighting curr = (RatSighting) o;
                RatSighting curr = (RatSighting) list.getItemAtPosition(i);
                Intent sightingDetailView = new Intent(getBaseContext(), SightingDetailView.class);
                sightingDetailView.putExtra("Sighting", curr);
                startActivity(sightingDetailView);
            }
        });

        //SightingList adapter = new SightingList(RatSightingsActivity.this);
        //list.setAdapter(adapter);
        list.setAdapter(new SightingList(RatSightingsActivity.this));
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
        finish();

    }
}