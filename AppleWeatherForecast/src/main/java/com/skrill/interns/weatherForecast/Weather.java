/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.weatherForecast;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Weather {
    @XmlElement(name = "date")
    private String date;
    @XmlElement(name = "tempMaxC")
    private String tempMaxCelsius;
    @XmlElement(name = "tempMinC")
    private String tempMinCelsius;
    @XmlElement(name = "windspeedKmph")
    private String windSpeedKmph;
    @XmlElement(name = "winddir16Point")
    private String windDirection;
    @XmlElement(name = "weatherDesc")
    private String weatherDesc;

    public Weather() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTempMaxC() {
        return tempMaxCelsius;
    }

    public void setTempMaxCelsius(String tempMaxCelsius) {
        this.tempMaxCelsius = tempMaxCelsius;
    }

    public String getTempMinCelsius() {
        return tempMinCelsius;
    }

    public void setTempMinCelsius(String tempMinCelsius) {
        this.tempMinCelsius = tempMinCelsius;
    }

    public String getWindspeedKmph() {
        return windSpeedKmph;
    }

    public void setWindspeedKmph(String windspeedKmph) {
        this.windSpeedKmph = windspeedKmph;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String winddirection) {
        this.windDirection = winddirection;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    @Override
    public String toString() {
        return "Weather on " + date + "\n\tMax temp C: " + tempMaxCelsius + "\n\tMin temp C: " + tempMinCelsius
                + "\n\tWind speed kmph: " + windSpeedKmph + "\n\tWind direction: " + windDirection + "\n\tCondition: "
                + weatherDesc;
    }

}
