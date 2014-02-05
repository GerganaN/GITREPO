package com.skrill.interns.weather;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "day", "date", "temperature", "weatherCondition", "wind" })
public class Forecast {

    private String day;
    private String date;
    private String temperature;
    private String weatherCondition;
    private String wind;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
