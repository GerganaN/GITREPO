/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.weatherForecast;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;

public class Converter {
    private Gson gson;
    private StringWriter writer = new StringWriter();
    private JAXBContext jaxbContext;
    private Marshaller jaxbMarshaller;

    public Converter() {
        gson = new Gson();
        try {
            jaxbContext = JAXBContext.newInstance(Forecast.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            System.out.println("Jaxb");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("NUll");
        }
    }

    public String toString(Forecast forecast) {
        StringBuilder sb = new StringBuilder("");

        try {
            sb.append(forecast.getCurrentWeather().toString()).append("\n");
            for (Weather w : forecast.getWeather()) {
                sb.append(w.toString()).append("\n");
            }
        } catch (NullPointerException ex) {
            return "";
        }
        return sb.toString();
    }

    public String toJson(Forecast forecast) {
        return gson.toJson(forecast);
    }

    public String toXML(Forecast forecast) {
        try {
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(forecast, writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}
