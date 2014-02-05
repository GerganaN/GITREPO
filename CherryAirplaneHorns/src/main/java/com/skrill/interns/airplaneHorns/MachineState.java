package com.skrill.interns.airplaneHorns;

import javax.xml.bind.annotation.XmlElement;

public class MachineState {
    @XmlElement
    protected String name;

    @XmlElement
    protected boolean isWorking;

    public MachineState() {

    }

}
