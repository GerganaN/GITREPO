package com.skrill.interns.airplaneHorns;

import java.util.List;
import java.util.Random;

public abstract class Machine extends MachineState implements Runnable {
    protected int productionTime;
    protected int firstPart;
    protected int secondPart;
    protected int producedPart;
    protected List<Station> stations;
    protected static Random rand = new Random();
    public Machine(List<Station> stations, int productionTime, String name) throws IllegalArgumentException {
        if (stations == null) {
            throw new IllegalArgumentException();
        }
        this.stations = stations;
        this.productionTime = productionTime;
        this.name = name;
        this.firstPart = 0;
        this.secondPart = 0;
        this.producedPart = 0;
        this.isWorking = false;
    }

    public Machine() {
    }

    public void addFirstPart(int part) {
        if (part >= 0) {
            this.firstPart += part;
        }
    }

    public void addSecondPart(int part) {
        if (part >= 0) {
            this.secondPart += part;
        }
    }

    public void addProducedPart(int part) {
        if (part >= 0) {
            this.producedPart += part;
        }
    }

    public void removeFirstPart(int part) {
        if (part >= 0) {
            this.firstPart -= part;
        }
    }

    public void removeSecondPart(int part) {
        if (part >= 0) {
            this.secondPart -= part;
        }
    }

    public void removeProducedPart(int part) {
        if (part >= 0) {
            this.producedPart -= part;
        }
    }

    public boolean getWorking() {
        return isWorking;
    }

    abstract void produce();

    abstract boolean checkIfReadyToProduce();

    abstract protected void load(Station station);

    abstract void unload(Station station);

    public void run() {
        while (true) {
            int numberOfStations = stations.size();
            Station station = stations.get(rand.nextInt(numberOfStations));
            load(station);
            if (checkIfReadyToProduce()) {
                produce();

                unload(station);
            }
        }
    }
}
