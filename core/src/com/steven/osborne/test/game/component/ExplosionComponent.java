package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExplosionComponent implements Component {
    private float radius;
    private float maximumRadius;
}
