package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VelocityComponent implements Component {
    public static final float PLAYER_SPEED = 15;
    public static final float ENEMY_SPEED = 10;
    private float x;
    private float y;
}
