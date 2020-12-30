package com.steven.osborne.test.game.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.steven.osborne.test.game.component.BodyComponent;
import com.steven.osborne.test.game.component.HealthComponent;
import com.steven.osborne.test.game.component.OnDeathComponent;

public class DeathSystem extends IteratingSystem {

    private ComponentMapper<HealthComponent> healthComponentMapper = ComponentMapper.getFor(HealthComponent.class);
    private ComponentMapper<OnDeathComponent> onDeathComponentMapper = ComponentMapper.getFor(OnDeathComponent.class);
    private ComponentMapper<BodyComponent> bodyComponentMapper = ComponentMapper.getFor(BodyComponent.class);

    private World world;

    public DeathSystem(World world) {
        super(Family.all(HealthComponent.class).get());

        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent healthComponent = healthComponentMapper.get(entity);

        if (healthComponent.getHealth() <= 0 ) {
            if (onDeathComponentMapper.has(entity)) {
                OnDeathComponent onDeathComponent = onDeathComponentMapper.get(entity);
                onDeathComponent.getOnDeathEvent().execute(getEngine(), entity);
            }
            if (bodyComponentMapper.has(entity)) {
                BodyComponent bodyComponent = bodyComponentMapper.get(entity);
                world.destroyBody(bodyComponent.getBody());
            }
            getEngine().removeEntity(entity);
        }
    }
}
