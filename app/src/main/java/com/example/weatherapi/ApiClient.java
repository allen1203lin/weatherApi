package com.example.weatherapi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public Retrofit myWeatherApi(){
        return new Retrofit.Builder()
                .baseUrl("https://opendata.cwa.gov.tw/api/v1/rest/datastore/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory.create())
                .build();
    }
}