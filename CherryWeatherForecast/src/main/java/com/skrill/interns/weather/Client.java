package com.skrill.interns.weather;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static final int TIMEOUT = 10000;

    public static void main(String args[]) {

        try {
            socket = new Socket("localhost", 2000);
            socket.setSoTimeout(TIMEOUT);

            OutputStream out = socket.getOutputStream();
            PrintStream printStream = new PrintStream(out, true, "UTF-8");

            // enter city and number of days
            Scanner console = new Scanner(System.in);
            System.out.println("Enter city: ");
            printStream.println(console.nextLine());
            System.out.println("Enter number of days (1-3): ");
            printStream.println(console.nextLine());

            // received Data from the server
            InputStream inputStream = socket.getInputStream();
            byte[] receivedData = new byte[1024];
            inputStream.read(receivedData);

            ByteBuffer bufferedReceivedData = ByteBuffer.allocate(receivedData.length);
            bufferedReceivedData.put(receivedData);
            String forecast = new String(bufferedReceivedData.array());
            System.out.println(forecast.trim());
            socket.close();

        } catch (UnknownHostException e) {
            System.err.println("Unknown IP Address!");
        } catch (SocketException e) {
            System.err.println("Socket is closed!");
        } catch (SocketTimeoutException e) {
            System.err.println("Timetout expired!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
