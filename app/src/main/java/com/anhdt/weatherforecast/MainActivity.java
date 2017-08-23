package com.anhdt.weatherforecast;

import android.Manifest;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSON_CODE = 1001;
    private EditText edtSearchCity;
    private Button btnOK, btnNextDays;
    private TextView tvCity, tvCountry, tvTemperature, tvState, tvHumidity, tvCloud, tvWind, tvDayUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.INTERNET}, PERMISSON_CODE);
        }
        initViews();
    }

    private void getWeatherCurrentData(final String data) {


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=53fbf527d52d4d773e828243b90c1f8e";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            tvCity.setText(name);

                            long l = Long.valueOf(day);
                            Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                            String dayTmp = simpleDateFormat.format(date);

                            tvDayUpdate.setText(dayTmp);


                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("KET QUA: ", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error when connect to internet", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(stringRequest);

    }

    private void initViews() {
        edtSearchCity = (EditText) findViewById(R.id.edt_search);

        btnOK = (Button) findViewById(R.id.btn_search);
        btnNextDays = (Button) findViewById(R.id.btn_days_next);

        tvCity = (TextView) findViewById(R.id.tv_city);
        tvCountry = (TextView) findViewById(R.id.tv_country);
        tvTemperature = (TextView) findViewById(R.id.tv_temperature);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvHumidity = (TextView) findViewById(R.id.tv_humidity);
        tvCloud = (TextView) findViewById(R.id.tv_cloud);
        tvWind = (TextView) findViewById(R.id.tv_wind);
        tvDayUpdate = (TextView) findViewById(R.id.tv_day_update);

        btnOK.setOnClickListener(this);
        btnNextDays.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                searchCity();
                break;
            case R.id.btn_days_next:
                forecastNextDays();
                break;
            default:
                break;
        }
    }

    private void forecastNextDays() {

    }

    private void searchCity() {
        String city = edtSearchCity.getText().toString();
        getWeatherCurrentData(city);
    }
}
