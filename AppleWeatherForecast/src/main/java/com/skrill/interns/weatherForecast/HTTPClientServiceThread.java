/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.weatherForecast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HTTPClientServiceThread implements Runnable {

    private BufferedWriter outputBuffer;
    private BufferedReader inputBuffer;
    private Socket connection;
    private InputStreamReader inputStream;
    private OutputStreamWriter outputStream;
    private ForecastExtractor extractor;
    private Map<String, Forecast> forecastDatabase;
    private String filePath;
    private String resourceFromRequest;
    private Map<String, String> parametersMap;
    private Converter converter;

    public HTTPClientServiceThread(Socket clientConnection, Map<String, Forecast> forecastDatabase) {
        this.connection = clientConnection;
        this.forecastDatabase = forecastDatabase;
        extractor = new ForecastExtractor();
        filePath = getClass().getResource("index.html").getPath();
        resourceFromRequest = "";
        parametersMap = new HashMap<String, String>();
        converter = new Converter();

        try {
            connection.setSoTimeout(200 * 1000);
            inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");
            outputStream = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            outputBuffer = new BufferedWriter(outputStream);
            inputBuffer = new BufferedReader(inputStream);

        } catch (SocketException e) {
            System.out.println("Connection failed");
            try {
                connection.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!connection.isClosed()) {
            String requestLine = readRequest();
            handleRequest(requestLine);
        }
    }

    private Forecast checkCityForecast(String cityToCheck) {
        Forecast forecast = null;
        String cityKey = cityToCheck.toLowerCase();
        if (forecastDatabase.containsKey(cityKey)) {
            forecast = forecastDatabase.get(cityKey);
        } else {
            forecast = extractor.getForecast(cityToCheck);
        }
        return forecast;
    }

    private void sendForecastToClient(String message) {
        try {
            outputBuffer.write(message);
            outputBuffer.flush();
        } catch (SocketTimeoutException e) {
            System.out.println("Client not found");
            e.printStackTrace();
        } catch (SocketException e) {
            System.out.println("Socket exception");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }

    private String readRequest() {
        String message = "";
        try {
            message = inputBuffer.readLine();

            // Cleaning the buffer
            while (!"".equals(inputBuffer.readLine())) {
            }
        } catch (IOException e) {
            System.out.println("Connection aborted");
            try {
                connection.close();
            } catch (IOException e1) {
                System.out.println("Cannot close socket");
            }
        }
        return message;
    }

    private void handleRequest(String query) {
        String[] splitQuery;
        String finalMessage = "";
        if ("".equals(query)) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        splitQuery = query.split(" ", 3);
        resourceFromRequest = splitQuery[1];

        if ("/".equals(resourceFromRequest)) {
            finalMessage = sendHTMLDocument();
        } else {
            finalMessage = sendRequestedForecast();
        }
        sendForecastToClient(finalMessage);
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String sendRequestedForecast() {
        String city = "";
        String format = "";
        Forecast forecast = null;
        String finalMessage = "";
        String message = "";

        getParameters();

        city = parametersMap.get("city");
        if (city != null) {
            forecast = checkCityForecast(city);
        }

        if (forecast != null) {
            format = parametersMap.get("format");
            message = chooseStructuredData(format, forecast);
        }
        if ("".equals(message)) {
            String urlNotFound = "URL not found";
            finalMessage = createResponseMessage(404, "URL not found", "plain", urlNotFound);
        } else {
            finalMessage = createResponseMessage(200, "OK", format, message);
        }
        return finalMessage;
    }

    private void getParameters() {
        try {
            resourceFromRequest = URLDecoder.decode(resourceFromRequest, "UTF-8");
            String[] resourceParts = resourceFromRequest.split("\\?", 2);
            String[] parametersParts = resourceParts[1].split("&");
            for (String string : parametersParts) {
                String[] parameters = string.split("=");
                if (parameters.length == 2) {
                    parametersMap.put(parameters[0], parameters[1]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String chooseStructuredData(String format, Forecast forecast) {
        if ("json".equalsIgnoreCase(format)) {
            return converter.toJson(forecast);
        } else if ("xml".equalsIgnoreCase(format)) {
            return converter.toXML(forecast);
        }
        return converter.toString(forecast);
    }

    private String sendHTMLDocument() {
        String finalMessage = "";
        if ("/".equals(resourceFromRequest)) {
            FileReader fileReader;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                fileReader = new FileReader(filePath);
                BufferedReader br = new BufferedReader(fileReader);
                String oneLine = null;

                while ((oneLine = br.readLine()) != null) {
                    stringBuilder.append(oneLine);
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            String htmlString = stringBuilder.toString();

            finalMessage = createResponseMessage(200, "OK", "html", htmlString);
        }
        return finalMessage;
    }

    private String createResponseMessage(int code, String reason, String type, String message) {
        String typeHeader = "";
        if ("xml".equalsIgnoreCase(type) || "json".equalsIgnoreCase(type)) {
            typeHeader = "application/" + type;
        } else {
            typeHeader = "text/" + type;
        }
        return "HTTP/1.1 " + code + " " + reason + "\r\nContent-Type: " + typeHeader
                + ";charset=utf-8\r\nContent-Length: " + message.length() + "\r\nConnection: close\r\n" + "\r\n"
                + message;
    }
}
