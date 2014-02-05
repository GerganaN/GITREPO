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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class StatisticsLogger {
    private int port;
    private BufferedWriter outputBuffer;
    private BufferedReader inputBuffer;
    private String serverAddress;
    private Map<String, String> headersMap;
    Socket clientSocket;

    public StatisticsLogger(String serverAddress, String serverPort) {
        port = convertPort(serverPort);
        this.serverAddress = serverAddress;
        headersMap = new HashMap<String, String>();
        try {
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(serverAddress, port), 3000);
            outputBuffer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
            inputBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException socketEx) {
            socketEx.printStackTrace();
        } catch (NumberFormatException numFormatEx) {
            numFormatEx.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkLastResponseCode() {
        String requestLine = "";
        try {
            requestLine = inputBuffer.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readHeaders();
        // String body = readBody();
        System.out.println(requestLine);
    }

    public void sendInfo(String message) {
        try {
            String finalMessage = "POST /state HTTP/1.1\r\nHost: " + serverAddress + "\r\nContent-Length: "
                    + message.length() + "\r\nConnection: close\r\n\r\n" + message;
            outputBuffer.write(finalMessage);
            outputBuffer.flush();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        checkLastResponseCode();
    }

    private int convertPort(String portString) throws NumberFormatException {
        int portNumber;
        portNumber = Integer.parseInt(portString);
        if (portNumber < 0 || portNumber > 64000) {
            portNumber = -1;
        }
        return portNumber;
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

        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        } catch (IOException e) {
            return;
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

    public String parseRequest(String requestLine) {
        String[] requestParts = requestLine.split(" ");
        String resource = requestParts[1];
        return resource;
    }

}
