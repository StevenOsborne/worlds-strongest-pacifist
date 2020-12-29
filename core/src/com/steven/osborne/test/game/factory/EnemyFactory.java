package com.steven.osborne.test.game.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.steven.osborne.test.game.component.*;
import com.steven.osborne.test.game.event.EnemyOnDeathEvent;

import java.util.Arrays;

public class EnemyFactory implements EntityFactory {

    @Override
    public void create(Engine engine, Vector2 position) {
        Entity enemy = new Entity();
        Texture enemyTexture = new Texture("enemy.png");
        enemy.add(SpriteComponent.builder().texture(enemyTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        enemy.add(PositionComponent.builder().x(position.x).y(position.y).build());
        enemy.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        enemy.add(BoundsComponent.builder().rectangle(new Rectangle(position.x, position.y, 1, 1)).build());
        enemy.add(CollisionComponent.builder().tag("Enemy").isStatic(false).destroyTags(Arrays.asList("PlayerInset")).collideTags(Arrays.asList("Wall", "Enemy")).build());
        enemy.add(HealthComponent.builder().health(1).build());
        enemy.add(AiComponent.builder().speed(10f).build());
        enemy.add(OnDeathComponent.builder().onDeathEvent(new EnemyOnDeathEvent()).build());

        engine.addEntity(enemy);
    }
}
