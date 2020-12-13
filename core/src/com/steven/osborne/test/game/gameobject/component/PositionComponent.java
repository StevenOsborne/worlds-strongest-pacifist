package com.steven.osborne.test.game.gameobject.component;

import com.badlogic.ashley.core.Component;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class PositionComponent implements Component {
    private float x;
    private float y;
}
