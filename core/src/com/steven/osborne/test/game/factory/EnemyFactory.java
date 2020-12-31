package com.steven.osborne.test.game.factory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.steven.osborne.test.game.component.*;
import com.steven.osborne.test.game.event.EnemyOnDeathEvent;

import java.util.Arrays;

public class EnemyFactory implements EntityFactory {

    private World world;

    public EnemyFactory(World world) {
        this.world = world;
    }

    @Override
    public void create(Engine engine, Vector2 position) {
        Entity enemy = new Entity();
        Texture enemyTexture = new Texture("enemy.png");
        enemy.add(SpriteComponent.builder().texture(enemyTexture).visible(true).build());//TODO - This should use a texture atlas - When we have more textures
        enemy.add(PositionComponent.builder().x(position.x).y(position.y).build());
        enemy.add(VelocityComponent.builder().x(0.0f).y(0.0f).build());
        enemy.add(CollisionComponent.builder().tag("Enemy").isStatic(false).destroyTags(Arrays.asList("Player")).collideTags(Arrays.asList("Wall", "Enemy")).build());
        enemy.add(HealthComponent.builder().health(1).build());
        enemy.add(AiComponent.builder().speed(10f).build());
        enemy.add(OnDeathComponent.builder().onDeathEvent(new EnemyOnDeathEvent(world)).build());
        enemy.add(BodyComponent.builder().body(createBody(position, 0.5f, 0.5f, enemy)).build());

        engine.addEntity(enemy);
    }

    private Body createBody(Vector2 position, float halfX, float halfY, Entity entity) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);

        PolygonShape box = new PolygonShape();
        box.setAsBox(halfX, halfY);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 0.0f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(entity);

        box.dispose();

        return body;
    }
}
