/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;

import static android.R.id.message;
import static com.example.android.quakereport.R.string.emptyMessage;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private EarthquakeAdaptor adapter;
    private ListView earthquakeListView=null;
    private TextView message;
    private ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        earthquakeListView = (ListView) findViewById(R.id.list);
        message=(TextView)findViewById(R.id.message);
        earthquakeListView.setEmptyView(message);


        if(isConnected) {
            getSupportLoaderManager().initLoader(0, null, this).forceLoad();
            Log.e(LOG_TAG, "loader init");
        }else {
            message.setText(R.string.internetMessage);
            progressBar.setVisibility(View.GONE);
        }



    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG,"loader on create");
        return new EarthquakeLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader,final List<Earthquake> earthquakes) {

        // Find a reference to the {@link ListView} in the layout


        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdaptor adapter=new EarthquakeAdaptor(EarthquakeActivity.this,earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);


        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake earthquake=earthquakes.get(position);
                String url=earthquake.getUrl();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

            }
        });

        Log.e(LOG_TAG,"loader finished");
        message.setText(R.string.emptyMessage);

        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.e(LOG_TAG,"loader reset");

    }

    /*private class EarthquakeAsyncTask extends AsyncTask<URL,Void,List<Earthquake>>{

        /*@Override
        protected List<Earthquake> doInBackground(URL... params) {
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




        @Override
        protected void onPostExecute(final List<Earthquake> earthquakes) {


        }
    }*/
}
