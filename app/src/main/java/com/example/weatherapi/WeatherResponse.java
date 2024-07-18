package com.example.weatherapi;

import java.util.List;

public class WeatherResponse {
    public Records records;

    public class Records{
        public List<Location> location;

    }
    public class Location{
        public String locationName;
        public List<WeatherElement> weatherElement;

        @Override
        public String toString() {
            return "Location{" +
                    "weatherElement=" + weatherElement +
                    '}';
        }
    }
    public class WeatherElement{
        public String elementName;
        public List<Time> time;
    }
    public class Time{
        public Parameter parameter;
    }
    public class Parameter{
        public String parameterName;
        public String parameterUnit;
    }
    public String getDataByTime(Integer index,Integer day){
        return records.location.get(0).weatherElement.get(index).time.get(day).parameter.parameterName;
    }
    public String getUnitByTime(Integer index){
        return records.location.get(0).weatherElement.get(0).time.get(index).parameter.parameterUnit;
    }
    public Integer getElementSize(){
        return records.location.get(0).weatherElement.size();
    }
}