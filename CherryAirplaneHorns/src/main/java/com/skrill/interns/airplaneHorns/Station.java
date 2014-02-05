package com.skrill.interns.airplaneHorns;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.annotation.XmlElement;

public class Station {

    @XmlElement
    private String name;

    @XmlElement
    private volatile Integer partA;

    @XmlElement
    private volatile Integer partB;

    @XmlElement
    private volatile Integer partC;

    @XmlElement
    private volatile Integer partD;

    @XmlElement
    private volatile Integer partE;

    protected Lock locker = new ReentrantLock();

    public Station(String name) {
        partA = partB = partC = partD = partE = 0;
        this.name = name;
    }

    public Station() {
    }

    public String getName() {
        return name;
    }

    public Integer getPartA() {
        return partA;
    }

    public Integer getPartB() {
        return partB;
    }

    public Integer getPartC() {
        return partC;
    }

    public Integer getPartD() {
        return partD;
    }

    public Integer getPartE() {
        return partE;
    }

    public void addPartA(int quantity) {
        partA += quantity;
    }

    public void addPartB(int quantity) {
        partB += quantity;
    }

    public void addPartC(int quantity) {
        partC += quantity;
    }

    public void addPartD(int quantity) {
        partD += quantity;
    }

    public void addPartE(int quantity) {
        partE += quantity;
    }

    public void removePartA(int quantity) {
        partA -= quantity;
    }

    public void removePartB(int quantity) {
        partB -= quantity;
    }

    public void removePartC(int quantity) {
        partC -= quantity;

    }

    public void removePartD(int quantity) {
        partD -= quantity;
    }

    public void removePartE(int quantity) {
        partE -= quantity;
    }
}
