package com.skrill.interns.weather;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientConnection extends Thread {

    private static final int TIMEOUT = 10000;
    private Socket connectionSocket;
    private BufferedWriter bufferOutToClient;
    private BufferedReader bufferInFromClient;
    private static List<String> mostSearchedCities;
    private Pattern pattern;
    private Matcher matcher;

    static {
        List<String> list = new ArrayList<String>();
        list.add("SOFIA");
        list.add("VRACA");
        list.add("VARNA");
        mostSearchedCities = list;
    }

    private void setReadAndWriteBuffers() throws UnsupportedEncodingException, IOException {
        OutputStreamWriter outToClient = new OutputStreamWriter(connectionSocket.getOutputStream(), Charset.forName("UTF-8"));
        InputStreamReader inFromClient = new InputStreamReader(connectionSocket.getInputStream(), Charset.forName("UTF-8"));
        bufferOutToClient = new BufferedWriter(outToClient);
        bufferInFromClient = new BufferedReader(inFromClient);
    }

    private Map<String, String> parseParameters(String receivedMessage) {
        Map<String, String> parametersKeyValue = new HashMap<String, String>();
        matcher = pattern.matcher(receivedMessage);
        if (matcher.matches()) {
            String[] requestElements = receivedMessage.split(" ");
            if (requestElements.length == 3) {
                String[] requestParameters = requestElements[1].split("\\?");
                if (requestParameters.length == 2) {
                    String[] KeyValueArray = requestParameters[1].split("&");
                    for (int i = 0; i < KeyValueArray.length; i++) {
                        String[] keyValue = KeyValueArray[i].split("=");
                        if (keyValue.length == 2) {
                            parametersKeyValue.put(keyValue[0], keyValue[1]);
                        }
                    }
                    return parametersKeyValue;
                }
            }
        }
        return null;
    }

    private void respondsToClient() throws IOException {
        String receivedMessage = bufferInFromClient.readLine();
        Map<String, String> parametersKeyValue = parseParameters(receivedMessage);
        if (parametersKeyValue == null) {
            String errorMessage = "<html><head><meta charset=\"utf-8\"/></head><body><h1 style=\"color:red\">ERROR 400: Bad Request</h1> Incorrect set parameters</body></html>";
            respondsErrorToClient("HTTP/1.1 400 Bad Request\r\n" + "Content-Type: text/html; charset=UTF-8\r\n" + "Content-Length: "
                    + errorMessage.length() + "\r\n" + "Connection: close\r\n" + "\r\n" + errorMessage);
            return;
        }
        // get parameter city if it has been set
        String city = parametersKeyValue.get("city");
        if (city == null) {
            String errorMessage = "<html><head><meta charset=\"utf-8\"/></head><body><h1 style=\"color:red\">ERROR 400: Bad Request</h1> City is not set</body></html>";
            respondsErrorToClient("HTTP/1.1 400 Bad Request\r\n" + "Content-Type: text/html; charset=UTF-8\r\n" + "Content-Length: "
                    + errorMessage.length() + "\r\n" + "Connection: close\r\n" + "\r\n" + errorMessage);
            return;
        }
        city = city.toUpperCase();
        // check if the city is supported
        if (!(WeatherForecast.getCities().containsKey(city))) {
            String errorMessage = "<html><head><meta charset=\"utf-8\"/></head><body><h1 style=\"color:red\">ERROR 400: Bad Request</h1> City is not supported!</body></html>";
            respondsErrorToClient("HTTP/1.1 400 Bad Request\r\n" + "Content-Type: text/html; charset=UTF-8\r\n" + "Content-Length: "
                    + errorMessage.length() + "\r\n" + "Connection: close\r\n" + "\r\n" + errorMessage);
            return;
        }

        // get parameter day if it has been set or set default day in other case
        int days;
        String daysString = parametersKeyValue.get("days");
        if (daysString == null) {
            days = 1;
        } else {
            try {
                days = Integer.parseInt(daysString);
            } catch (NumberFormatException e) {
                String errorMessage = "<html><head><meta charset=\"utf-8\"/></head><body><h1 style=\"color:red\">ERROR 400: Bad Request</h1> Days are not set correctly</body></html>";
                respondsErrorToClient("HTTP/1.1 400 Bad Request\r\n" + "Content-Type: text/html; charset=UTF-8\r\n" + "Content-Length: "
                        + errorMessage.length() + "\r\n" + "Connection: close\r\n" + "\r\n" + errorMessage);
                return;
            }
        }
        if (days < 1 || days > 3) { // check if the days are correct
            days = 1;
        }

        // get parameter format if it has been set or set default day in other
        // case
        String format = parametersKeyValue.get("format");
        if (format == null && !("JSON".equals(format) || "XML".equals(format))) {
            format = "default";
        } else {
            format = parametersKeyValue.get("format").toUpperCase();
        }
        //
        String cacheMapKey = city + "_" + days + "_" + format;
        String responseMessage;
        if (Server.getCachedForecasts().containsKey(cacheMapKey)) {
            responseMessage = Server.getCachedForecasts().get(cacheMapKey);
            System.out.println("Got forecast from cache");
        } else {
            WeatherForecast forecast = new WeatherForecast();
            responseMessage = forecast.getWeather(city, days, format);
            if (mostSearchedCities.contains(city)) {
                Server.getCachedForecasts().put(cacheMapKey, responseMessage);
            }
            System.out.println("Got forecast from weather station");
        }
        bufferOutToClient.write("HTTP/1.1 200 OK\r\nContent-Type: text/" + format + "; charset=UTF-8\r\nContent-Length: "
                + responseMessage.getBytes(Charset.forName("UTF-8")).length + "\r\n" + "Connection: close\r\n\n" + responseMessage);
        bufferOutToClient.flush();
    }

    private void respondsErrorToClient(String errorMessage) {
        try {
            bufferOutToClient.write(errorMessage + "\n");
            bufferOutToClient.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientConnection(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
        pattern = Pattern.compile("GET .*? HTTP/1.1");
        try {
            connectionSocket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            System.err.println("Could not set SoTimeout!");
        }
    }

    public void run() {
        try {
            setReadAndWriteBuffers();
            respondsToClient();
        } catch (UnsupportedEncodingException e) {
            respondsErrorToClient("Type of encoding is not supported");
        } catch (IOException e) {
            String errorMessage = "<html><head><meta charset=\"utf-8\"/></head><body><h1 style=\"color:red\">ERROR 408: Request Timeout</h1></body></html>";
            respondsErrorToClient("HTTP/1.1 408 Request Timeout\r\nContent-Type: text/html; charset=UTF-8\r\nContent-Length: "
                    + errorMessage.length() + "\r\nConnection: close\r\n\n" + errorMessage);
        } finally {
            try {
                connectionSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
