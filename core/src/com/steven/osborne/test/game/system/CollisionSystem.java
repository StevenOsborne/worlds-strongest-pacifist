package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.steven.osborne.test.game.gameobject.component.*;

public class CollisionSystem extends IteratingSystem {

    private ComponentMapper<CollisionComponent> collisionComponentMapper = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<BoundsComponent> boundsComponentMapper = ComponentMapper.getFor(BoundsComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<HealthComponent> healthComponentMapper = ComponentMapper.getFor(HealthComponent.class);
    private ComponentMapper<ExplosionComponent> explosionComponentMapper = ComponentMapper.getFor(ExplosionComponent.class);

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
                    if (boundsComponent.getRectangle() != null && boundsComponent1.getRectangle() != null) { //Rectangle, Rectangle
                        if (boundsComponent.getRectangle().overlaps(boundsComponent1.getRectangle())) {
                            resolveCollision(collisionEntity, collisionComponent, positionComponent, boundsComponent, boundsComponent1, collisionComponent1);
                        }
                    } else if (boundsComponent.getCircle() != null && boundsComponent1.getRectangle() != null) { //Circle, Rectangle
                        if (Intersector.overlaps(boundsComponent.getCircle(), boundsComponent1.getRectangle())) {
                            resolveCollision(collisionEntity, collisionComponent, positionComponent, boundsComponent, boundsComponent1, collisionComponent1);
                        }
                    } else if (boundsComponent.getCircle() != null && boundsComponent1.getCircle() != null) {//Circle, Circle
                        if (Intersector.overlaps(boundsComponent.getCircle(), boundsComponent1.getCircle())) {
                            System.out.println("Circle, circle collision");
                        }
                    }
                }
            }
        }
    }

    private void resolveCollision(Entity collidedEntity,
                                  CollisionComponent collidingCollisionComponent,
                                  PositionComponent collidingPositionComponent,
                                  BoundsComponent collidingBoundsComponent,
                                  BoundsComponent collidedBoundsComponent,
                                  CollisionComponent collidedCollisionComponent) {

        if (collidingCollisionComponent.getCollideTags().contains(collidedCollisionComponent.getTag())) {
            resolveSolidCollision(collidingPositionComponent, collidingBoundsComponent, collidedBoundsComponent);
        }

        if (collidingCollisionComponent.getDestroyTags().contains(collidedCollisionComponent.getTag())) {
            if (healthComponentMapper.has(collidedEntity)) {
                healthComponentMapper.get(collidedEntity).setHealth(0);
            }
        }
    }

    //TODO - It's kind of crap, but it works for now - THIS DOES NOT WORK AT BELOW 50FPS
    private void resolveSolidCollision(PositionComponent collidingPositionComponent, BoundsComponent collidingBoundsComponent, BoundsComponent collidedBoundsComponent) {
        float collidingLeft = collidingBoundsComponent.getRectangle().getX();
        float collidingRight = collidingLeft + collidingBoundsComponent.getRectangle().getWidth();
        float collidingBottom = collidingBoundsComponent.getRectangle().getY();
        float collidingTop = collidingBottom + collidingBoundsComponent.getRectangle().getHeight();

        float collidedLeft = collidedBoundsComponent.getRectangle().getX();
        float collidedRight = collidedLeft + collidedBoundsComponent.getRectangle().getWidth();
        float collidedBottom = collidedBoundsComponent.getRectangle().getY();
        float collidedTop = collidedBottom + collidedBoundsComponent.getRectangle().getHeight();

        if (collidingRight > collidedLeft && collidingRight < collidedRight) {
            collidingPositionComponent.setX(collidingPositionComponent.getX() - 0.3f);
            collidingBoundsComponent.getRectangle().setX(collidingBoundsComponent.getRectangle().getX() - 0.3f);
        }

        if (collidingLeft < collidedRight && collidingLeft > collidedLeft) {
            collidingPositionComponent.setX(collidingPositionComponent.getX() + 0.3f);
            collidingBoundsComponent.getRectangle().setX(collidingBoundsComponent.getRectangle().getX() + 0.3f);
        }

        if (collidingTop > collidedBottom && collidingTop < collidedTop) {
            collidingPositionComponent.setY(collidingPositionComponent.getY() - 0.3f);
            collidingBoundsComponent.getRectangle().setY(collidingBoundsComponent.getRectangle().getY() - 0.3f);
        }

        if (collidingBottom < collidedTop && collidingBottom > collidedBottom) {
            collidingPositionComponent.setY(collidingPositionComponent.getY() + 0.3f);
            collidingBoundsComponent.getRectangle().setY(collidingBoundsComponent.getRectangle().getY() + 0.3f);
        }
    }
}
