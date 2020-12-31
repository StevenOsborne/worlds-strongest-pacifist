package com.steven.osborne.test.game.event;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.steven.osborne.test.game.component.*;

import java.util.Arrays;

public class EnemyOnDeathEvent implements OnDeathEvent {

    @Override
    public void execute(Engine engine, Entity entity) {
        PositionComponent positionComponent = entity.getComponent(PositionComponent.class);
        Entity multiplier = new Entity();
        Texture multiplierTexture = new Texture("multiplier.png");
        multiplier.add(SpriteComponent.builder().texture(multiplierTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        multiplier.add(PositionComponent.builder().x(positionComponent.getX()).y(positionComponent.getY()).build());
        multiplier.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        multiplier.add(CollisionComponent.builder().tag("Multiplier").isStatic(false).collideTags(Arrays.asList("Wall")).build());
        multiplier.add(HealthComponent.builder().health(1).build());
        multiplier.add(LifetimeComponent.builder().lifetime(5f).build());
        multiplier.add(AiComponent.builder().speed(25f).range(new Circle(positionComponent.getX(), positionComponent.getY(), 5f)).build());

        engine.addEntity(multiplier);
    }
}
