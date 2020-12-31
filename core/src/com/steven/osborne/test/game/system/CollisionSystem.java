package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.steven.osborne.test.game.component.CollisionComponent;
import com.steven.osborne.test.game.component.HealthComponent;

import java.util.List;

public class CollisionSystem extends IteratingSystem {

    private ComponentMapper<CollisionComponent> collisionComponentMapper = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<HealthComponent> healthComponentMapper = ComponentMapper.getFor(HealthComponent.class);

    private Array<Entity> collisionEntities;

    public CollisionSystem() {
        super(Family.all(CollisionComponent.class).get());

        this.collisionEntities = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        for (Entity entity : collisionEntities) {
            CollisionComponent collisionComponentA = collisionComponentMapper.get(entity);
            List<Entity> collidingWith = collisionComponentA.getCollidingWith();
            if (collidingWith != null) {
                resolveCollision(collidingWith, collisionComponentA);
                collisionComponentA.setCollidingWith(null);
            }
        }

        collisionEntities.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        collisionEntities.add(entity);
    }

    private void resolveCollision(List<Entity> collidedEntities,
                                  CollisionComponent collisionComponentA) {

        for (Entity entity : collidedEntities) {
            CollisionComponent collisionComponentB = collisionComponentMapper.get(entity);
            if (collisionComponentA.getDestroyTags().contains(collisionComponentB.getTag())) {
                if (healthComponentMapper.has(entity)) {
                    healthComponentMapper.get(entity).setHealth(0);
                }
            }
        }
    }
}
