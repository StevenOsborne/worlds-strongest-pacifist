package com.steven.osborne.test.game.event;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.steven.osborne.test.game.component.CollisionComponent;
import com.steven.osborne.test.game.component.ExplosionComponent;
import com.steven.osborne.test.game.component.HealthComponent;
import com.steven.osborne.test.game.component.PositionComponent;

import java.util.Arrays;

public class BarbellOnDeathEvent implements OnDeathEvent {

    public BarbellOnDeathEvent() {
    }

    @Override
    public void execute(Engine engine, Entity entity) {
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        Entity explosion = new Entity();
        explosion.add(ExplosionComponent.builder().radius(1f).maximumRadius(8f).build());
        explosion.add(PositionComponent.builder().x(positionComponent.getX()).y(positionComponent.getY()).build());
        explosion.add(CollisionComponent.builder().tag("Explosion").isStatic(false).destroyTags(Arrays.asList("Enemy")).build());
        explosion.add(HealthComponent.builder().health(1).build());

        engine.addEntity(explosion);
    }
}
