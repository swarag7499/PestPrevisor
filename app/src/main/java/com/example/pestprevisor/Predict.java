package com.example.pestprevisor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Predict extends AppCompatActivity {

    private Button btnPredict;
    public TextView showData,showData1;
    static JSONObject data = null;
   String location = null;



    int i =0, j=0, k= 1;;
    Map<String, Float> predictions = new HashMap<>();
    Map<String, Float> newHash = new HashMap<>();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prediction);

        this.location = MainActivity.loc;
        showData = findViewById(R.id.showData);
        showData1 = findViewById(R.id.pred);
        btnPredict = findViewById(R.id.btnPredict);
       // showData.setText(location);


        float[] vec1 = {31.41f, 19.76f, 68.85f, 22.07f, 3.56f, 6.045f}; //Aphid

        float[] vec2 = {32.49f, 20.35f, 61.68f, 13.19f, 1.13f, 4.89f}; //Spodoptera

        float[] vec3 = {31.08f, 17.33f, 61.65f, 11.33f, 1.65f, 6.22f}; //PinkBollworm

        float[] vec4 = {30.70f, 19.31f, 67.69f, 22.18f, 3.41f, 6.19f}; //Mealybug

        float[] vec5 = {32.20f, 20.57f, 66.86f, 19.35f, 3.27f, 5.86f}; //Jassid

        float[] vec6 = {31.47f, 19.25f, 65.90f, 19.64f, 3.35f, 5.94f }; //Thrips

        float[] vec7 ={31.58f, 18.52f, 62.72f, 15.07f, 1.81f, 6.13f}; //AmericanBollworm

        float[] vec8 ={32.40f, 19.15f, 66.3f, 13.84f, 2.07f, 6.45f}; //BacteriaLeafBlight

        float[] vec9 = {35.99f, 25.30f, 56.27f, 1.20f, 1.50f, 5.06f}; //AlternariaLeafBlight


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottomNavigationHomeMenuId:
                                Toast.makeText(getApplicationContext(), "Home screen", Toast.LENGTH_LONG).show();
                                startActivity (new Intent(Predict.this, MainActivity.class));

                                return true;

                            case R.id.bottomNavigationCameraMenuId:
                                Toast.makeText(getApplicationContext(), "Detection screen", Toast.LENGTH_LONG).show();
                                startActivity (new Intent(Predict.this, ClassifierActivity.class));
                                return true;

                            case R.id.bottomNavigationKnowMenuId:
                                Toast.makeText(getApplicationContext(), "Know More screen", Toast.LENGTH_LONG).show();
                                startActivity (new Intent(Predict.this, KnowMore.class));
                                return true;


                        }
                        return false;
                    }
                });

        getJSON(location);


        btnPredict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] inp1 = new String[7];
                float[] inp= new float[6];
                try {
                    inp1 = formatJSON(data);

                    for(int i=0;i<inp.length; i++) {
                        inp[i] = Float.valueOf(inp1[i + 1]);
                        Log.i("INPUT ARRAY -------", String.valueOf(inp[i]));
                    }

                    create_Dictionary(vec1,inp);
                    create_Dictionary(vec2,inp);
                    create_Dictionary(vec3,inp);
                    create_Dictionary(vec4,inp);
                    create_Dictionary(vec5,inp);
                    create_Dictionary(vec6,inp);
                    create_Dictionary(vec7,inp);
                    create_Dictionary(vec8,inp);
                    create_Dictionary(vec9,inp);

                    newHash = sortByValue((HashMap<String, Float>) predictions);

                    for (Map.Entry<String, Float> en : newHash.entrySet()) {
                          if(j==5) break;
                        else{
                            showData1.append("\n\t\t\t" + k + ".\t" + en.getKey() +
                                    "\t --- \t\t\t" + en.getValue() + "%");
                            k++;
                            j++;
                        }
                    }

                    if(isHighSeverity(newHash)){
                        try{
                            AlertDialog alertDialog = new AlertDialog.Builder(Predict.this,R.style.MyAlertDialogStyle).create();
                            alertDialog.setTitle("High Severity Alert");
                            alertDialog.setMessage("The severity of predicted pests is HIGH(>90). Please refer to Know More for help.");
                            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            });
                          //  AlertDialog.Builder builder = new AlertDialog.Builder(Predict.this, R.style.MyAlertDialogStyle);
                            alertDialog.show();
                        }
                        catch (Exception e){
                            Log.d("Show Dialog: " , e.getMessage());
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

      /* String[] inp1 = new String[7];
        try {
              inp1 = formatJSON(data);
              for(int i=0;i<inp1.length;i++)
              Log.i("INPUT ARRAY -------", inp1[i]);
              //showData1.append(inp1[0] +"\n");

        } catch (JSONException e) {
            e.printStackTrace();
        }*/


   /*     float[] inp = new float[6];
       for(int i=1;i<inp1.length;i++)
           inp[i]= Float.valueOf(inp1[i]);





        create_Dictionary(vec1,inp);
        create_Dictionary(vec2,inp);
        create_Dictionary(vec3,inp);
        create_Dictionary(vec4,inp);
        create_Dictionary(vec5,inp);
        create_Dictionary(vec6,inp);

        newHash = sortByValue((HashMap<String, Float>) predictions);
         btnPredict.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 for (Map.Entry<String, Float> en : newHash.entrySet()) {
                     //  if(j==3) break;
                     //  else {
                     showData1.append(en.getKey() +
                             " --- " + en.getValue() + "%\n");
                     // j++;
                     // }
                 }

             }
         });

    */
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
                    URL url = new URL("https://api.weatherbit.io/v2.0/forecast/daily?city="+city+"&key=7a9847e4957343b89d9b9e8240092b29" );
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
                        String[] arr =new String[7];
                         arr =formatJSON(data);
                        showData.setText(arr[0]);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();

    }

    public String[] formatJSON(JSONObject data) throws JSONException {
        JSONArray jr = data.getJSONArray("data");
        String humidity = null,result =null,max_temp =null,min_temp= null,preci = null,wind = null;
        long sunrise_ts=0,sunset_ts=0,sum_rise =0, sum_set=0;
        float sum_hum =0;
        float sum_max=0,sum_min=0,sum_wind=0,sum_preci=0;


        for(int i=0;i<7;i++)
        {   JSONObject jb2 = jr.getJSONObject(i);
             max_temp = jb2.getString("max_temp");
            humidity = jb2.getString("rh");
            min_temp = jb2.getString("min_temp");
            preci = jb2.getString("precip");
            wind = jb2.getString("wind_spd");
            sunrise_ts =jb2.getLong("sunrise_ts");
            sunset_ts =jb2.getLong("sunset_ts");
             Log.i(".......",max_temp);
            Log.i(".......",min_temp);
            Log.i(".......",humidity);
            Log.i(".......",preci);
            Log.i(".......",wind);
            Log.i(".......",String.valueOf(sunrise_ts));
            Log.i(".......",String.valueOf(sunset_ts));
             sum_max = sum_max+ Float.valueOf(max_temp);
            sum_hum = sum_hum+ Float.valueOf(humidity);
            sum_min = sum_min + Float.valueOf(min_temp);
            sum_preci = sum_preci + Float.valueOf(preci);
            sum_wind = sum_wind+ Float.valueOf(wind);
            sum_rise = sum_rise+Long.valueOf(sunrise_ts);
            sum_set = sum_set+Long.valueOf(sunset_ts);
        }

        Date sr =new Date(sum_rise*1000);
        Date ss = new Date(sum_set*1000);
        long diffInMillies = Math.abs(ss.getTime()- sr.getTime());
        long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        String mxT=null,mnT=null, rh=null, rn=null,win=null,ssh=null;
        mxT =Float.toString(sum_max/7);
        mnT=Float.toString(sum_min/7);
        rh =Float.toString(sum_hum/7);
        rn =Float.toString(sum_preci/7);
        win =Float.toString(sum_wind/7);
        ssh = Long.toString(diff/7);


         result = "\n Max Temperature : "+Float.toString(sum_max/7)+ " °C";
         result+= "\n\n Min Temperature : "+Float.toString(sum_min/7)+ " °C";
         result+="\n\n Humidity : " + Float.toString(sum_hum/7) + " %";
        result+="\n\n Rainfall : " + Float.toString(sum_preci/7) + " mm";
        result+= "\n\n Wind Speed : "+Float.toString(sum_wind/7)+ " m/s";
        result+= "\n\n Sunshine Hours : "+Long.toString(diff/7)+ " hrs";


     String[] input ={result,mxT,mnT,rh,rn,win,ssh};

        return (input);


    }




    public float cosinesim(float[] vec, float[] in){
        float dotProduct =0f;
        float normA =0f;
        float normB =0f;
        for(int i =0; i<vec.length;i++){
            dotProduct += vec[i] * in[i];
            normA += Math.pow(vec[i],2);
            normB += Math.pow(in[i],2);
        }
        double res =(Math.sqrt(normA) * Math.sqrt(normB));
        float r = (float)res;

        return (dotProduct/r);
    }

    public void create_Dictionary(float[] a, float[] b){

        String[] keys ={"Aphid" ,"Spodoptera" , "PinkBollworm", "Mealybug", "Jassid", "Thrips", "AmericanBollworm", "BacteriaLeafBlight", "AlternariaLeafBlight"};

        predictions.put(keys[i], cosinesim(a,b)*100 );
        Log.i("...........",String.valueOf(predictions));

        i++;

        //  Log.i(".......",String.valueOf(sortByValue((HashMap<String, Float>) predictions)));
    }


    public static HashMap<String, Float> sortByValue(HashMap<String, Float> hm){
        List<Map.Entry<String, Float> > list =
                new LinkedList<Map.Entry<String, Float> >(hm.entrySet());


        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Float> >() {
            public int compare(Map.Entry<String, Float> o1,
                               Map.Entry<String, Float> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        }.reversed());

        // put data from sorted list to hashmap
        HashMap<String, Float> temp = new LinkedHashMap<String, Float>();
        for (Map.Entry<String, Float> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }


    public boolean isHighSeverity(Map<String, Float> hm){
       boolean isHigh = false;
        Float value = hm.entrySet().stream().findFirst().get().getValue();
        if(value> 90){
            isHigh = true;
        }
        return (isHigh);
    }

}





