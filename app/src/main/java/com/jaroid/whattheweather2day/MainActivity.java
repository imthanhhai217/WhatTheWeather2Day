package com.jaroid.whattheweather2day;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jaroid.whattheweather2day.models.CurrentWeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView tvName, tvDescription, tvTemp, tvCurrentTemp,
            tvHumidity, tvSeaLevel, tvWindSpeed, tvTempMax, tvPressure;
    private ImageView imgWeather;

    private WeatherServices mWeatherServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mWeatherServices = RetrofitClient.getService(Global.BASE_URL, WeatherServices.class);
        mWeatherServices
                .getWeatherByCityName("Hà Nội", Global.API_KEY)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                Gson gson = new Gson();
                                CurrentWeatherResponse currentWeather = gson.
                                        fromJson(response.body().toString(), CurrentWeatherResponse.class);
                                Log.d(TAG, "onResponse: " + currentWeather.getName());
                            } else {
                                Log.d(TAG, "onResponse: " + response.code() + " | " + response.message());
                            }
                        } else {
                            Log.d(TAG, "onResponse: unsuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });

        mWeatherServices
                .getWeatherByCityNameModel("Nam Định", Global.API_KEY)
                .enqueue(new Callback<CurrentWeatherResponse>() {
                    @Override
                    public void onResponse(Call<CurrentWeatherResponse> call, Response<CurrentWeatherResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                CurrentWeatherResponse model = response.body();
                                bindCurrentWeather(model);
                                Log.d(TAG, "onResponse: " + model.getName());
                            } else {
                                Log.d(TAG, "onResponse: " + response.code() + " | " + response.message());
                            }
                        } else {
                            Log.d(TAG, "onResponse: unsuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentWeatherResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
    }

    private void initView() {
        tvName = findViewById(R.id.tvName);
        tvDescription = findViewById(R.id.tvDescription);
        tvTemp = findViewById(R.id.tvTemp);
        tvCurrentTemp = findViewById(R.id.tvCurrentTemp);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvSeaLevel = findViewById(R.id.tvSeaLevel);
        tvWindSpeed = findViewById(R.id.tvWindSpeed);
        tvTempMax = findViewById(R.id.tvTempMax);
        tvPressure = findViewById(R.id.tvPressure);
        imgWeather = findViewById(R.id.imgWeather);
    }

    private void bindCurrentWeather(CurrentWeatherResponse model) {
        tvName.setText(model.getName());
        tvDescription.setText(model.getWeather().get(0).getDescription());
        tvTemp.setText(Global.convertK2C(model.getMain().getTemp()));
        tvCurrentTemp.setText(Global.convertK2C(model.getMain().getFeelsLike()));
        tvHumidity.setText(model.getMain().getHumidity()+" %");
        tvSeaLevel.setText(model.getMain().getSeaLevel()+"");
        tvWindSpeed.setText(model.getWind().getSpeed()+" m/s");
        tvTempMax.setText(Global.convertK2C(model.getMain().getTempMax()));
        tvPressure.setText(model.getMain().getPressure()+" hPa");

        Glide.with(getApplicationContext())
                .load(Global.getUrlIcon(model.getWeather().get(0).getIcon()))
                .into(imgWeather);
    }
}