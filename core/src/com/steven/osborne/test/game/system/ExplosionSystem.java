package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.component.ExplosionComponent;

public class ExplosionSystem extends IteratingSystem {

    private ComponentMapper<ExplosionComponent> explosionComponentMapper = ComponentMapper.getFor(ExplosionComponent.class);

    public ExplosionSystem() {
        super(Family.all(ExplosionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ExplosionComponent explosionComponent = explosionComponentMapper.get(entity);

        explosionComponent.setRadius(Math.min(explosionComponent.getRadius() + (20 * deltaTime), explosionComponent.getMaximumRadius()));

        if (explosionComponent.getRadius() >= explosionComponent.getMaximumRadius()) {
            getEngine().removeEntity(entity);
        }
    }
}
