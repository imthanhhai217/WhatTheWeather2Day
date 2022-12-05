package com.jaroid.whattheweather2day;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofitInstance;

    private static Retrofit getRetrofitInstance(String url) {
        if (retrofitInstance == null) {
            //Create retrofit instance with url
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

    public static <T> T getService(String url, Class<T> services) {
        return getRetrofitInstance(url).create(services);
    }
}
