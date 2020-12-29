package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.component.PositionComponent;
import com.steven.osborne.test.game.component.VelocityComponent;

public class MovementSystem extends IteratingSystem {
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<VelocityComponent> velocityComponentMapper = ComponentMapper.getFor(VelocityComponent.class);

    public MovementSystem() {
        super(Family.all(PositionComponent.class, VelocityComponent.class).get());
    }

    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = positionComponentMapper.get(entity);
        VelocityComponent velocity = velocityComponentMapper.get(entity);

        position.setX(position.getX() + (velocity.getX() * deltaTime));
        position.setY(position.getY() + (velocity.getY() * deltaTime));
    }
}