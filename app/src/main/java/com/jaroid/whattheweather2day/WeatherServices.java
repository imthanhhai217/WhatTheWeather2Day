package com.jaroid.whattheweather2day;

import com.google.gson.JsonObject;
import com.jaroid.whattheweather2day.models.CurrentWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherServices {

    @GET("weather?")
    Call<JsonObject> getWeatherByCityName(@Query("q") String cityName,
                                          @Query("appid") String api_key);

    @GET("weather?")
    Call<CurrentWeatherResponse> getWeatherByCityNameModel(@Query("q") String cityName,
                                                      @Query("appid") String api_key);
}
