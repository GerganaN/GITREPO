package com.skrill.interns.airplaneHornsClientServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {
    private static final int PORT = 10000;
    private static ServerSocket serverSocket;
    static boolean isFactoryOn = false;
    static String response = "AirplaneHorns Fabrik is not working at the moment ";

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
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
