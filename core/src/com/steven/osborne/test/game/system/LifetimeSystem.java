package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.component.LifetimeComponent;

public class LifetimeSystem extends IteratingSystem {

    private ComponentMapper<LifetimeComponent> lifetimeComponentMapper = ComponentMapper.getFor(LifetimeComponent.class);

    public LifetimeSystem() {
        super(Family.all(LifetimeComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        LifetimeComponent lifetimeComponent = lifetimeComponentMapper.get(entity);

        lifetimeComponent.setTimePassed(lifetimeComponent.getTimePassed() + deltaTime);

        if (lifetimeComponent.getTimePassed() >= lifetimeComponent.getLifetime()) {
            getEngine().removeEntity(entity);
        }
    }
}
