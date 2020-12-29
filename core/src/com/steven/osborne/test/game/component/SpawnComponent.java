package com.steven.osborne.test.game.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.steven.osborne.test.game.factory.EntityFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class SpawnComponent implements Component {
    private int amount;
    private int amountIncrement;
    private int maximumAmount;
    private float delay;
    private float delayDecrement;
    private float minimumDelay;
    private float seconds;
    private EntityFactory factory;
    private List<Rectangle> spawnAreas;
}
