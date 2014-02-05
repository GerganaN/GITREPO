package com.skrill.interns.airplaneHornsClientServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UDPServer {
    private static final int PORT = 1024;
    private static DatagramSocket serverSocket;
    private static Map<InetAddress, String> clientNames;

    static {
        try {
            Map<InetAddress, String> map = new HashMap<InetAddress, String>();
            map.put(InetAddress.getLocalHost(), "SOFIA : ");
            map.put(InetAddress.getByName("10.129.2.18"), "VARNA : ");
            map.put(InetAddress.getByName("10.129.2.63"), "VRACA : ");
            clientNames = map;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            serverSocket = new DatagramSocket(PORT);
            System.out.println("Waiting for clients.. ");
            byte[] receivedData = new byte[1024];
            while (true) {

                DatagramPacket receivePacket = new DatagramPacket(receivedData, receivedData.length);
                try {
                    serverSocket.receive(receivePacket);
                } catch (IOException ex) {
                    System.err.println("Packet has not been received");
                    ex.printStackTrace();
                }
                String received = new String(Arrays.copyOfRange(receivedData, 0, receivePacket.getLength()),
                        Charset.forName("UTF-8"));

                InetAddress address = receivePacket.getAddress();

                if (clientNames.containsKey(address)) {
                    System.out.println(clientNames.get(address) + received.trim());
                } else {
                    System.out.println(receivePacket.getAddress() + " " + received.trim());
                }
            }
        } catch (SocketException e) {
            System.err.println("Socket could not be opened");
            e.printStackTrace();
        }
    }
}
