package com.skrill.interns.weather;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static Map<String, String> cachedForecast = new HashMap<String, String>();

    public static Map<String, String> getCachedForecasts() {
        return cachedForecast;
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(2000);
            while (true) {
                System.out.println("Waiting for clients to connect...");
                Socket clientSocket = serverSocket.accept();
                System.out.print("Client has connected!\n");
                ClientConnection newConnection = new ClientConnection(clientSocket);
                newConnection.start();
            }
        } catch (SocketException e) {
            System.err.println("Server could not start!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
