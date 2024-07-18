package com.example.weatherapi;

import java.util.List;

public interface Contract {
    interface View{
        void setTextNull();
        void getData();
        String[] getTimeData();
        String[] getElementData();
        void setMultipleResultTextView(WeatherResponse weatherResponse ,
                                       List time_list  , String selectedTime);
        void setSingleResultTextView(WeatherResponse weatherResponse ,
                                     List element_list  , List time_list,
                                     String finalSelectedElement ,String selectedTime );
    }
    interface Presenter{
        void getWeather(String selectedElement, String selectedLocation, String selectedTime);
    }

}
