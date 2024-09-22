package com.araneta.weatherapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;


import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity, etCountry;
    TextView tvResult;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private String appid;
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appid = getString(R.string.apiKey);
        tvResult = findViewById(R.id.tvResult);
    }

    public void getWeatherDetails(View view) {
        String city = "Cebu City";
        String country = "Philippines";
        String tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                double temp = jsonObjectMain.getDouble("temp") - 273.15; // Convert Kelvin to Celsius
                String suggestion = getSuggestion(temp); // Get suggestion based on temperature

                // Display temperature and suggestion only
                String output = "Temp: " + df.format(temp) + " °C"
                        + "\nSuggestion: " + suggestion;

                tvResult.setTextColor(Color.rgb(68, 134, 199));
                tvResult.setText(output);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show());

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private String getSuggestion(double temp) {
        String[] hotSuggestions = {
                "Stay cool and hydrated!",
                "Maybe it's time for some ice cream!",
                "Perfect weather for a swim!",
                "Stay in the shade when possible.",
                "Don't forget your sunscreen!",
                "Light, breathable clothing is a must!",
                "How about a cold smoothie?",
                "Best to stay indoors during peak heat.",
                "Take frequent breaks if you're outside.",
                "An icy drink can help cool you down!"
        };

        String[] warmSuggestions = {
                "A nice day for a walk.",
                "Enjoy the outdoors!",
                "Great day for a picnic.",
                "Perfect for some outdoor sports!",
                "A beautiful day for a bike ride.",
                "Open the windows and let the breeze in.",
                "How about a nice outdoor brunch?",
                "Catch up on gardening!",
                "Ideal weather for a scenic hike.",
                "Wear light layers just in case!"
        };

        String[] coolSuggestions = {
                "Perfect for a cozy day indoors.",
                "How about some warm tea?",
                "Cool weather—stay comfy!",
                "Snuggle up with a good book.",
                "Maybe time for a light jacket.",
                "Great weather for a brisk walk.",
                "How about baking something warm?",
                "A light scarf might come in handy.",
                "A nice day for indoor activities.",
                "Consider layering up just in case!"
        };

        String[] chillySuggestions = {
                "Time for a sweater!",
                "A good day for a warm drink.",
                "Stay warm!",
                "Consider bundling up a bit more today.",
                "Perfect day for some hot cocoa!",
                "You might need a coat today.",
                "Stay inside and keep warm if possible.",
                "A chilly day—keep your feet warm!",
                "How about a bowl of soup?",
                "Mittens might be a good idea!"
        };

        Random random = new Random();
        String suggestion = "";

        if (temp > 30) {
            suggestion = hotSuggestions[random.nextInt(hotSuggestions.length)];
        } else if (temp > 24 && temp <= 30) {
            suggestion = warmSuggestions[random.nextInt(warmSuggestions.length)];
        } else if (temp >= 18 && temp <= 24) {
            suggestion = coolSuggestions[random.nextInt(coolSuggestions.length)];
        } else if (temp < 18) {
            suggestion = chillySuggestions[random.nextInt(chillySuggestions.length)];
        }


        return suggestion;
    }


}