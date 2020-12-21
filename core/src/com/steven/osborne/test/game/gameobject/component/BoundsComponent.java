package com.steven.osborne.test.game.gameobject.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class BoundsComponent implements Component {
    private Rectangle bounds; //May need to create a more specific bounds class if we need more shapes
}
