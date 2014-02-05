/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

import java.util.HashMap;
import java.util.Map;

public class Database {

    private Map<String, PlantState> stateMap;

    public Database() {
        stateMap = new HashMap<String, PlantState>();
    }

    public PlantState get(String city) {
        if (stateMap.containsKey(city)) {
            return stateMap.get(city);
        }
        return null;
    }

    public void update(StationState stationState) {
        PlantState plantState;
        if (stateMap.containsKey(stationState.getPlantName())) {
            plantState = stateMap.get(stationState.getPlantName());
            plantState.update(stationState);
        } else {
            plantState = new PlantState(stationState.getPlantName());
            plantState.update(stationState);
            stateMap.put(stationState.getPlantName(), plantState);
        }
    }
}
