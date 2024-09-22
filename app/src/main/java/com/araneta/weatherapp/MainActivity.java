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
        etCity = findViewById(R.id.etCity);
        etCountry = findViewById(R.id.etCountry);
        tvResult = findViewById(R.id.tvResult);
    }

    public void getWeatherDetails(View view) {
        String city = "Cebu City";
        String country = "Philippines";
        String tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, tempUrl, response -> {
            String output = "";
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                String description = jsonObjectWeather.getString("description");
                JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                double temp = jsonObjectMain.getDouble("temp") - 273.15;
                double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                float pressure = jsonObjectMain.getInt("pressure");
                int humidity = jsonObjectMain.getInt("humidity");
                JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                String wind = jsonObjectWind.getString("speed");
                JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                String clouds = jsonObjectClouds.getString("all");
                JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                String countryName = jsonObjectSys.getString("country");
                String cityName = jsonResponse.getString("name");
                tvResult.setTextColor(Color.rgb(68, 134, 199));
                String suggestion = getWeatherSuggestion(temp, humidity);
                output += "Current weather of " + cityName + " (" + countryName + ")"
                        + "\n Temp: " + df.format(temp) + " °C"
                        + "\n Feels Like: " + df.format(feelsLike) + " °C"
                        + "\n Humidity: " + humidity + "%"
                        + "\n Description: " + description
                        + "\n Wind Speed: " + wind + "m/s (meters per second)"
                        + "\n Cloudiness: " + clouds + "%"
                        + "\n Pressure: " + pressure + " hPa"
                        + "\n Suggestions: " + suggestion + " hPa";
                tvResult.setText(output);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show());
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private String getWeatherSuggestion(double temp, int humidity) {
        String[] hotSuggestions = {
                "Stay cool and hydrated!",
                "Maybe it's time for some ice cream!",
                "Perfect weather for a swim!"
        };

        String[] warmSuggestions = {
                "A nice day for a walk.",
                "Enjoy the outdoors!",
                "Great day for a picnic."
        };

        String[] coolSuggestions = {
                "Perfect for a cozy day indoors.",
                "How about some warm tea?",
                "Cool weather—stay comfy!"
        };

        String[] chillySuggestions = {
                "Time for a sweater!",
                "A good day for a warm drink.",
                "Stay warm!"
        };

        String[] highHumiditySuggestions = {
                "Quite humid today, stay light!",
                "Feels sticky—dress lightly.",
                "Humid weather, stay fresh!"
        };

        String[] lowHumiditySuggestions = {
                "Dry air today, stay moisturized.",
                "Low humidity—keep hydrated!",
                "Dry day, drink plenty of water!"
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

        // Add a humidity-based suggestion
        if (humidity > 80) {
            suggestion += " " + highHumiditySuggestions[random.nextInt(highHumiditySuggestions.length)];
        } else if (humidity < 40) {
            suggestion += " " + lowHumiditySuggestions[random.nextInt(lowHumiditySuggestions.length)];
        }

        return suggestion;
    }


}