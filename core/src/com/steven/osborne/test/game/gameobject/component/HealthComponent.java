package com.steven.osborne.test.game.gameobject.component;

import com.badlogic.ashley.core.Component;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HealthComponent implements Component {
    private int health;
}
