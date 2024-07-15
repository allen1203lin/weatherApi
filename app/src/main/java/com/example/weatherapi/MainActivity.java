package com.example.weatherapi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import com.example.weatherapi.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private String[] location_data,element_data,time_data,tw_element;
    private String selected_location,selected_element,selected_time;
    private getAPI getApi;
    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataBinding(); //set up databinding
        getData(); // load spinner data from resource
        apiClient = new ApiClient();
        getApi = apiClient.myWeatherApi().create(getAPI.class);
        setSpinner();
        binding.mainSearchButton.setOnClickListener(view -> getWeather(selected_element,selected_location,selected_time)); // setting search button for new data

    }

    private void getWeather(String selectedElement, String selectedLocation, String selectedTime) {
        String authorization = "CWA-25C9C6F3-AA7A-4C7F-9E21-F227628CE53B";
        if(selectedElement.equals("All")) selectedElement=""; // if "All" is select , return empty string
        String finalSelectedElement = selectedElement; //store final element
        getApi.getWeatherApi(authorization,selectedLocation,finalSelectedElement)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WeatherResponse>() {
                    @Override
                    public void onNext(@NonNull WeatherResponse weatherResponse) {
                        binding.mainResultTv.setText(""); //clear textview to remove previous data
                        List time_list = Arrays.asList(time_data);// convert string to list
                        List element_list = Arrays.asList(element_data); // convert string to list
                        //check if user chose multiple element
                        if(weatherResponse.getElementSize() != 1){
                            for(int i=0 ; i<weatherResponse.getElementSize() ; i++){
                                //append each element to textview
                                binding.mainResultTv.append(tw_element[i] + weatherResponse.getDataByTime(i,time_list.indexOf(selectedTime))+"\n");
                            }
                        }
                        else {
                            //if single element is clicked , return it's data
                            binding.mainResultTv.setText(tw_element[element_list.indexOf(finalSelectedElement)] + weatherResponse.getDataByTime(0,time_list.indexOf(selectedTime))+"\n");
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


    private void getData(){
        location_data = getResources().getStringArray(R.array.location_data);
        element_data = getResources().getStringArray(R.array.element_data);
        time_data = getResources().getStringArray(R.array.time_data);
        tw_element = getResources().getStringArray(R.array.tw_element);
    }

    private void setDataBinding(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setView(this);

    }


    private void setSpinner() {
        //initialize adapter for each spinner
        ArrayAdapter location_adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                location_data
        );
        ArrayAdapter element_adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                element_data
        );
        ArrayAdapter time_adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                time_data
        );

        binding.mainLocationSpinner.setAdapter(location_adapter);
        binding.mainElementSpinner.setAdapter(element_adapter);
        binding.mainDaySpinner.setAdapter(time_adapter);
        //分別設定spinner的資料

        binding.mainLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_location = binding.mainLocationSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });//location的spinner點擊事件

        binding.mainElementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_element = binding.mainElementSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });//element的spinner點擊事件

        binding.mainDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_time = binding.mainDaySpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });//time的spinner點擊事件
    }
}