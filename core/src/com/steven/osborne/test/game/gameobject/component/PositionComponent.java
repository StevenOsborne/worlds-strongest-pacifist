package com.steven.osborne.test.game.gameobject.component;

import com.badlogic.ashley.core.Component;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(setterPrefix = "with")
public class PositionComponent implements Component {
    private float x = 0.0f;
    private float y = 0.0f;
}
