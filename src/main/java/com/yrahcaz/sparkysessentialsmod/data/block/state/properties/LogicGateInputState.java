package com.yrahcaz.sparkysessentialsmod.data.block.state.properties;

import net.minecraft.util.StringRepresentable;

public enum LogicGateInputState implements StringRepresentable {
    NONE("none"),
    ZERO("0"),
    ONE("1"),
    BOTH("both");

    private final String name;

    LogicGateInputState(String p_61534_) {
        this.name = p_61534_;
    }

    public String getSerializedName() {
        return this.name;
    }
}
