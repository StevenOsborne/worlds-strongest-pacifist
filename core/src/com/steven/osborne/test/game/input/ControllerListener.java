package com.steven.osborne.test.game.input;

public interface ControllerListener {
    boolean onControllerAxisInput(int axisIndex, float value);
    boolean onControllerButtonInput(int buttonIndex);
}
