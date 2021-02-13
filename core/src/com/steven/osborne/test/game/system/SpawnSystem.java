package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steven.osborne.test.game.component.InputComponent;
import com.steven.osborne.test.game.component.PositionComponent;
import com.steven.osborne.test.game.component.SpawnComponent;

public class SpawnSystem extends IteratingSystem {

    private ComponentMapper<SpawnComponent> spawnComponentMapper = ComponentMapper.getFor(SpawnComponent.class);
    private ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);

    private Entity player;

    public SpawnSystem() {
        super(Family.all(SpawnComponent.class).get());
    }

    @Override
    public void addedToEngine (Engine engine) {
        super.addedToEngine(engine);

        player = getEngine().getEntitiesFor(Family.all(InputComponent.class, PositionComponent.class).get()).first();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SpawnComponent spawnComponent = spawnComponentMapper.get(entity);

        spawnComponent.setSeconds(spawnComponent.getSeconds() + deltaTime);
        if (spawnComponent.getSeconds() > spawnComponent.getDelay()) {
            Rectangle spawnArea = getRandomSpawnArea(spawnComponent);
            for (int i = 0; i < spawnComponent.getAmount(); i++) {
                spawnComponent.getFactory().create(getEngine(), getRandomPosition(spawnArea));
            }

            spawnComponent.setAmount(Math.min(spawnComponent.getAmount() + spawnComponent.getAmountIncrement(), spawnComponent.getMaximumAmount()));

            spawnComponent.setDelay(Math.max(spawnComponent.getDelay() - spawnComponent.getDelayDecrement(), spawnComponent.getMinimumDelay()));

            spawnComponent.setSeconds(0);
        }
    }

    private Rectangle getRandomSpawnArea(SpawnComponent spawnComponent) {
        Rectangle spawnArea = null;
        if (spawnComponent.getSpawnAreas().size() == 1) {
            spawnArea = spawnComponent.getSpawnAreas().get(0);
        } else {
            boolean validSpawn = false;
            while (!validSpawn) { //TODO - not a very thread friendly solution - Probably be fine with only 4 options though
                int spawnAreaNum = MathUtils.random(spawnComponent.getSpawnAreas().size() - 1);
                spawnArea = spawnComponent.getSpawnAreas().get(spawnAreaNum);
                PositionComponent playerPosition = positionComponentMapper.get(player);
                if (!spawnArea.contains(playerPosition.getX(), playerPosition.getY())) {
                    validSpawn = true;
                }
            }

        }
        return spawnArea;
    }

    private Vector2 getRandomPosition(Rectangle spawnArea) {
        float randomX = MathUtils.random(spawnArea.x, spawnArea.x + spawnArea.getWidth());
        float randomY = MathUtils.random(spawnArea.y, spawnArea.y + spawnArea.getHeight());

        return new Vector2(randomX, randomY);
    }
}
