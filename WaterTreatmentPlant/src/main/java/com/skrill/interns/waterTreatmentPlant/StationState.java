/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.waterTreatmentPlant;

public class StationState {

    private String plantName;
    private int stationId;
    private boolean working;
    private int firstPipeId;
    private boolean firstPipeInput;
    private int secondPipeId;
    private boolean secondPipeInput;
    private int firstPipeUsage;
    private int secondPipeUsage;

    public StationState() {
    }

    public StationState(String plantName, int stationId, boolean working, int firstPipeId, boolean firstPipeInput,
            int secondPipeId, boolean secondPipeInput, int firstPipeUsage, int secondPipeUsage) {

        this.plantName = plantName;
        this.stationId = stationId;
        this.working = working;
        this.firstPipeId = firstPipeId;
        this.firstPipeInput = firstPipeInput;
        this.secondPipeId = secondPipeId;
        this.secondPipeInput = secondPipeInput;
        this.firstPipeUsage = firstPipeUsage;
        this.secondPipeUsage = secondPipeUsage;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public int getFirstPipeId() {
        return firstPipeId;
    }

    public void setFirstPipeId(int firstPipeId) {
        this.firstPipeId = firstPipeId;
    }

    public boolean isFirstPipeInput() {
        return firstPipeInput;
    }

    public void setFirstPipeInput(boolean firstPipeInput) {
        this.firstPipeInput = firstPipeInput;
    }

    public int getSecondPipeId() {
        return secondPipeId;
    }

    public void setSecondPipeId(int secondPipeId) {
        this.secondPipeId = secondPipeId;
    }

    public boolean isSecondPipeInput() {
        return secondPipeInput;
    }

    public void setSecondPipeInput(boolean secondPipeInput) {
        this.secondPipeInput = secondPipeInput;
    }

    public int getFirstPipeUsage() {
        return firstPipeUsage;
    }

    public void setFirstPipeUsage(int firstPipeUsage) {
        this.firstPipeUsage = firstPipeUsage;
    }

    public int getSecondPipeUsage() {
        return secondPipeUsage;
    }

    public void setSecondPipeUsage(int secondPipeUsage) {
        this.secondPipeUsage = secondPipeUsage;
    }

    public void update(boolean workingState, boolean firstPipeInputState, boolean secondPipeInputState) {
        working = workingState;
        firstPipeInput = firstPipeInputState;
        secondPipeInput = secondPipeInputState;
    }

    @Override
    public String toString() {
        return "StationState [plantName=" + plantName + ", stationId=" + stationId + ", working=" + working
                + ", firstPipeId=" + firstPipeId + ", firstPipeInput=" + firstPipeInput + ", secondPipeId="
                + secondPipeId + ", secondPipeInput=" + secondPipeInput + ", firstPipeUsage=" + firstPipeUsage
                + ", secondPipeUsage=" + secondPipeUsage + "]";
    }
}
