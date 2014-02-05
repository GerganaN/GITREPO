package com.skrill.interns.airplaneHornsClientServer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class UDPClient {
    private static final String SERVER_HOST = "10.129.2.39";
    private static final int PORT = 1024;

    private DatagramSocket clientSocket;
    private InetAddress serverIpAddress;
    private static byte[] sentData;

    public UDPClient() throws SocketException, UnknownHostException {
        clientSocket = new DatagramSocket();
        serverIpAddress = InetAddress.getByName(SERVER_HOST);
        sentData = new byte[128];
    }

    public void sendToServer(String message) {
        try {
            sentData = message.getBytes(Charset.forName("UTF-8"));
            DatagramPacket sendPacket = new DatagramPacket(sentData, sentData.length, serverIpAddress, PORT);
            clientSocket.send(sendPacket);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The encoding is not supported");
        } catch (IOException e) {
            System.err.println("Sorry, your message has not been sent");
        }
    }

    protected void finalize() {
        clientSocket.close();
    }
}
