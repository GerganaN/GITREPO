/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.weatherForecast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForecastServer {

    public static void main(String[] args) throws IOException {
        List<String> cities = new ArrayList<String>();
        Map<String, Forecast> forecastDatabase = new HashMap<String, Forecast>();
        ServerSocket serverListener = new ServerSocket(1234);

        fillCities(cities);

        new Thread(new ForecastDatabaseService(forecastDatabase, cities)).start();
        ExecutorService pool = Executors.newCachedThreadPool();

        while (true) {
            Socket clientConnection = serverListener.accept();
            pool.execute(new HTTPClientServiceThread(clientConnection, forecastDatabase));
        }
    }

    public static void fillCities(List<String> cities) {
        cities.add("sofia");
        cities.add("plovdiv");
        cities.add("varna");
        cities.add("burgas");
        cities.add("london");
        cities.add("paris");
    }
}
