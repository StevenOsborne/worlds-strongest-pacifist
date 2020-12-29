package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BoundsComponent implements Component {
    private Rectangle rectangle;
    private Circle circle;
}
