package com.steven.osborne.test.game.event;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.steven.osborne.test.game.component.HealthComponent;
import com.steven.osborne.test.game.component.PointsComponent;

public class MultiplierOnDeathEvent implements OnDeathEvent {

    @Override
    public void execute(Engine engine, Entity entity) {
        Entity points = new Entity();
        points.add(PointsComponent.builder().multiplier(1).build());
        points.add(HealthComponent.builder().health(0).build());

        engine.addEntity(points);
    }
}
