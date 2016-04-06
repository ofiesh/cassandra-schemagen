package com.datastax.driver.core;

public class UdtDataType extends DataType {

    private final String type;
    private final boolean frozen;

    public UdtDataType(String type) {
        super(null);
        this.type = type;
        this.frozen = true;
    }

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public String toString() {
        return "frozen<" + type + ">";
    }
}
