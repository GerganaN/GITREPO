package com.skrill.interns.waterTreatmentPlant;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pipe {

    private Lock lock = new ReentrantLock();
    private int pipeId;
    private boolean isInput;
    private int used;

    public Lock getLock() {
        return lock;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public Pipe(int pipeId) {
        this.pipeId = pipeId;
    }

    public boolean isInput() {
        return isInput;
    }

    public void setInput(boolean isInput) {
        this.isInput = isInput;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getPipeId() {
        return pipeId;
    }

    public void setPipeId(int pipeId) {
        this.pipeId = pipeId;
    }

    public void decrementUsage() {
        used--;
    }

    public void incrementUsage() {
        used++;
    }
}
