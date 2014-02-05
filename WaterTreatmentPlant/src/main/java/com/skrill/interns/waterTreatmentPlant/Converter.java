/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

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
            jaxbContext = JAXBContext.newInstance(PlantState.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            System.out.println("Jaxb");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("NUll");
        }
    }

    public String toJson(PlantState plantState) {
        return gson.toJson(plantState);
    }

    public String toXML(PlantState plantState) {
        try {
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(plantState, writer);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public StationState jsonToStationState(String json) {
        StationState state = gson.fromJson(json, StationState.class);
        return state;
    }
}
