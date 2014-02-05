/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.weatherForecast;

import java.util.List;
import java.util.Map;

public class ForecastDatabaseService implements Runnable {

    private List<String> cities;
    private ForecastExtractor extractor;
    private Map<String, Forecast> forecastDatabase;

    public ForecastDatabaseService(Map<String, Forecast> forecastDatabase, List<String> cities) {
        this.extractor = new ForecastExtractor();
        this.forecastDatabase = forecastDatabase;
        this.cities = cities;
    }

    public void fillDatabase() {
        for (String city : cities) {
            forecastDatabase.put(city, extractor.getForecast(city));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                fillDatabase();
                Thread.sleep(5 * 60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
