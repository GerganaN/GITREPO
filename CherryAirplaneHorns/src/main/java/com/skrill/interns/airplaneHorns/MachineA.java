package com.skrill.interns.airplaneHorns;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import com.skrill.interns.airplaneHornsClientServer.TCPClient;

public class MachineA extends Machine {
    private final int REQUIRED_PART_A = 77;
    private final int REQUIRED_PART_B = 29;
    private final int PRODUCED_PART_D = 8;
    private TCPClient airplaneHornsClient;

    public MachineA(List<Station> stations, int productionTime, String name) throws IllegalArgumentException {
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
        synchronized (station.getPartA()) {
            if (firstPart < REQUIRED_PART_A && station.getPartA() >= REQUIRED_PART_A) {
                station.removePartA(REQUIRED_PART_A);
                addFirstPart(REQUIRED_PART_A);
                System.out.println(REQUIRED_PART_A + " P_A are loaded in " + this.name + " from " + station.getName());
                airplaneHornsClient.sendToServer(AirplaneHorns.getState());
            }
        }
        synchronized (station.getPartB()) {
            if (this.secondPart < REQUIRED_PART_B && station.getPartB() >= REQUIRED_PART_B) {
                station.removePartB(REQUIRED_PART_B);
                addSecondPart(REQUIRED_PART_B);
                System.out.println(REQUIRED_PART_B + " P_B are loaded in " + this.name + " from " + station.getName());
                airplaneHornsClient.sendToServer(AirplaneHorns.getState());
            }
        }
    }

    protected void unload(Station station) {
        removeProducedPart(PRODUCED_PART_D);
        station.addPartD(PRODUCED_PART_D);
        System.out.println(this.name + " produced " + PRODUCED_PART_D + " P_D");
    }

    protected boolean checkIfReadyToProduce() {
        if ((firstPart >= REQUIRED_PART_A) && (secondPart >= REQUIRED_PART_B)) {
            return true;
        }
        return false;
    }

    protected void produce() {
        try {
            isWorking = true;
            removeFirstPart(REQUIRED_PART_A);
            removeSecondPart(REQUIRED_PART_B);
            Thread.sleep(productionTime);
            addProducedPart(PRODUCED_PART_D);
            isWorking = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
