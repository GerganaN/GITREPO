/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

public class WaterTreatmentPlantClient {
    private final static int TIME_WORKING = 50;

    public static void main(String[] args) {
        try {

            StringBuilder sb = new StringBuilder();
            for (int i = 3; i < args.length; i++) {
                sb.append(args[i]);
                if (i != args.length - 1) {
                    sb.append(" ");
                }
            }

            WaterTreatmentPlant plant = new WaterTreatmentPlant(args[0], args[1], args[2], sb.toString());
            plant.startPlant(TIME_WORKING);
            plant.stopPlant();
        } catch (IllegalArgumentException ex) {
            System.out.println("Illegal number of stations");
        }
    }
}
