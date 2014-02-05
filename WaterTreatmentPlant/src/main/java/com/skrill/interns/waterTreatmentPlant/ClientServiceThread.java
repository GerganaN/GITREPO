/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonParseException;

public class ClientServiceThread implements Runnable {

    private Socket connection;
    private InputStreamReader inputStream;
    private OutputStreamWriter outputStream;
    private BufferedWriter outputBuffer;
    private BufferedReader inputBuffer;
    private Database database;
    private Map<String, String> headersMap;
    private Map<String, String> parametersMap;
    private Converter converter;

    public ClientServiceThread(Socket clientConnection, Database database) {
        this.connection = clientConnection;
        this.database = database;
        headersMap = new HashMap<String, String>();
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
            requestResponseSender();
        }
    }

    public void requestResponseSender() {
        try {
            String requestLine = inputBuffer.readLine();
            readHeaders();
            String body = readBody();
            if (requestLine == null) {
                connection.close();
                return;
            }
            String resource = parseRequest(requestLine);
            if ("/state".equalsIgnoreCase(resource)) {
                if (!("".equalsIgnoreCase(body))) {
                    StationState state = converter.jsonToStationState(body);
                    database.update(state);
                    sendResponseToClient(200, "OK", "text/plain", "");
                }
            } else {
                getParameters(resource);
                sendToClient();
            }
        } catch (JsonParseException jsonex) {
            sendResponseToClient(400, "Bad Request", "text/plain", "");
        } catch (SocketException s) {

            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendToClient() {
        String city = parametersMap.get("city");
        if (city != null) {
            PlantState plantState = database.get(city);
            if (plantState != null) {
                String format = parametersMap.get("format");
                if ("XML".equalsIgnoreCase(format)) {
                    String plantStateMessage = converter.toXML(plantState);
                    sendResponseToClient(200, "OK", "application/xml", plantStateMessage);
                } else {
                    String plantStateMessage = converter.toJson(plantState);
                    sendResponseToClient(200, "OK", "application/json", plantStateMessage);
                }

            } else {
                sendResponseToClient(404, "Not Found", "text/plain", "No such city");
            }
        } else {
            sendResponseToClient(404, "Not Found", "text/plain", "Wrong parameters");
        }
    }

    public void sendResponseToClient(int status, String reason, String type, String message) {
        try {
            outputBuffer.write("HTTP/1.1 " + status + " " + reason + "\r\nContent-Type:" + type
                    + ";charset=utf-8\r\nContent-Length: " + message.length() + "\r\nConnection: close\r\n" + "\r\n"
                    + message);
            outputBuffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String parseRequest(String requestLine) {
        String[] requestParts = requestLine.split(" ");
        String resource = requestParts[1];
        return resource;
    }

    public void getParameters(String resource) {
        try {
            resource = URLDecoder.decode(resource, "UTF-8");
            String[] resourceString = resource.split("\\?", 2);
            String[] parametersParts = resourceString[1].split("&");
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

    public void readHeaders() {
        try {
            int numberOfNewLines = 0;
            while (numberOfNewLines < 1) {
                String line = inputBuffer.readLine();
                if ("".equalsIgnoreCase(line)) {
                    numberOfNewLines++;
                } else if (line != null) {
                    String[] headerParts = line.split(": ");
                    headersMap.put(headerParts[0], headerParts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readBody() {
        Integer contentLength = 0;
        try {
            contentLength = Integer.valueOf(headersMap.get("Content-Length"));
        } catch (NumberFormatException ex) {
            return "";
        }

        char[] bodyBuffer = new char[contentLength];
        String body = "";
        try {
            inputBuffer.read(bodyBuffer);
            body = new String(bodyBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}
