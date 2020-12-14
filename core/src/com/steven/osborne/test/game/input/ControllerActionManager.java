package com.steven.osborne.test.game.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class ControllerActionManager extends ControllerAdapter {
    private List<ControllerListener> controllerListeners = new ArrayList();
    private Array<Controller> connectedControllers;

    public ControllerActionManager() {
        Controllers.addListener(this);
        connectedControllers = Controllers.getControllers();
    }

    public void subscribe(ControllerListener listener) {
        controllerListeners.add(0, listener);
    }

    public void unsubscribe(ControllerListener listener) {
        controllerListeners.remove(listener);
    }

    @Override
    public void connected (Controller controller) {
        connectedControllers.add(controller);
    }

    @Override
    public boolean axisMoved (Controller controller, int axisIndex, float value) {
        for (ControllerListener controllerListener : controllerListeners) {
            if (controllerListener.onControllerInput(axisIndex, value)) {
                return true;
            }
        }
        return false;
    }
}
