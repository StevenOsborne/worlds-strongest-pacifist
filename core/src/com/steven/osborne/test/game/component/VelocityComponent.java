package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;

@Builder
public class VelocityComponent implements Component {
    public float speed;
    public Vector2 velocity;
}
