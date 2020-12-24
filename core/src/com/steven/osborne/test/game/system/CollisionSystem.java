package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.steven.osborne.test.game.gameobject.component.BoundsComponent;
import com.steven.osborne.test.game.gameobject.component.CollisionComponent;
import com.steven.osborne.test.game.gameobject.component.HealthComponent;
import com.steven.osborne.test.game.gameobject.component.PositionComponent;

public class CollisionSystem extends IteratingSystem {

    private ComponentMapper<CollisionComponent> collisionComponentMapper = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<BoundsComponent> boundsComponentMapper = ComponentMapper.getFor(BoundsComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<HealthComponent> healthComponentMapper = ComponentMapper.getFor(HealthComponent.class);

    private ImmutableArray<Entity> collisionEntities;

    public CollisionSystem() {
        super(Family.all(CollisionComponent.class, BoundsComponent.class, PositionComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        collisionEntities = engine.getEntitiesFor(Family.all(CollisionComponent.class, BoundsComponent.class, PositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) { //TODO - This approach may not work with lots of entities
        BoundsComponent boundsComponent = boundsComponentMapper.get(entity);
        CollisionComponent collisionComponent = collisionComponentMapper.get(entity);
        PositionComponent positionComponent = positionComponentMapper.get(entity);

        if (!collisionComponent.isStatic()) {
            for (Entity collisionEntity : collisionEntities) {
                if (entity != collisionEntity) {
                    BoundsComponent boundsComponent1 = boundsComponentMapper.get(collisionEntity);
                    CollisionComponent collisionComponent1 = collisionComponentMapper.get(collisionEntity);
                    if (boundsComponent.getBounds().overlaps(boundsComponent1.getBounds())) {
                        resolveCollision(entity, collisionComponent, positionComponent, boundsComponent, boundsComponent1, collisionComponent1);
                    }
                }
            }
        }
    }

    //TODO - This will eventually need to handle destructive collisions (player and enemies)
    //TODO - Collision with other entities (enemies, barbells, multiplier) only need to know if a collision happens, not direction
    private void resolveCollision(Entity collidingEntity,
                                  CollisionComponent collidingCollisionComponent,
                                  PositionComponent collidingPositionComponent,
                                  BoundsComponent collidingBoundsComponent,
                                  BoundsComponent collidedBoundsComponent,
                                  CollisionComponent collidedCollisionComponent) {

        resolveSolidCollision(collidingPositionComponent, collidingBoundsComponent, collidedBoundsComponent);

        if (collidingCollisionComponent.getCollidingTags().contains(collidedCollisionComponent.getTag())) {
            if (healthComponentMapper.has(collidingEntity)) {
                healthComponentMapper.get(collidingEntity).setHealth(0);
            }
        }
    }

    //TODO - It's kind of crap, but it works for now - THIS DOES NOT WORK AT BELOW 50FPS
    private void resolveSolidCollision(PositionComponent collidingPositionComponent, BoundsComponent collidingBoundsComponent, BoundsComponent collidedBoundsComponent) {
        float collidingLeft = collidingBoundsComponent.getBounds().getX();
        float collidingRight = collidingLeft + collidingBoundsComponent.getBounds().getWidth();
        float collidingBottom = collidingBoundsComponent.getBounds().getY();
        float collidingTop = collidingBottom + collidingBoundsComponent.getBounds().getHeight();

        float collidedLeft = collidedBoundsComponent.getBounds().getX();
        float collidedRight = collidedLeft + collidedBoundsComponent.getBounds().getWidth();
        float collidedBottom = collidedBoundsComponent.getBounds().getY();
        float collidedTop = collidedBottom + collidedBoundsComponent.getBounds().getHeight();

        if (collidingRight > collidedLeft && collidingRight < collidedRight) {
            collidingPositionComponent.setX(collidingPositionComponent.getX() - 0.3f);
            collidingBoundsComponent.getBounds().setX(collidingBoundsComponent.getBounds().getX() - 0.3f);
        }

        if (collidingLeft < collidedRight && collidingLeft > collidedLeft) {
            collidingPositionComponent.setX(collidingPositionComponent.getX() + 0.3f);
            collidingBoundsComponent.getBounds().setX(collidingBoundsComponent.getBounds().getX() + 0.3f);
        }

        if (collidingTop > collidedBottom && collidingTop < collidedTop) {
            collidingPositionComponent.setY(collidingPositionComponent.getY() - 0.3f);
            collidingBoundsComponent.getBounds().setY(collidingBoundsComponent.getBounds().getY() - 0.3f);
        }

        if (collidingBottom < collidedTop && collidingBottom > collidedBottom) {
            collidingPositionComponent.setY(collidingPositionComponent.getY() + 0.3f);
            collidingBoundsComponent.getBounds().setY(collidingBoundsComponent.getBounds().getY() + 0.3f);
        }
    }
}
