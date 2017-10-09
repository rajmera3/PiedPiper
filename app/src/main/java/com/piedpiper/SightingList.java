package com.piedpiper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pbokey on 10/9/17.
 */

public class SightingList extends ArrayAdapter<RatSighting> {
    private Activity context;
    private List<RatSighting> sightingList;

    public SightingList(Activity context, List<RatSighting> rsList) {
        super(context, R.layout.activity_ratsightings, rsList);
        this.context = context;
        this.sightingList = rsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.ratsightings_layout, null, true);

        TextView incidentZip = (TextView) listViewItem.findViewById(R.id.textViewIncidentZip);

        RatSighting sighting = sightingList.get(position);
        incidentZip.setText(sighting.getIncidentZip());

        return listViewItem;
    }
}
