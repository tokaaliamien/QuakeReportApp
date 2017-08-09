package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.drawable.GradientDrawable;

import static android.R.attr.resource;

/**
 * Created by Demo on 2017-07-21.
 */

public class EarthquakeAdaptor extends ArrayAdapter {
    public EarthquakeAdaptor(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }


        Earthquake currentEarthquake = (Earthquake) getItem(position);


        TextView magTextView = (TextView) listItemView.findViewById(R.id.magnitude);
        double mag = currentEarthquake.getMag();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String result = decimalFormat.format(mag);


        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magTextView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagColor(mag);

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);
        magTextView.setText(result);


        String place = currentEarthquake.getPlace();
        int index = findIndex(place);
        if (index != -1) {
            TextView offsetTextView = (TextView) listItemView.findViewById(R.id.location_offset);
            offsetTextView.setText(place.substring(0, index));

            TextView locationTextView = (TextView) listItemView.findViewById(R.id.primary_location);
            locationTextView.setText(place.substring(index, place.length()));
        }


        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        long date = currentEarthquake.getDate();
        Date dateObject = new Date(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD, yyyy");
        result = dateFormat.format(dateObject);
        dateTextView.setText(result);

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        result = timeFormat.format(dateObject);
        timeTextView.setText(result);


        return listItemView;

    }

    private int findIndex(String place) {
        if (place.contains("of"))
            return place.indexOf("of") + 2;

        else if (place.contains("Near"))
            return place.indexOf("Near") + 8;

        else
            return -1;
    }

    private int getMagColor(double mag) {
        int approx = (int) Math.floor(mag);
        int magnitudeColor;
        switch (approx) {
            case 0:
            case 1:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude1);
                break;
            case 2:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude2);
                break;
            case 3:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude3);
                break;
            case 4:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude4);
                break;
            case 5:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude5);
                break;
            case 6:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude6);
                break;
            case 7:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude7);
                break;
            case 8:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude8);
                break;
            case 9:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude9);
                break;
            default:
                magnitudeColor = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
        }
        return magnitudeColor;


    }

}
