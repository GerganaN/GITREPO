package com.skrill.interns.weather;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "weather")
public class Weather {

    // XmlElement sets the name of the entities
    @XmlElement(name = "forecast")
    private List<Forecast> forecasts;

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    @Override
    public String toString() {
        StringBuilder weather = new StringBuilder();
        for (int i = 0; i < forecasts.size(); i++) {
            weather.append(forecasts.get(i).getDay() + "\n");
            weather.append(forecasts.get(i).getDate() + "\n");
            weather.append(forecasts.get(i).getTemperature() + "\n");
            weather.append(forecasts.get(i).getWeatherCondition() + "\n");
            weather.append(forecasts.get(i).getWind() + "\n");
            weather.append("\n\n");
        }
        return weather.toString();
    }
}
