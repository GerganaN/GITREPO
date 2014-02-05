package com.skrill.interns.airplaneHornsClientServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;

public class ClientConnection extends Thread {
    private Socket connectionSocket;
    private BufferedWriter bufferOutToClient;
    private BufferedReader bufferInFromServer;
    private boolean isInterrupted;

    public ClientConnection(Socket connectionSocket) {
        this.connectionSocket = connectionSocket;
        this.isInterrupted = false;
    }

    private void setReadAndWriteBuffers() throws UnsupportedEncodingException, IOException {
        OutputStreamWriter outToClient = new OutputStreamWriter(connectionSocket.getOutputStream(),
                Charset.forName("UTF-8"));
        InputStreamReader inFromServer = new InputStreamReader(connectionSocket.getInputStream(),
                Charset.forName("UTF-8"));
        bufferOutToClient = new BufferedWriter(outToClient);
        bufferInFromServer = new BufferedReader(inFromServer);
    }

    private void getFactoryState() throws IOException {
        StringBuilder receivedMessage = new StringBuilder();
        String currentLine;
        while (!(currentLine = bufferInFromServer.readLine()).isEmpty()) {
            receivedMessage.append(currentLine);
        }
        TCPServer.response = receivedMessage.toString().trim();
        TCPServer.isFactoryOn = true;
    }

    private void respondToClient() throws IOException {
        String responseMessage = TCPServer.response;
        if (TCPServer.isFactoryOn) {
            bufferOutToClient.write("HTTP/1.1 200 OK\r\nContent-Type: text/xml; charset=UTF-8\r\nContent-Length: "
                    + responseMessage.length() + "\r\n" + "Connection: close\r\n\n" + responseMessage);
        } else {
            bufferOutToClient.write("HTTP/1.1 200 OK\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Length: "
                    + responseMessage.length() + "\r\n" + "Connection: close\r\n\n" + responseMessage);
        }
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

    private void checkRequest() throws IOException {
        String firstLine = bufferInFromServer.readLine();
        System.out.println(firstLine);
        if (firstLine.matches("POST / HTTP/1.1")) {
            return;
        }
        if (firstLine.matches("GET / HTTP/1.1")) {
            isInterrupted = true;
        }
    }

    public void run() {
        try {
            setReadAndWriteBuffers();
            checkRequest();
            if (!isInterrupted) {
                while (!connectionSocket.isClosed()) {
                    getFactoryState();
                }
            } else {
                respondToClient();
            }
        } catch (UnsupportedEncodingException e) {
            respondsErrorToClient("Type of encoding is not supported");
        } catch (IOException e) {
            respondsErrorToClient("ERROR");
        } finally {
            try {
                connectionSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}
