package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ParentComponent implements Component {
    private Entity parent;
    private Vector2 relativePosition;
}
