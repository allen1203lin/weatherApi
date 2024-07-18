package com.example.weatherapi;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Presenter implements Contract.Presenter {
    private getAPI getApi;
    private final Contract.View callback;

    public Presenter(getAPI getapi, Contract.View view) {
        this.callback = view;
        this.getApi = getapi;
    }

    public void getWeather(String selectedElement, String selectedLocation, String selectedTime) {

        String authorization = "CWA-25C9C6F3-AA7A-4C7F-9E21-F227628CE53B";
        if(selectedElement.equals("All")) selectedElement=""; // if "All" is select , return empty string
        String finalSelectedElement = selectedElement; //store final element
        getApi.getWeatherApi(authorization,selectedLocation,finalSelectedElement)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WeatherResponse>() {
                    @Override
                    public void onNext(@NonNull WeatherResponse weatherResponse) {
                       callback.setTextNull(); //clear textview to remove previous data
                        List time_list = Arrays.asList(callback.getTimeData());// convert string to list
                        List element_list = Arrays.asList(callback.getElementData()); // convert string to list
                        //check if user chose multiple element
                        if(weatherResponse.getElementSize() != 1){
                            callback.setMultipleResultTextView(weatherResponse , time_list , selectedTime);
                        }
                        else {
                            //if single element is clicked , return it's data
                            callback.setSingleResultTextView(weatherResponse , element_list ,
                                    time_list , finalSelectedElement , selectedTime);
                            }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("test","onError:");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("test","onComplete:");
                    }
                });
    }
}
