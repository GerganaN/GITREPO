package com.skrill.interns.airplaneHorns;

import java.util.List;

public class Sender implements Runnable {
    private List<Station> stations;

    public Sender(List<Station> stations) {
        this.stations = stations;
    }

    public void run() {
        Station currentStation;
        int amountPartE;
        while (true) {
            for (int i = 0; i < stations.size(); i++) {
                currentStation = stations.get(i);
                if (currentStation.getPartE() > 0) {
                    amountPartE = currentStation.getPartE();
                    currentStation.removePartE(amountPartE);
                    System.out.println(currentStation.getName() + " sends " + amountPartE + " P_E");
                }
            }
        }
    }
}
