/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "plant")
public class PlantState {

    @XmlElement
    private String plantName;
    @XmlElementWrapper
    private Map<Integer, StationState> stationStates;

    public PlantState() {
        plantName = "";
    }

    public PlantState(String plantName) {
        this.plantName = plantName;
        stationStates = new HashMap<Integer, StationState>();
    }

    public String getPlantName() {
        return plantName;
    }

    public Map<Integer, StationState> getStationStates() {
        return stationStates;
    }

    public void update(StationState state) {
        stationStates.put(state.getStationId(), state);
    }
}
