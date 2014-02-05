/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

import java.util.List;

import org.testng.annotations.Test;

public class WaterTreatmentPlantTest {

    @Test
    public void when_distributePipes_stations_are_set_with_the_correct_pipes() throws Exception {
        // GIVEN
        // WHEN
        WaterTreatmentPlant plant = new WaterTreatmentPlant("test", "3", "test", "4567");
        List<Station> stations = plant.getStations();

        // THEN
        Station station = stations.get(0);
        assert (station.getFirstPipe().getPipeId() == 0);
        assert (station.getSecondPipe().getPipeId() == stations.size() - 1);

        station = stations.get(1);
        assert (station.getFirstPipe().getPipeId() == 1);
        assert (station.getSecondPipe().getPipeId() == 0);
    }
}
