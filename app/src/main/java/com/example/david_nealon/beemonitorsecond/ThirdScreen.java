package com.example.david_nealon.beemonitorsecond;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.content.Context;
import android.widget.TextView;
import android.widget.ScrollView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class ThirdScreen extends AppCompatActivity {


    HttpURLConnection urlConnection;
    TextView readingVals;
    String error = "Could not retrieve values! Make sure device is connected to the Internet!";
    //ArrayList<String> humListR = new ArrayList(50);
    //ArrayList<String> tempListR = new ArrayList(50);
    //ArrayList<String> massListR = new ArrayList(50);
    //ArrayList<String> timeStampR = new ArrayList(50);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Previous Readings");
        readingVals = (TextView) findViewById(R.id.readingVals);
        Intent i = getIntent();
        //humListR = i.getStringArrayListExtra("humData");
        //tempListR = i.getStringArrayListExtra("tempData");
        //massListR = i.getStringArrayListExtra("massData");
        //timeStampR = i.getStringArrayListExtra("timeStamp");

        new JSONTask().execute("https://data.sparkfun.com/output/4J025nLanNH7mj0gn413.json");

    }


    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String humValue = "";
            String tempValue = "";
            String massValue = "";
            String timeStamp = "";
            ArrayList humList = new ArrayList();


            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream input = urlConnection.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(input));
                StringBuffer buffer = new StringBuffer();

                String lineEnd = "";
                while ((lineEnd = read.readLine()) != null) {
                    buffer.append(lineEnd);
                }

                String finalJson = buffer.toString();
                JSONArray firstArray = new JSONArray(finalJson);
                String resultVal = "";

                for (int i = 0; i < firstArray.length(); i++) {
                    JSONObject jsonobject = firstArray.getJSONObject(i);
                    timeStamp = jsonobject.getString("timestamp");
                    humValue = jsonobject.getString("humidity");
                    humList.add(humValue);
                    massValue = jsonobject.getString("mass");
                    tempValue = jsonobject.getString("temperature");
                    resultVal += "\n" + timeStamp + "               " + humValue + "  /   " + massValue + "  /   " + tempValue;

                }

                return resultVal;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return error;
        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            readingVals.setTextSize(15);
            readingVals.setTextColor(Color.parseColor("#0000FF"));
            readingVals.setText(result);
        }



    }
}
