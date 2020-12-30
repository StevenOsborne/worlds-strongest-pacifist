package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.component.HealthComponent;
import com.steven.osborne.test.game.component.ParentComponent;

public class ParentSystem extends IteratingSystem {

    private ComponentMapper<ParentComponent> parentComponentMapper = ComponentMapper.getFor(ParentComponent.class);
    private ComponentMapper<HealthComponent> healthComponentMapper = ComponentMapper.getFor(HealthComponent.class);

    public ParentSystem() {
        super(Family.all(ParentComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ParentComponent parentComponent = parentComponentMapper.get(entity);

        if (healthComponentMapper.has(entity) && healthComponentMapper.has(parentComponent.getParent())) {
            HealthComponent healthComponent = healthComponentMapper.get(entity);
            HealthComponent parentHealthComponent = healthComponentMapper.get(parentComponent.getParent());

            if (parentComponent.getMode() == ParentComponent.Mode.CHILD_KILL_PARENT) {
                parentHealthComponent.setHealth(healthComponent.getHealth());
            } else {
                healthComponent.setHealth(parentHealthComponent.getHealth());
            }
        }
    }
}
