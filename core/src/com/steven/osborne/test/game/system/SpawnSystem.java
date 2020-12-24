package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.gameobject.component.SpawnComponent;

public class SpawnSystem extends IteratingSystem {

    private ComponentMapper<SpawnComponent> spawnComponentMapper = ComponentMapper.getFor(SpawnComponent.class);

    public SpawnSystem() {
        super(Family.all(SpawnComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpawnComponent spawnComponent = spawnComponentMapper.get(entity);

        spawnComponent.setSeconds(spawnComponent.getSeconds() + deltaTime);
        if (spawnComponent.getSeconds() > spawnComponent.getDelay()) {
            for (int i = 0; i < spawnComponent.getAmount(); i++) {
                spawnComponent.getFactory().create(getEngine(), i);
            }

            spawnComponent.setAmount(Math.min(spawnComponent.getAmount() + spawnComponent.getAmountIncrement(), spawnComponent.getMaximumAmount()));

            spawnComponent.setDelay(Math.max(spawnComponent.getDelay() - spawnComponent.getDelayDecrement(), spawnComponent.getMinimumDelay()));

            spawnComponent.setSeconds(0);
        }
    }
}
