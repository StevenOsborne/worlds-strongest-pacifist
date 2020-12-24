package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.gameobject.component.HealthComponent;

public class HealthSystem extends IteratingSystem {

    private ComponentMapper<HealthComponent> healthComponentMapper = ComponentMapper.getFor(HealthComponent.class);

    public HealthSystem() {
        super(Family.all(HealthComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = healthComponentMapper.get(entity);

        if (healthComponent.getHealth() <= 0 ) {
            getEngine().removeEntity(entity);
        }
    }
}
