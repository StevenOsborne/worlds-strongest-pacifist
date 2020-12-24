package com.steven.osborne.test.game.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.steven.osborne.test.game.gameobject.component.*;

import java.util.Arrays;

public class EnemyFactory implements EntityFactory {

    @Override
    public void create(Engine engine, int count) {
        Entity enemy = new Entity();
        Texture enemyTexture = new Texture("enemy.png");
        enemy.add(SpriteComponent.builder().texture(enemyTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        enemy.add(PositionComponent.builder().x(10f + (count * 1.5f)).y(0f).build());
        enemy.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        enemy.add(BoundsComponent.builder().bounds(new Rectangle(10f + (count * 1.5f), 0f, 1, 1)).build());
        enemy.add(CollisionComponent.builder().tag("Enemy").isStatic(false).collidingTags(Arrays.asList("Player")).build());
        enemy.add(HealthComponent.builder().health(1).build());

        engine.addEntity(enemy);
    }
}
