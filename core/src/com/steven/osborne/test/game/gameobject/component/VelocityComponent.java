package com.steven.osborne.test.game.gameobject.component;

import com.badlogic.ashley.core.Component;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VelocityComponent implements Component {
    public static final float SPEED = 15;
    private float x;
    private float y;
}
