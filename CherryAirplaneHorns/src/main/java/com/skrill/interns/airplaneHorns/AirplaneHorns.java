package com.skrill.interns.airplaneHorns;

import java.util.ArrayList;
import java.util.List;

public class AirplaneHorns {
    // private static final int MACHINE_A_THREADS = 2;
    // private static final int MACHINE_B_THREADS = 2;
    // private static final int DELIVERY_THREADS = 4;
    // private static final int SENDER_THREADS = 1;
    private static final int STATION_NUMBER = 2;
    private static List<Station> stations;
    private static List<MachineState> machines;
    private static FactoryState state;

    static {
        List<Station> tempStationsList = new ArrayList<Station>();
        List<MachineState> tempMachinesList = new ArrayList<MachineState>();
        stations = tempStationsList;
        machines = tempMachinesList;
    }

    public static FactoryState getState() {
        return state;
    }

    public static void main(String[] args) {
        for (int i = 0; i < STATION_NUMBER; i++) {
            stations.add(new Station("Station " + i));
        }
        try {

            // ExecutorService deliveryExecutor =
            // Executors.newFixedThreadPool(DELIVERY_THREADS); deliveryExecutor.
            // deliveryExecutor.execute(new Delivery(Distributors.A, stations));
            // deliveryExecutor.execute(new Delivery(Distributors.B, stations));
            // deliveryExecutor.execute(new Delivery(Distributors.C, stations));
            //
            // ExecutorService machineAExecutor =
            // Executors.newFixedThreadPool(MACHINE_A_THREADS); for (int i = 0;
            // i < MACHINE_A_THREADS; i++) { MachineA machineA = new
            // MachineA(stations, 7000, "M_A " + i); machines.add(machineA);
            // machineAExecutor.execute(machineA); }
            //
            // ExecutorService machineBExecutor =
            // Executors.newFixedThreadPool(MACHINE_B_THREADS); for (int i = 0;
            // i < MACHINE_B_THREADS; i++) { MachineB machineB = new
            // MachineB(stations, 11000, "M_B " + i); machines.add(machineB);
            // machineBExecutor.execute(machineB);
            //
            // ExecutorService senderExecutor =
            // Executors.newFixedThreadPool(SENDER_THREADS); for (int i = 0; i <
            // SENDER_THREADS; i++) { senderExecutor.execute(new
            // Sender(stations)); }
            //
            // deliveryExecutor.shutdown(); machineAExecutor.shutdown();
            // machineBExecutor.shutdown(); senderExecutor.shutdown(); }

            Thread d1 = new Thread(new Delivery(Distributors.A, stations));
            Thread d2 = new Thread(new Delivery(Distributors.B, stations));
            Thread d3 = new Thread(new Delivery(Distributors.C, stations));

            Machine mA1 = new MachineA(stations, 7000, "M_A 1");
            Thread mA1T = new Thread(mA1);
            machines.add(mA1);

            Machine mA2 = new MachineA(stations, 7000, "M_A 2");
            Thread mA2T = new Thread(mA2);
            machines.add(mA2);

            Machine mB1 = new MachineB(stations, 11000, "M_B 1");
            Thread mB1T = new Thread(mB1);
            machines.add(mB1);

            Machine mB2 = new MachineB(stations, 11000, "M_B 2");
            Thread mB2T = new Thread(mB2);
            machines.add(mB2);

            state = new FactoryState(stations, machines);

            d1.start();
            d2.start();
            d3.start();
            mA1T.start();
            mA2T.start();
            mB1T.start();
            mB2T.start();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
