package com.steven.osborne.test.game.gameobject.component;

import com.badlogic.ashley.core.Component;
import lombok.Builder;

@Builder
public class InputComponent implements Component {
    public static final float CONTROLLER_DEAD_ZONE = 0.2f;
}
