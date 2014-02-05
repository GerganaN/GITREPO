package com.skrill.interns.airplaneHorns;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "state")
public class FactoryState {

    @XmlElementWrapper(name = "stations")
    @XmlElement(name = "station")
    private List<Station> stations;

    @XmlElementWrapper(name = "machines")
    @XmlElement(name = "machine")
    private List<MachineState> machines;

    public FactoryState(List<Station> stations, List<MachineState> machines) {
        this.stations = stations;
        this.machines = machines;
    }

    public FactoryState() {
    }

}
