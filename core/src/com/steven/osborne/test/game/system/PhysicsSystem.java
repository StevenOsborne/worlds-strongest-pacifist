package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.steven.osborne.test.game.component.BodyComponent;
import com.steven.osborne.test.game.component.PositionComponent;

public class PhysicsSystem extends IteratingSystem {

    private static final float MAX_STEP_TIME = 1/45f;
    private static float accumulator = 0f;

    private World world;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<BodyComponent> bodyComponentMapper = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);

    public PhysicsSystem(World world) {
        super(Family.all(BodyComponent.class, PositionComponent.class).get());

        this.world = world;
        this.bodiesQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if (accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;

            //Entity Queue
            for (Entity entity : bodiesQueue) {
                PositionComponent positionComponent = positionComponentMapper.get(entity);
                BodyComponent bodyComponent = bodyComponentMapper.get(entity);
                Vector2 position = bodyComponent.getBody().getPosition();
                positionComponent.setX(position.x);
                positionComponent.setY(position.y);
//                positionComponent.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;
            }
        }

        bodiesQueue.clear();

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
