package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.steven.osborne.test.game.component.*;

public class ExplosionSystem extends IteratingSystem {

    private class AreaQueryCallback implements QueryCallback {
        public Array<Body> foundBodies = new Array<>();
        @Override
        public boolean reportFixture(Fixture fixture) {
            foundBodies.add(fixture.getBody());
            return true;
        }
    }

    private ComponentMapper<ExplosionComponent> explosionComponentMapper = ComponentMapper.getFor(ExplosionComponent.class);
    private ComponentMapper<CollisionComponent> collisionComponentMapper = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<HealthComponent> healthComponentMapper = ComponentMapper.getFor(HealthComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);

    private World world;

    public ExplosionSystem(World world) {
        super(Family.all(ExplosionComponent.class, HealthComponent.class, PositionComponent.class, CollisionComponent.class).get());
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ExplosionComponent explosionComponent = explosionComponentMapper.get(entity);
        HealthComponent healthComponent = healthComponentMapper.get(entity);
        PositionComponent positionComponent = positionComponentMapper.get(entity);
        CollisionComponent collisionComponent = collisionComponentMapper.get(entity);

        AreaQueryCallback areaQueryCallback = new AreaQueryCallback(); //TODO - Do we need to create this every time?

        world.QueryAABB(areaQueryCallback, positionComponent.getX() - explosionComponent.getRadius(),
                positionComponent.getY() - explosionComponent.getRadius(),
                positionComponent.getX() + explosionComponent.getRadius(),
                positionComponent.getY() + explosionComponent.getRadius());

        for (Body body : areaQueryCallback.foundBodies) {
            if ((body.getWorldCenter().sub(new Vector2(positionComponent.getX(), positionComponent.getY()))).len() >= explosionComponent.getRadius()) {
                continue;
            }
            Entity collidingEntity = (Entity)body.getUserData();
            collisionComponent.getCollidingWith().add(collidingEntity);

        }

        explosionComponent.setRadius(Math.min(explosionComponent.getRadius() + (100 * deltaTime), explosionComponent.getMaximumRadius()));

        if (explosionComponent.getRadius() >= explosionComponent.getMaximumRadius()) {
            healthComponent.setHealth(0);
        }
    }
}
