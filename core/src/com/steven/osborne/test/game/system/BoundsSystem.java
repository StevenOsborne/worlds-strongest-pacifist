package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.gameobject.component.BoundsComponent;
import com.steven.osborne.test.game.gameobject.component.PositionComponent;

public class BoundsSystem extends IteratingSystem {

    private ComponentMapper<BoundsComponent> boundsComponentMapper = ComponentMapper.getFor(BoundsComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);

    public BoundsSystem() {
        super(Family.all(BoundsComponent.class, PositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BoundsComponent bounds = boundsComponentMapper.get(entity);
        PositionComponent position = positionComponentMapper.get(entity);

        bounds.getBounds().x = position.getX();
        bounds.getBounds().y = position.getY();
    }
}
