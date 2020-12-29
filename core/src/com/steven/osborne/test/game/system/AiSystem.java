package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.component.AiComponent;
import com.steven.osborne.test.game.component.InputComponent;
import com.steven.osborne.test.game.component.PositionComponent;
import com.steven.osborne.test.game.component.VelocityComponent;

public class AiSystem extends IteratingSystem {

    private ComponentMapper<VelocityComponent> velocityComponentMapper = ComponentMapper.getFor(VelocityComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<AiComponent> aiComponentMapper = ComponentMapper.getFor(AiComponent.class);

    private Entity player;//TODO - Should this be passed into the AiComponent?

    public AiSystem() {
        super(Family.all(AiComponent.class, VelocityComponent.class, PositionComponent.class).get());
    }

    @Override
    public void addedToEngine (Engine engine) {
        super.addedToEngine(engine);

        player = getEngine().getEntitiesFor(Family.all(InputComponent.class, PositionComponent.class).get()).first();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent velocityComponent = velocityComponentMapper.get(entity);
        PositionComponent positionComponent = positionComponentMapper.get(entity);
        PositionComponent playerPositionComponent = positionComponentMapper.get(player);
        AiComponent aiComponent = aiComponentMapper.get(entity);

        boolean followPlayer = true;

        if (aiComponent.getRange() != null) {
            aiComponent.getRange().x = positionComponent.getX();
            aiComponent.getRange().y = positionComponent.getY();
            followPlayer = aiComponent.getRange().contains(playerPositionComponent.getX(), playerPositionComponent.getY());
        }

        if (followPlayer) {

            float diffX = playerPositionComponent.getX() - positionComponent.getX();
            float diffY = playerPositionComponent.getY() - positionComponent.getY();

            float angle = (float) Math.atan2(diffY, diffX);

            velocityComponent.setX((float) (aiComponent.getSpeed() * Math.cos(angle)));
            velocityComponent.setY((float) (aiComponent.getSpeed() * Math.sin(angle)));
        } else {
            velocityComponent.setX(Math.max(0f, velocityComponent.getX() - 1f));
            velocityComponent.setY(Math.max(0f, velocityComponent.getX() - 1f));
        }
    }
}
