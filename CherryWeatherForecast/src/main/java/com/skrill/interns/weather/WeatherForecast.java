package com.skrill.interns.weather;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WeatherForecast {

    private static String[] dayHTMLClasses = { ".wfTodayContent", ".wfTomorrowContent", ".wfDayAfterTomorrowContent" };
    private static Map<String, String> cities;

    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("SOFIA", "http://sinoptik.bg/sofia-bulgaria-100727011");
        map.put("VRACA", "http://sinoptik.bg/vratsa-bulgaria-100725712");
        map.put("VARNA", "http://sinoptik.bg/varna-bulgaria-100726050");
        map.put("YAMBOL", "http://sinoptik.bg/yambol-bulgaria-100725578");
        map.put("MONTANA", "http://sinoptik.bg/montana-bulgaria-100729114");
        map.put("BURGAS", "http://sinoptik.bg/burgas-bulgaria-100732770");
        map.put("PLOVDIV", "http://sinoptik.bg/plovdiv-bulgaria-100728193");
        cities = map;
    }

    public static Map<String, String> getCities() {
        return cities;
    }

    public String getWeather(String city, int numberOfDays, String format) throws IOException {
        String url = cities.get(city);
        Document sinoptik = Jsoup.connect(url).get();
        Weather weather = createWeather(sinoptik, numberOfDays);
        if ("JSON".equals(format)) {
            return getJsonForecast(weather);
        }
        if ("XML".equals(format)) {
            try {
                return getWeatherForecastAsXML(weather);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        return weather.toString();
    }

    private Forecast createForecast(Document url, String className) {
        String day = url.select(className + " .wfNonCurrentDay").text();
        String date = url.select(className + " .wfNonCurrentDate").text();
        String temperature = url.select(className + " .wfNonCurrentTemp").text();
        String weatherCondition = url.select(className + " .wfNonCurrentCond").text();
        String wind = url.select(className + " .wfNonCurrentWind ").text();

        Forecast forecast = new Forecast();
        forecast.setDay(day);
        forecast.setDate(date);
        forecast.setTemperature(temperature);
        forecast.setWeatherCondition(weatherCondition);
        forecast.setWind(wind);
        return forecast;
    }

    public Weather createWeather(Document url, int numberOfDays) {
        List<Forecast> forecasts = new ArrayList<Forecast>();
        for (int i = 0; i < numberOfDays; i++) {
            forecasts.add(createForecast(url, dayHTMLClasses[i]));
        }
        Weather weather = new Weather();
        weather.setForecasts(forecasts);
        return weather;
    }

    public String getWeatherForecastAsXML(Weather weather) throws JAXBException {
        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(Weather.class);
        Marshaller marshal = context.createMarshaller();
        marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshal.marshal(weather, sw);

        return sw.toString();
    }

    public String getJsonForecast(Weather weather) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(weather);

        return json;
    }
}
