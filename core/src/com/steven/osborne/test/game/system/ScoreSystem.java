package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.steven.osborne.test.game.component.PointsComponent;
import com.steven.osborne.test.game.component.ScoreComponent;
import com.steven.osborne.test.game.component.UiComponent;

public class ScoreSystem extends IteratingSystem {

    ComponentMapper<PointsComponent> pointsComponentMapper = ComponentMapper.getFor(PointsComponent.class);
    ComponentMapper<ScoreComponent> scoreComponentMapper = ComponentMapper.getFor(ScoreComponent.class);
    ComponentMapper<UiComponent> uiComponentMapper = ComponentMapper.getFor(UiComponent.class);

    private ScoreComponent scoreComponent;
    private UiComponent uiComponent;

    public ScoreSystem() {
        super(Family.all(PointsComponent.class).get());
    }

    @Override
    public void addedToEngine (Engine engine) {
        super.addedToEngine(engine);

        Entity scoreEntity = getEngine().getEntitiesFor(Family.all(ScoreComponent.class, UiComponent.class).get()).first();
        scoreComponent = scoreComponentMapper.get(scoreEntity);
        uiComponent = uiComponentMapper.get(scoreEntity);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PointsComponent pointsComponent = pointsComponentMapper.get(entity);

        scoreComponent.setMultiplier(scoreComponent.getMultiplier() + pointsComponent.getMultiplier());

        scoreComponent.setScore(scoreComponent.getScore() + (pointsComponent.getBasePoints() * scoreComponent.getMultiplier()));

        uiComponent.setText(String.format("Score: %d    |    Multiplier: %d", scoreComponent.getScore(), scoreComponent.getMultiplier()));
    }
}
