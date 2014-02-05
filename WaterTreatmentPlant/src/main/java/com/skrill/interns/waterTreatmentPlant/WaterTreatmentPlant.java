/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaterTreatmentPlant {

    private String plantName;
    private int numberOfStations;
    private List<Station> stations;
    private List<Pipe> pipes;
    private ExecutorService executor;
    private String hostAddress;
    private String port;

    public WaterTreatmentPlant(String hostAddress, String port, String numberOfStationsAndPipes, String plantName)
            throws IllegalArgumentException {

        this.plantName = plantName;
        this.hostAddress = hostAddress;
        this.port = port;
        numberOfStations = convertNumberOfStationsAndPipes(numberOfStationsAndPipes);
        stations = new ArrayList<Station>();
        pipes = new ArrayList<Pipe>();
        executor = Executors.newFixedThreadPool(numberOfStations);
        createPipes(numberOfStations);
        createStations(numberOfStations);
        distributePipes();
    }

    public int convertNumberOfStationsAndPipes(String numberOfStationsAndPipes) throws IllegalArgumentException {
        int number;
        number = Integer.parseInt(numberOfStationsAndPipes);
        if (number < 2) {
            throw new IllegalArgumentException();
        }
        return number;
    }

    public void createPipes(int numberOfPipes) {
        for (int i = 0; i < numberOfPipes; i++) {
            pipes.add(new Pipe(i));
        }
    }

    public List<Pipe> getPipes() {
        return pipes;
    }

    public void createStations(int numberOfStationsToCreate) {
        for (int i = 0; i < numberOfStationsToCreate; i++) {
            stations.add(new Station(i, this.plantName, hostAddress, port));
        }
    }

    public List<Station> getStations() {
        return stations;
    }

    public void distributePipes() {
        Station station = stations.get(0);
        station.setFirstPipe(pipes.get(0));
        station.setSecondPipe(pipes.get(pipes.size() - 1));
        for (int i = 1; i < stations.size(); i++) {
            station = stations.get(i);
            // Get the pipe with the same number and the previous pipe
            station.setFirstPipe(pipes.get(i));
            station.setSecondPipe(pipes.get(i - 1));
        }
    }

    public void startStations() {
        for (Station station : stations) {
            station.setPower(true);
            executor.execute(station);
        }
    }

    public void stopStations() {
        System.out.println(plantName + " plant is shutting down stations.");
        // Shutdown the stations
        for (Station station : stations) {
            station.setPower(false);
        }
    }

    public void startPlant(int timeToWork) {
        try {
            startStations();
            Thread.sleep(timeToWork * 1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopPlant() {
        stopStations();
        executor.shutdown();
        System.out.println(plantName + " plant is stopped.");
    }
}
