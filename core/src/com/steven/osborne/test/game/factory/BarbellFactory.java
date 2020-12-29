package com.steven.osborne.test.game.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steven.osborne.test.game.component.*;
import com.steven.osborne.test.game.event.BarbellOnDeathEvent;

import java.util.Arrays;

public class BarbellFactory implements EntityFactory {

    @Override
    public void create(Engine engine, Vector2 position) {
        Entity barbell = new Entity();
        Texture enemyTexture = new Texture("barbell_middle.png");
        barbell.add(SpriteComponent.builder().texture(enemyTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        barbell.add(PositionComponent.builder().x(position.x).y(position.y).build());
        barbell.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        barbell.add(BoundsComponent.builder().rectangle(new Rectangle(position.x, position.y, 4, 0.25f)).build());
        barbell.add(CollisionComponent.builder().tag("Barbell").isStatic(false).collideTags(Arrays.asList("Wall")).build());
        barbell.add(HealthComponent.builder().health(1).build());
        barbell.add(OnDeathComponent.builder().onDeathEvent(new BarbellOnDeathEvent()).build());

        engine.addEntity(barbell);
    }
}
