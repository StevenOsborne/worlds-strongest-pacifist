package com.steven.osborne.test.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class InputActionManager extends InputAdapter {
    private List<ActionListener> actionListeners = new ArrayList();
    private Map<Integer, InputAction> keyboardMappings = Map.ofEntries(
            entry(Input.Keys.UP, InputAction.UP),
            entry(Input.Keys.DOWN, InputAction.DOWN),
            entry(Input.Keys.LEFT, InputAction.LEFT),
            entry(Input.Keys.RIGHT, InputAction.RIGHT),
            entry(Input.Keys.MENU, InputAction.MENU)
    );

    public InputActionManager() {
        Gdx.input.setInputProcessor(this);
    }

    public void subscribe(ActionListener listener) {
        actionListeners.add(0, listener);
    }

    public void unsubscribe(ActionListener listener) {
        actionListeners.remove(listener);
    }

    @Override
    public boolean keyDown (int keycode) {
        InputAction action = keyboardMappings.get(keycode);

        if (action != null) {
            for (ActionListener actionListener : actionListeners) {
                if (actionListener.onAction(action)) {
                    return true;
                }
            }
        }
        return false;
    }
}
