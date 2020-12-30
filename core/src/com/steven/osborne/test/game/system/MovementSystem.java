package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.component.BodyComponent;
import com.steven.osborne.test.game.component.VelocityComponent;

public class MovementSystem extends IteratingSystem {
    private ComponentMapper<BodyComponent> bodyComponentMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<VelocityComponent> velocityComponentMapper = ComponentMapper.getFor(VelocityComponent.class);

    public MovementSystem() {
        super(Family.all(BodyComponent.class, VelocityComponent.class).get());
    }

    public void processEntity(Entity entity, float deltaTime) {
        BodyComponent bodyComponent = bodyComponentMapper.get(entity);
        VelocityComponent velocity = velocityComponentMapper.get(entity);

        bodyComponent.getBody().setLinearVelocity(velocity.getX(), velocity.getY());
    }
}