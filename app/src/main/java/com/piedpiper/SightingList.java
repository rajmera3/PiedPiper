package com.piedpiper;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pbokey on 10/9/17.
 */

public class SightingList extends ArrayAdapter<RatSighting> {
    private final Activity context;
    private final List<RatSighting> sightingList;

    /**
     * Constructor for sightings list
     * @param context the context
     */
    public SightingList(Activity context) {
        super(context, R.layout.activity_ratsightings, MainActivity.sightingsList);
        this.context = context;
        this.sightingList = MainActivity.sightingsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.ratsightings_layout, new LinearLayout(context), true);

        TextView mainListText = listViewItem.findViewById(R.id.mainListText);

        RatSighting sighting = sightingList.get(position);
        mainListText.setText(sighting.getUniqueKey());

        return listViewItem;
    }

}
