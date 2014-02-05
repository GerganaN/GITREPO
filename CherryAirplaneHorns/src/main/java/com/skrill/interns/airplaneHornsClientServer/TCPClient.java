package com.skrill.interns.airplaneHornsClientServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.skrill.interns.airplaneHorns.FactoryState;

public class TCPClient {
    private static final String SERVER_HOST = "localhost";
    private static final int PORT = 10000;
    private Socket clientSocket;
    private BufferedWriter bufferOutToServer;
    private Marshaller jaxbMarshaller;

    public TCPClient() throws IOException, UnknownHostException {
        clientSocket = new Socket(SERVER_HOST, PORT);
        OutputStreamWriter outToServer = new OutputStreamWriter(clientSocket.getOutputStream(), Charset.forName("UTF-8"));
        bufferOutToServer = new BufferedWriter(outToServer);
        setJAXBSerializator();
    }

    private void setJAXBSerializator() {
        try {
            JAXBContext context = JAXBContext.newInstance(FactoryState.class);
            jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        } catch (JAXBException e) {
            System.err.println("Cannot serialize the data");
        }
    }

    private String serializeData(FactoryState data) {
        StringWriter serializedData = new StringWriter();
        try {
            // if there was an exception when this object was created
            if (jaxbMarshaller == null) {
                return "";
            }
            jaxbMarshaller.marshal(data, serializedData);
        } catch (JAXBException e) {
            return "";
        }
        return serializedData.toString();
    }

    public void sendToServer(FactoryState state) {
        try {
            String sentMessage = serializeData(state);
            bufferOutToServer.write("POST / HTTP/1.1\n\n");
            bufferOutToServer.write(sentMessage);
            bufferOutToServer.write("\n");
            bufferOutToServer.flush();
        } catch (IOException e) {
            System.err.println("Could not sent the message to the server");
        }
    }
}
