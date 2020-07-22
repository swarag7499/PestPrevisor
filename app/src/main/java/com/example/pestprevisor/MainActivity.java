package com.example.pestprevisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.pestprevisor.tflite.Classifier;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private Spinner locationSpinner;
    private Button btn;
    public  TextView showData;
    private ImageView iconimage;
    private ImageView cp;
    static JSONObject data = null;
    static String loc;

    public static final String TAG = "No-Internet";
//5b39c6
    /*

    <gradient android:startColor="#ADFF2F"
    android:endColor="#2E8B57"
    android:angle="270" />

    <gradient android:startColor="#00ced1"
        android:endColor="#6495ed"
        android:angle="270"

        />
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context;

        locationSpinner = findViewById(R.id.spinner);
        showData = findViewById(R.id.main_showData);
        iconimage = findViewById(R.id.imageView);
        cp = findViewById(R.id.imageView2);
        btn = findViewById(R.id.btnSubmit);

        cp.setImageResource(R.drawable.caterpillar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottomNavigationHomeMenuId:
                                Toast.makeText(getApplicationContext(), "Home screen", Toast.LENGTH_LONG).show();
                              //  startActivity (new Intent(MainActivity.this, Home.class));
                                return true;

                            case R.id.bottomNavigationCameraMenuId:
                                Toast.makeText(getApplicationContext(), "Detection screen", Toast.LENGTH_LONG).show();
                                startActivity (new Intent(MainActivity.this, ClassifierActivity.class));
                                return  false;

                            case R.id.bottomNavigationKnowMenuId:
                                Toast.makeText(getApplicationContext(), "Know More screen", Toast.LENGTH_LONG).show();
                                startActivity (new Intent(MainActivity.this, KnowMore.class));
                                return true;

                        }
                        return false;
                    }
                });


if(isOnline()) {


    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.location_arrays, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    locationSpinner.setAdapter(adapter);


    locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            getJSON(parent.getItemAtPosition(position).toString());
            loc = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });


    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Predict.class);
            startActivity(intent);
        }
    });

}

else{
    try {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();

        alertDialog.setTitle("No Internet Connectivity");
        alertDialog.setMessage("Internet not available.\nMake sure your mobile internet/WiFi is on and try again.");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });

        alertDialog.show();
    } catch (Exception e) {
        Log.d("Show Dialog: " , e.getMessage());
    }
}


    }






    public void getJSON(final String city) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                //   URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city +"&units=metric"+ "&APPID=609306c389d2ab7bd4d3159c99186cae");
                  //  URL url = new URL("https://api.weatherbit.io/v2.0/forecast/daily?city="+city+"&key=7a9847e4957343b89d9b9e8240092b29" );
                    URL url = new URL("https://api.weatherbit.io/v2.0/current?city="+city+"&key=7a9847e4957343b89d9b9e8240092b29" );
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";
                    while ((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();
                    data = new JSONObject(json.toString());
                   /* if (data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }*/
                } catch (Exception e) {
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void Void) {
                if (data != null) {
                    try {
                        showData.setText(formatJSON(data));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();

    }

    public String formatJSON(JSONObject data) throws JSONException {
        JSONArray jr = data.getJSONArray("data");
        String result = null;
        long sunrise=0,sunset=0,sunshine_hrs=0;
        JSONObject jb1 = jr.getJSONObject(0);
        JSONObject jb2 = jb1.getJSONObject("weather");
        result = "\n Temperature : "+jb1.getString("temp")+ " °C";
     //   result+= "\n\n Min Temperature : "+jb1.getString("min_temp")+ " °C";
        result+= "\n\n Humidity : "+jb1.getString("rh")+ " %";
        result+= "\n\n Rainfall : "+jb1.getString("precip")+ " mm";
        result+= "\n\n Windspeed : "+jb1.getString("wind_spd")+ " m/s ";
        result+= "\n\n Description: "+jb2.getString("description");
        result+= "\n\n Pressure : " + jb1.getString("pres") + " mb";
     //   result+= "\n \n Sunrise : " + jb1.getString("sunrise") +"\tSunset :"+ jb1.getString("sunset");




        String icon = jb2.getString("icon");
        String icon_url= "https://www.weatherbit.io/static/img/icons/"+icon+".png";
        Picasso.with(this).load(icon_url).into(iconimage);

      /*  sunrise = jb1.getLong("sunrise_ts");
        sunset = jb1.getLong("sunset_ts");
        Date date = new java.util.Date(sunrise*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH ");
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);

        Date date1 = new java.util.Date(sunset*1000L);
        SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("HH ");
        String formattedDate1 = sdf1.format(date1);
        System.out.println(formattedDate1);*/

       // Log.i(".......",humidity);

        return (result);
      // String result = "\n\n Latitude : "+data.getJSONObject("coord").getString("lat");
      /*  result+="\n Max Temp  : "+data.getJSONObject("main").getString("temp_max")+"°C";
        result+="\n Min Temp  : "+data.getJSONObject("main").getString("temp_min")+"°C";
        result+="\n   Humidity  : "+data.getJSONObject("main").getString("humidity")+ "%";
        result+="\n Pressure  : "+data.getJSONObject("main").getString("pressure")+ " hPa";*/







    }

    private boolean isOnline() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }


}

