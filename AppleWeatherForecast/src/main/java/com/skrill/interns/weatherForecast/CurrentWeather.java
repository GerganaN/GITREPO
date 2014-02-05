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
public class CurrentWeather extends Weather {
    @XmlElement(name = "temp_C")
    private String tempCelsius;

    public CurrentWeather() {
    }

    public String getTempCelsius() {
        return tempCelsius;
    }

    public void setTempCelsius(String tempCelsius) {
        this.tempCelsius = tempCelsius;
    }

    @Override
    public String toString() {
        return "CurrentWeather:" + "\n\tCurrent temp C: " + tempCelsius + "\n\tWind speed kmph: "
                + this.getWindspeedKmph() + "\n\tWind direction: " + this.getWindDirection()
                + "\n\tCurrent condition: " + this.getWeatherDesc();
    }

}
