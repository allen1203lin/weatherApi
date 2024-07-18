package com.example.weatherapi;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity implements Contract.View{
    private ActivityMainBinding binding;
    private String[] location_data,element_data,time_data,tw_element;
    private String selected_location,selected_element,selected_time;
    private getAPI getApi;
    private ApiClient apiClient;
    private Contract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataBinding(); //set up databinding
        getData(); // load spinner data from resource
        getTimeData();
        getElementData();
        setSpinner();

        //presenter.getData();
        apiClient = new ApiClient();
        getApi = apiClient.myWeatherApi().create(getAPI.class);
        presenter = new Presenter(getApi ,this);
        binding.mainSearchButton.setOnClickListener(view -> presenter.getWeather(selected_element,
                selected_location,selected_time)); // setting search button for new data

    }
    public String[] getTimeData(){
        return time_data = getResources().getStringArray(R.array.time_data);
    }
    public String[] getElementData(){
     return element_data = getResources().getStringArray(R.array.element_data);
    }
    public void setMultipleResultTextView(WeatherResponse weatherResponse ,
                                         List time_list  , String selectedTime){
        for(int i=0 ; i<weatherResponse.getElementSize() ; i++){
            //append each element to textview
            binding.mainResultTv.append(tw_element[i] + weatherResponse.getDataByTime(i,time_list.indexOf(selectedTime))+"\n");
        }
    }
    public void setSingleResultTextView(WeatherResponse weatherResponse ,
                                        List element_list  , List time_list,
                                        String finalSelectedElement ,String selectedTime ){
        binding.mainResultTv.setText(tw_element[element_list.indexOf(finalSelectedElement)]+ weatherResponse.getDataByTime(0,time_list.indexOf(selectedTime))+"\n");
    }

    public void getData(){
        location_data = getResources().getStringArray(R.array.location_data);


        tw_element = getResources().getStringArray(R.array.tw_element);
    }
    private void setDataBinding(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.setView(this);
    }
    public void setTextNull(){
        binding.mainResultTv.setText("");
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