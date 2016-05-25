package com.example.david_nealon.beemonitorsecond;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class StartScreen extends AppCompatActivity implements View.OnClickListener {

    static String json = "";
    public Calendar c = Calendar.getInstance();
    ArrayList<String> humList = new ArrayList(50);
    ArrayList<String> tempList = new ArrayList(50);
    ArrayList<String> massList = new ArrayList(50);
    ArrayList<String> timeStamps = new ArrayList(50);
    TextView h;
    TextView t;
    TextView m;
    TextView tr;
    String error = "Could not retrieve values! Make sure device is connected to the Internet!";
    TextView currentTime;
    HttpURLConnection urlConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_start_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Bee Monitor");
        Button refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);
        Button prButton = (Button) findViewById(R.id.prButton);
        prButton.setOnClickListener(this);
        Button chartButton = (Button) findViewById(R.id.chartButton);
        chartButton.setOnClickListener(this);
        h = (TextView) findViewById(R.id.humView);
        t = (TextView) findViewById(R.id.tempView);
        m = (TextView) findViewById(R.id.massView);
        tr = (TextView) findViewById(R.id.results);

        //currentTime = (TextView) findViewById(R.id.currentTime);
        //currentTime.setText((CharSequence) c);
        new JSONTask().execute("https://data.sparkfun.com/output/4J025nLanNH7mj0gn413.json");

    }


    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String humValue = "";
            String tempValue = "";
            String massValue = "";
            String timeStamp = "";
            String humValueA = "";
            String tempValueA = "";
            String massValueA = "";
            String timeStampA = "";


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
                //JSONObject firstObject = new JSONObject(finalJson);
                JSONArray firstArray = new JSONArray(finalJson);
                JSONObject finalObject = firstArray.getJSONObject(0);
                humValue = finalObject.getString("humidity");
                massValue = finalObject.getString("mass");
                tempValue = finalObject.getString("temperature");
                timeStamp = finalObject.getString("timestamp");
                for (int i = 0; i < 50; i++) {
                    JSONObject jsonobject = firstArray.getJSONObject(i);
                    timeStampA = jsonobject.getString("timestamp");
                    timeStamps.add(timeStampA);
                    humValueA = jsonobject.getString("humidity");
                    humList.add(humValueA);
                    massValueA = jsonobject.getString("mass");
                    massList.add(massValueA);
                    tempValueA = jsonobject.getString("temperature");
                    tempList.add(tempValueA);

                }

                return tempValue + "              " + massValue + "               " +humValue + "\n\n\n\n\n" + timeStamp;
                //return tempValue;



            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }

            return error;
        }



        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tr.setTextSize(30);
            tr.setTextColor(Color.parseColor("#FFFFFF"));
            tr.setText(result);
        }






    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Clicking the refresh button
    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.refreshButton: {

                //Fetch current date & time for updates
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                //String currDate = sdf.format(new Date());
                //currentTime.setText(currDate);

                //Repeat JSON Task
                new JSONTask().execute("https://data.sparkfun.com/output/4J025nLanNH7mj0gn413.json");


            }
        }
        switch (v.getId()) {
            case R.id.chartButton:  {
                Intent i = new Intent(this, SecondScreen.class);
                i.putExtra("humData", humList);
                i.putExtra("tempData", tempList);
                i.putExtra("massData", massList);
                startActivity(i);

            }
        }

        switch (v.getId()) {
            case R.id.prButton:  {
                Intent i = new Intent(this, ThirdScreen.class);
                i.putExtra("humData", humList);
                i.putExtra("tempData", tempList);
                i.putExtra("massData", massList);
                i.putExtra("timeStamp", timeStamps);
                startActivity(i);
            }
        }
    }
}