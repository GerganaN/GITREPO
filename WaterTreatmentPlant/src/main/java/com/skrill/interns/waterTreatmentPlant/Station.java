package com.skrill.interns.waterTreatmentPlant;

import java.util.Random;

import com.google.gson.Gson;

public class Station implements Runnable {
    private String plantName;
    private int stationId;
    private Pipe firstPipe;
    private Pipe secondPipe;
    private boolean power;
    private boolean working;
    private StatisticsLogger logger;

    public Station(int stationId, String plantName, String hostname, String port) {
        this.stationId = stationId;
        this.logger = new StatisticsLogger(hostname, port);
        power = false;
        working = false;
        this.plantName = plantName;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public Pipe getFirstPipe() {
        return firstPipe;
    }

    public void setFirstPipe(Pipe firstPipe) {
        this.firstPipe = firstPipe;
    }

    public Pipe getSecondPipe() {
        return secondPipe;
    }

    public void setSecondPipe(Pipe secondPipe) {
        this.secondPipe = secondPipe;
    }

    public void setPower(boolean power) {
        this.power = power;
    }

    public boolean onePipeIsUsed() {
        if (firstPipe.getUsed() > 0) {
            if (secondPipe.getUsed() == 0) {
                incrementPipeUsage();
                adjustFlow(firstPipe, secondPipe);
                return true;
            }
        } else {
            if (secondPipe.getUsed() > 0) {
                incrementPipeUsage();
                adjustFlow(secondPipe, firstPipe);
                return true;
            }
        }
        return false;
    }

    public boolean bothPipesAreUsed() {

        if (firstPipe.getUsed() > 0 && secondPipe.getUsed() > 0) {
            if (firstPipe.isInput() && (!secondPipe.isInput())) {
                incrementPipeUsage();
                return true;
            }
            if ((!firstPipe.isInput()) && secondPipe.isInput()) {
                incrementPipeUsage();
                return true;
            }
        }
        return false;
    }

    public boolean noPipesAreUsed() {
        if (firstPipe.getUsed() == 0 && secondPipe.getUsed() == 0) {
            firstPipe.setInput(true);
            secondPipe.setInput(false);
            incrementPipeUsage();
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        boolean firstPipeLock = false;
        boolean secondPipeLock = false;
        while (power) {
            firstPipeLock = firstPipe.getLock().tryLock();
            secondPipeLock = secondPipe.getLock().tryLock();
            if (firstPipeLock && secondPipeLock) {
                if (bothPipesAreUsed() || onePipeIsUsed() || noPipesAreUsed()) {
                    working = true;
                }
            }
            if (firstPipeLock) {
                firstPipe.getLock().unlock();
            }
            if (secondPipeLock) {
                secondPipe.getLock().unlock();
            }
            if (working) {
                startWorking();
                stationNotWorking();
            }
        }
    }

    private void startWorking() {
        try {
            pumpingWater();
            Thread.sleep(new Random().nextInt(4) * 1000 + 1000);

            firstPipe.getLock().lock();
            firstPipe.decrementUsage();
            logger.sendInfo(stateToJson());
            firstPipe.getLock().unlock();

            secondPipe.getLock().lock();
            secondPipe.decrementUsage();
            logger.sendInfo(stateToJson());
            secondPipe.getLock().unlock();

            working = false;
        } catch (InterruptedException e) {
            System.out.println("InterruptedException exception!");
            e.printStackTrace();
        }
    }

    private void stationNotWorking() {
        try {
            logger.sendInfo(stateToJson());
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void pumpingWater() {
        logger.sendInfo(stateToJson());
    }

    public void adjustFlow(Pipe takenPipe, Pipe freePipe) {
        if (takenPipe.isInput()) {
            freePipe.setInput(false);
        } else {
            freePipe.setInput(true);
        }
    }

    public void incrementPipeUsage() {
        firstPipe.incrementUsage();
        logger.sendInfo(stateToJson());
        secondPipe.incrementUsage();
        logger.sendInfo(stateToJson());
    }

    public String stateToJson() {
        Gson gson = new Gson();
        String json = gson.toJson(getStationState());
        return json;
    }

    public StationState getStationState() {
        StationState stationState = new StationState(plantName, stationId, working, firstPipe.getPipeId(),
                firstPipe.isInput(), secondPipe.getPipeId(), secondPipe.isInput(), firstPipe.getUsed(),
                secondPipe.getUsed());
        return stationState;
    }

}
