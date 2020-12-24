package com.steven.osborne.test.game.gameobject.component;

import com.badlogic.ashley.core.Component;
import com.steven.osborne.test.game.factory.EntityFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
}
