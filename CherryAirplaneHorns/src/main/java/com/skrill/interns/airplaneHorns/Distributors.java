package com.skrill.interns.airplaneHorns;

public enum Distributors {

    A(20000, 200, "P_A" ), B(14000, 50, "P_B"), C(13000, 20, "P_C");

    private final int frequency;
    private final int quantity;
    private final String partName;

    Distributors(int frequency, int quantity, String partName) {
        this.frequency = frequency;
        this.quantity = quantity;
        this.partName = partName;
    }

    public int frequency() {
        return frequency;
    }

    public int quantity() {
        return quantity;
    }
    public String partName(){
        return partName;
    }
}
