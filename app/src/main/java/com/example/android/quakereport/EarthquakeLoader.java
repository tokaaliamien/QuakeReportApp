package com.example.android.quakereport;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.support.v4.content.AsyncTaskLoader;

import static com.example.android.quakereport.EarthquakeActivity.LOG_TAG;


/**
 * Created by Demo on 2017-07-28.
 */

public class EarthquakeLoader extends AsyncTaskLoader <List<Earthquake>> {
    private static final String LOG_TAG=EarthquakeLoader.class.getSimpleName();
    private static final String BASE_URL="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    public EarthquakeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG,"loader startLoading");
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.e(LOG_TAG,"loader in background");
        URL url=null;
        try {
            url=new URL(BASE_URL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Can't make url");
        }

        String jsonResponse="";

        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        try {
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                Log.e(LOG_TAG,"Connected");
                inputStream=urlConnection.getInputStream();
                StringBuilder output=new StringBuilder();
                if (inputStream!=null){
                    InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
                    BufferedReader reader=new BufferedReader(inputStreamReader);
                    String line=reader.readLine();
                    while (line!=null){
                        output.append(line);
                        line=reader.readLine();
                    }
                }

                jsonResponse=output.toString();
                Log.e(LOG_TAG,"json: "+jsonResponse);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"IOException exception ");
        }finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG,"can't close inputStream");
                }
            }
        }

        final List<Earthquake> earthquakes=extractFeatureFromJson(jsonResponse);

        return earthquakes;
    }

    private List<Earthquake> extractFeatureFromJson(String earthquakeJSON){
        if(TextUtils.isEmpty(earthquakeJSON))
            return null;

        List<Earthquake>result=null;

        try {
            JSONObject jsonObject=new JSONObject(earthquakeJSON);
            JSONArray features=jsonObject.getJSONArray("features");

            result=new ArrayList<>();

            for(int i=0;i<features.length();i++){
                JSONObject temp=features.getJSONObject(i);
                JSONObject properties=temp.getJSONObject("properties");
                double mag=properties.getDouble("mag");
                String place=properties.getString("place");
                long date=properties.getLong("time");
                String url=properties.getString("url");

                result.add(new Earthquake(mag,place,date,url));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

}
