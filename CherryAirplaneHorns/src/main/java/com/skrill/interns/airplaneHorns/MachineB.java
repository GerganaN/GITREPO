package com.skrill.interns.airplaneHorns;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import com.skrill.interns.airplaneHornsClientServer.TCPClient;

public class MachineB extends Machine {
    private final int REQUIRED_PART_C = 22;
    private final int REQUIRED_PART_D = 7;
    private final int PRODUCED_PART_E = 5;
    private TCPClient airplaneHornsClient;

    public MachineB(List<Station> stations, int productionTime, String name) throws IllegalArgumentException {
        super(stations, productionTime, name);
        try {
            this.airplaneHornsClient = new TCPClient();
        } catch (UnknownHostException e) {
            this.airplaneHornsClient = null;
        } catch (IOException e) {
            this.airplaneHornsClient = null;
        }
    }

    protected void load(Station station) {
        synchronized (station.getPartC()) {
            if ((firstPart < REQUIRED_PART_C) && (station.getPartC() >= REQUIRED_PART_C)) {
                station.removePartC(REQUIRED_PART_C);
                addFirstPart(REQUIRED_PART_C);
                System.out.println(REQUIRED_PART_C + " P_C are loaded in " + this.name + " from " + station.getName());
                airplaneHornsClient.sendToServer(AirplaneHorns.getState());
            }
        }
        synchronized (station.getPartD()) {
            if ((this.secondPart < REQUIRED_PART_D) && (station.getPartD() >= REQUIRED_PART_D)) {
                station.removePartD(REQUIRED_PART_D);
                addSecondPart(REQUIRED_PART_D);
                System.out.println(REQUIRED_PART_D + " P_D are loaded in " + this.name + " from " + station.getName());
                airplaneHornsClient.sendToServer(AirplaneHorns.getState());
            }
        }
    }

    protected void unload(Station station) {
        removeProducedPart(PRODUCED_PART_E);
        station.addPartE(PRODUCED_PART_E);
        System.out.println(this.name + " produced " + PRODUCED_PART_E + " P_E");
    }

    protected boolean checkIfReadyToProduce() {
        if ((firstPart >= REQUIRED_PART_C) && (secondPart >= REQUIRED_PART_D)) {
            return true;
        }
        return false;
    }

    protected void produce() {
        try {
            isWorking = true;
            removeFirstPart(REQUIRED_PART_C);
            removeSecondPart(REQUIRED_PART_D);
            Thread.sleep(productionTime);
            addProducedPart(PRODUCED_PART_E);
            isWorking = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
