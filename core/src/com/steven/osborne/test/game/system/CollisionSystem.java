package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.component.CollisionComponent;
import com.steven.osborne.test.game.component.HealthComponent;

public class CollisionSystem extends IteratingSystem {

    private ComponentMapper<CollisionComponent> collisionComponentMapper = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<HealthComponent> healthComponentMapper = ComponentMapper.getFor(HealthComponent.class);

    public CollisionSystem() {
        super(Family.all(CollisionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent collisionComponentA = collisionComponentMapper.get(entity);
        Entity collidedEntity = collisionComponentA.getCollidingWith();
        if (collidedEntity != null) {
            CollisionComponent collisionComponentB = collisionComponentMapper.get(collidedEntity);
            resolveCollision(collidedEntity, collisionComponentA, collisionComponentB);
        }
    }

    private void resolveCollision(Entity collidedEntity,
                                  CollisionComponent collisionComponentA,
                                  CollisionComponent collisionComponentB) {

        if (collisionComponentA.getDestroyTags().contains(collisionComponentB.getTag())) {
            if (healthComponentMapper.has(collidedEntity)) {
                healthComponentMapper.get(collidedEntity).setHealth(0);
            }
        }
    }
}
