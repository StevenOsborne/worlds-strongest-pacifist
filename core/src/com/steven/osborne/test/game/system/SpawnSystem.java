package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steven.osborne.test.game.component.SpawnComponent;

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
            int spawnAreaNum = MathUtils.random(spawnComponent.getSpawnAreas().size() - 1);
            for (int i = 0; i < spawnComponent.getAmount(); i++) {
                spawnComponent.getFactory().create(getEngine(), getRandomPosition(spawnComponent, spawnAreaNum));
            }

            spawnComponent.setAmount(Math.min(spawnComponent.getAmount() + spawnComponent.getAmountIncrement(), spawnComponent.getMaximumAmount()));

            spawnComponent.setDelay(Math.max(spawnComponent.getDelay() - spawnComponent.getDelayDecrement(), spawnComponent.getMinimumDelay()));

            spawnComponent.setSeconds(0);
        }
    }

    private Vector2 getRandomPosition(SpawnComponent spawnComponent, int spawnAreaNum) {
        Rectangle spawnArea = spawnComponent.getSpawnAreas().get(spawnAreaNum);
        float randomX = MathUtils.random(spawnArea.x, spawnArea.x + spawnArea.getWidth());
        float randomY = MathUtils.random(spawnArea.y, spawnArea.y + spawnArea.getHeight());

        return new Vector2(randomX, randomY);
    }
}
