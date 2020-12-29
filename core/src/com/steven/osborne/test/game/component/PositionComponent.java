package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PositionComponent implements Component {
    private float x;
    private float y;
}
